package com.example.login_project.model;

public class Song {
    private int id;
    private String title;
    private String artist;
    private String filePath;
    private String coverPath; // Đường dẫn đến ảnh bìa

    public Song(int id, String title, String artist, String filePath, String coverPath) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.filePath = filePath;
        this.coverPath = coverPath;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }
}
