package com.example.login_project.api;
import com.example.login_project.model.Music;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MusicApiService {
    @GET("/api/music/all")
    Call<List<Music>> getAllMusic();

    @GET("/api/music/play/{id}")
    Call<byte[]> playMusic(@Path("id") Long id);
}

