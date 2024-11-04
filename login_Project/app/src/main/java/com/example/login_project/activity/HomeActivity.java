package com.example.login_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.login_project.R;
import com.example.login_project.adapter.MusicAdapter;
import com.example.login_project.model.Music;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private MusicAdapter musicAdapter, topSongsAdapter;
    private List<Music> musicList = new ArrayList<>();
    private List<Music> topSongsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Khai báo RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerAlbum);
        RecyclerView recyclerTopSongs = findViewById(R.id.recyclerTopSongs);

        LinearLayoutManager albumLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(albumLayoutManager);

        LinearLayoutManager topSongsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerTopSongs.setLayoutManager(topSongsLayoutManager);

        musicAdapter = new MusicAdapter(musicList, this::openMusicPlayer);
        recyclerView.setAdapter(musicAdapter);

        topSongsAdapter = new MusicAdapter(topSongsList, this::openMusicPlayer);
        recyclerTopSongs.setAdapter(topSongsAdapter);

        fetchMusicList();
        fetchTop10Songs();


        //Home button
        ImageView homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v-> {
            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Ends the current activity
        });

        //Profile button
        ImageView profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void openMusicPlayer(Music music) {
        Intent intent = new Intent(HomeActivity.this, MusicPlayerActivity.class);
        intent.putExtra("MUSIC_URL", music.getMusicUrl());
        intent.putExtra("IMAGE_URL", music.getImageUrl());
        intent.putExtra("TITLE", music.getTitle());
        intent.putExtra("ARTIST", music.getArtist());
        startActivity(intent);
    }

    private void fetchMusicList() {
        String url = "http://172.16.6.228:8080/api/music/all";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            Music music = new Music();
                            music.setId(jsonObject.getLong("id"));
                            music.setTitle(jsonObject.getString("title"));
                            music.setArtist(jsonObject.getString("artist"));
                            music.setMusicUrl("http://172.16.6.228:8080/api/music/play/" + music.getId());
                            music.setImageUrl("http://172.16.6.228:8080" + jsonObject.optString("imageUrl", ""));

                            musicList.add(music);
                        }
                        musicAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to parse music data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Failed to fetch music list: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void fetchTop10Songs() {
        String url = "http://172.16.6.228:8080/api/music/top10"; // API endpoint for top 10 songs

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            Music music = new Music();
                            music.setId(jsonObject.getLong("id"));
                            music.setTitle(jsonObject.getString("title"));
                            music.setArtist(jsonObject.getString("artist"));
                            music.setMusicUrl("http://172.16.6.228:8080/api/music/play/" + music.getId());
                            music.setImageUrl("http://172.16.6.228:8080" + jsonObject.optString("imageUrl", ""));

                            topSongsList.add(music);
                        }
                        topSongsAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to parse top songs data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("FetchTop10Songs", "Error: " + error.toString());
                    Toast.makeText(this, "Failed to fetch top 10 songs: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}

