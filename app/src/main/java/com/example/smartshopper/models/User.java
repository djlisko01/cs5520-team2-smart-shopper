package com.example.smartshopper.models;

import java.io.Serializable;

public class User implements Serializable {
    String userID;
    String username;
    String email;
    String password;

    // For use with firebase to make a user object from a json object (what snapshot.getValue returns)
    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public String toString() {
        return this.username;
    }

}