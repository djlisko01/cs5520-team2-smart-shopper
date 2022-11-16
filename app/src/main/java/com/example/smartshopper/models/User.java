package com.example.smartshopper.models;

import org.json.JSONObject;

public class User {
    String username;

    public User(String username) {
        this.username = username;
    }

    // For use with firebase to make a user object from a json object (what snapshot.getValue returns)
    public User() {
    }

    public String getUsername() {
        return this.username;
    }

    public String toString() {
        return this.username;
    }


}