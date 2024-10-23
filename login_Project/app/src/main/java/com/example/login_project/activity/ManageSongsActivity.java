package com.example.login_project.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.login_project.R;
import com.example.login_project.adapter.SongAdapter;
import com.example.login_project.helper.SongDatabaseHelper;
import com.example.login_project.model.Song;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ManageSongsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;
    private ListView listViewSongs;
    private Button btnAddSong;
    private SongDatabaseHelper dbHelper;
    private ArrayList<Song> songList;
    private SongAdapter songAdapter;
    private String selectedFilePath;
    private String selectedCoverFilePath;
    private ImageView imageViewCover;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_songs);

        // Kiểm tra và yêu cầu quyền truy cập bộ nhớ
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            // Nếu quyền đã được cấp, khởi tạo giao diện ngay lập tức
            initializeUI();
        }
    }


    private void initializeUI() {
        listViewSongs = findViewById(R.id.listViewSongs);
        btnAddSong = findViewById(R.id.btnAddSong);
        dbHelper = new SongDatabaseHelper(this);
        songList = dbHelper.getAllSongs();
        songAdapter = new SongAdapter(this, songList);
        listViewSongs.setAdapter(songAdapter);

        btnAddSong.setOnClickListener(v -> showAddSongDialog());
    }

    // Hiển thị dialog thêm bài hát
    private void showAddSongDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_song, null);
        dialogBuilder.setView(dialogView);

        EditText editTextTitle = dialogView.findViewById(R.id.editTextTitle);
        EditText editTextArtist = dialogView.findViewById(R.id.editTextArtist);
        Button btnChooseFile = dialogView.findViewById(R.id.btnChooseFile);
        Button btnChooseCover = dialogView.findViewById(R.id.btnChooseCover);
        imageViewCover = dialogView.findViewById(R.id.imageViewCover);

        // Chọn tệp âm thanh
        btnChooseFile.setOnClickListener(v -> chooseFile("audio/*", 1));

        // Chọn ảnh bìa
        btnChooseCover.setOnClickListener(v -> chooseFile("image/*", 2));

        dialogBuilder.setTitle("Thêm bài hát mới");
        dialogBuilder.setPositiveButton("Lưu", (dialog, whichButton) -> {
            String title = editTextTitle.getText().toString();
            String artist = editTextArtist.getText().toString();

            if (title.isEmpty() || artist.isEmpty() || selectedFilePath == null || selectedCoverFilePath == null) {
                Toast.makeText(ManageSongsActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHelper.addSong(new Song(0, title, artist, selectedFilePath, selectedCoverFilePath));
            songList.clear();
            songList.addAll(dbHelper.getAllSongs());
            songAdapter.notifyDataSetChanged();
        });
        dialogBuilder.setNegativeButton("Hủy", (dialog, whichButton) -> dialog.dismiss());
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    // Phương thức chung để chọn tệp
    private void chooseFile(String mimeType, int requestCode) {
        if (ContextCompat.checkSelfPermission(ManageSongsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType(mimeType);
            startActivityForResult(intent, requestCode);
        } else {
            Toast.makeText(ManageSongsActivity.this, "Vui lòng cấp quyền truy cập bộ nhớ để chọn tệp.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            if (uri != null) {
                if (requestCode == 1) { // Tệp âm thanh
                    selectedFilePath = getPathFromUri(uri); // Sử dụng đường dẫn thực sự nếu có thể
                    if (selectedFilePath != null) {
                        Toast.makeText(this, "Chọn tệp thành công.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Lưu trực tiếp URI nếu không có đường dẫn file cụ thể
                        selectedFilePath = uri.toString();
                        Toast.makeText(this, "Chọn tệp thành công. Phát từ URI.", Toast.LENGTH_SHORT).show();
                    }
                } else if (requestCode == 2) { // Ảnh bìa
                    selectedCoverFilePath = uri.toString(); // Lưu URI ảnh

                    if (imageViewCover != null) {
                        imageViewCover.setVisibility(View.VISIBLE);
                        imageViewCover.setImageURI(uri); // Hiển thị ảnh bìa
                    }

                    Toast.makeText(this, "Chọn ảnh bìa thành công.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private String getPathFromUri(Uri uri) {
        String path = null;
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            String[] split = docId.split(":");
            String type = split[0];

            if ("primary".equalsIgnoreCase(type)) {
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Audio.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                if (column_index != -1) {
                    cursor.moveToFirst();
                    path = cursor.getString(column_index);
                }
                cursor.close();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
        }
        return path; // Trả về null nếu không tìm thấy đường dẫn thực sự
    }


    // Lưu tệp từ Google Drive
    private String copyFileToInternalStorage(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        String fileName = getFileName(uri);

        File file = new File(getFilesDir(), fileName);

        try (InputStream inputStream = contentResolver.openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Lấy tên tệp từ URI
    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    // Kiểm tra URI từ Google Drive
    private boolean isGoogleDriveUri(Uri uri) {
        return "com.google.android.apps.docs.storage".equals(uri.getAuthority());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Quyền truy cập bộ nhớ đã được cấp.", Toast.LENGTH_SHORT).show();
                initializeUI(); // Khởi tạo giao diện nếu quyền được cấp
            } else {
                Toast.makeText(this, "Quyền truy cập bộ nhớ bị từ chối.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
