package com.example.letstalk;


// this is the UserModel to display the users in a RecyclerView
public class UserModel {
    String username;

    public UserModel(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
