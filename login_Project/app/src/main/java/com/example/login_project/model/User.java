package com.example.login_project.model;

public class User {
    private int id;
    private String email;
    private String password;
    private String username;
    private String gender;
    private int role;
    private String image;

    public User() {}

    public User(int id, String email, String password, String username, String gender, int role, String image) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.gender = gender;
        this.role = role;
        this.image = image;
    }

    public User(int id, String username, String email, String gender) {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getImage() { return image;}

    public void setImage(String image) { this.image = image;}

}
