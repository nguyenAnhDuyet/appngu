package com.example.login_project.activity;

import android.content.Intent;
import android.os.Bundle;
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
    private RecyclerView recyclerView;
    private MusicAdapter musicAdapter;
    private List<Music> musicList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        musicAdapter = new MusicAdapter(musicList, music -> {
            Intent intent = new Intent(HomeActivity.this, MusicPlayerActivity.class);
            intent.putExtra("MUSIC_URL", music.getMusicUrl());
            intent.putExtra("IMAGE_URL", music.getImageUrl());
            intent.putExtra("TITLE", music.getTitle());
            intent.putExtra("ARTIST", music.getArtist());
            startActivity(intent);
        });
        recyclerView.setAdapter(musicAdapter);

        fetchMusicList();
    }

    private void fetchMusicList() {
        String url = "http://172.16.5.111:8080/api/music/all"; // Địa chỉ API của bạn

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            Music music = new Music();
                            music.setId(jsonObject.getLong("id"));
                            music.setTitle(jsonObject.getString("title"));
                            music.setArtist(jsonObject.getString("artist"));
                            music.setMusicUrl("http://172.16.5.111:8080/api/music/play/" + music.getId());
                            music.setImageUrl("http://172.16.5.111:8080" + jsonObject.optString("imageUrl", ""));

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
}
