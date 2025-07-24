package com.example.mbchats;

public class Users {
    public String id;
    public String username;
    public String email;
    public String password;
    public String imageUrl;
    public String status;

    // Empty constructor required for Firebase
    public Users() {}

    public Users(String id, String username, String email,
                 String password, String imageUrl, String status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
        this.status = status;
    }
}