package com.example.login_project.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.login_project.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private ImageView playButton;
    private ImageView albumImageView;
    private TextView titleTextView, artistTextView, currentTimeTextView, totalTimeTextView;
    private SeekBar seekBar;
    private RelativeLayout mainLayout;
    private String musicUrl;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        mainLayout = findViewById(R.id.mainLayout);

        // Lấy màu từ màu nền
        if (mainLayout.getBackground() instanceof ColorDrawable) {
            int backgroundColor = ((ColorDrawable) mainLayout.getBackground()).getColor();
            getWindow().setStatusBarColor(backgroundColor);
        } else {
            // Gán màu mặc định nếu không thể lấy màu từ ColorDrawable
            getWindow().setStatusBarColor(getResources().getColor(R.color.defaultBackgroundColor));
        }
        playButton = findViewById(R.id.playButton);
        albumImageView = findViewById(R.id.ImageView);
        titleTextView = findViewById(R.id.titleTextView);
        artistTextView = findViewById(R.id.artistTextView);
        currentTimeTextView = findViewById(R.id.currentTimeTextView);
        totalTimeTextView = findViewById(R.id.totalTimeTextView);
        seekBar = findViewById(R.id.seekBar);

        Intent intent = getIntent();
        musicUrl = intent.getStringExtra("MUSIC_URL");
        String imageUrl = intent.getStringExtra("IMAGE_URL");
        String title = intent.getStringExtra("TITLE");
        String artist = intent.getStringExtra("ARTIST");

        titleTextView.setText(title);
        artistTextView.setText(artist);

        // Load ảnh bằng Glide và tạo Palette từ Bitmap khi tải thành công
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        Toast.makeText(MusicPlayerActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        albumImageView.setImageBitmap(resource);
                        createPaletteAsync(resource);
                        return true;
                    }
                })
                .into(albumImageView);

        playButton.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playButton.setImageResource(R.drawable.play); // Đổi icon thành Play
                } else {
                    mediaPlayer.start();
                    playButton.setImageResource(R.drawable.pause); // Đổi icon thành Pause
                }
            } else {
                playMusic(musicUrl);
                playButton.setImageResource(R.drawable.pause); // Đổi icon thành Pause
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    private void createPaletteAsync(Bitmap bitmap) {
        Palette.from(bitmap).generate(palette -> {
            if (palette != null) {
                int dominantColor = palette.getDominantColor(getResources().getColor(R.color.defaultBackgroundColor));
                mainLayout.setBackgroundColor(dominantColor);
            }
        });
    }

    private void playMusic(String musicUrl) {
        new Thread(() -> {
            try {
                URL url = new URL(musicUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                File tempFile = File.createTempFile("music", ".mp3", getCacheDir());
                FileOutputStream outputStream = new FileOutputStream(tempFile);
                InputStream inputStream = connection.getInputStream();

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();

                runOnUiThread(() -> {
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }

                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(tempFile.getAbsolutePath());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        seekBar.setMax(mediaPlayer.getDuration());
                        totalTimeTextView.setText(formatTime(mediaPlayer.getDuration()));
                        playButton.setImageResource(R.drawable.pause);
                        Toast.makeText(this, "Playing music", Toast.LENGTH_SHORT).show();

                        updateSeekBar();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void updateSeekBar() {
        handler.postDelayed(() -> {
            if (mediaPlayer != null) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(currentPosition);
                currentTimeTextView.setText(formatTime(currentPosition));
                updateSeekBar();
            }
        }, 1000);
    }

    private String formatTime(int milliseconds) {
        return String.format("%d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60);
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
