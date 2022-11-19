package com.example.smartshopper.models;

import java.io.Serializable;

public class User implements Serializable {
    String userUUID;
    String username;

    // For use with firebase to make a user object from a json object (what snapshot.getValue returns)
    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public String getUsername() {
        return this.username;
    }

    public String toString() {
        return this.username;
    }


}