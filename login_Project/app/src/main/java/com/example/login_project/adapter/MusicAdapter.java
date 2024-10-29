package com.example.login_project.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.login_project.R;
import com.example.login_project.model.Music;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {
    private List<Music> musicList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Music music);
    }

    public MusicAdapter(List<Music> musicList, OnItemClickListener listener) {
        this.musicList = musicList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        Music music = musicList.get(position);
        holder.bind(music, listener);
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public static class MusicViewHolder extends RecyclerView.ViewHolder {
        TextView title, artist;
        ImageView image;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.musicTitle);
            artist = itemView.findViewById(R.id.musicArtist);
            image = itemView.findViewById(R.id.musicImage);
        }

        public void bind(Music music, OnItemClickListener listener) {
            title.setText(music.getTitle());
            artist.setText(music.getArtist());

            // Load image from URL (use a library like Glide)
            Glide.with(image.getContext()).load(music.getImageUrl()).into(image);

            itemView.setOnClickListener(v -> listener.onItemClick(music));
        }
    }
}




