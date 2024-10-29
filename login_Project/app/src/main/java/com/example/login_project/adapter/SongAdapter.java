package com.example.login_project.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.login_project.R;
import com.example.login_project.model.Song;

import java.io.IOException;
import java.util.ArrayList;

public class SongAdapter extends ArrayAdapter<Song> {
    private Context context;

    public SongAdapter(Context context, ArrayList<Song> songs) {
        super(context, 0, songs);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Song song = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.song_list_item, parent, false);
        }

        TextView textTitle = convertView.findViewById(R.id.textTitle);
        TextView textArtist = convertView.findViewById(R.id.textArtist);
        ImageView imageViewCover = convertView.findViewById(R.id.imageViewCover);

        if (song != null) {
            textTitle.setText(song.getTitle());
            textArtist.setText(song.getArtist());

            // Kiểm tra URI và hiển thị ảnh bìa
            if (song.getCoverPath() != null && imageViewCover != null) {
                Uri coverUri = Uri.parse(song.getCoverPath());
                try {
                    getContext().getContentResolver().takePersistableUriPermission(coverUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    imageViewCover.setImageURI(coverUri);
                } catch (SecurityException e) {
                    imageViewCover.setImageResource(R.drawable.ic_launcher_background);
                }
            }
        }
        return convertView;
    }
}
  