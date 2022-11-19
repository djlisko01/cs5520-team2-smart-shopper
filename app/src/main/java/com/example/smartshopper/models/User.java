package com.example.smartshopper.models;

import java.io.Serializable;

public class User implements Serializable {
    String userID;
    String username;

    // For use with firebase to make a user object from a json object (what snapshot.getValue returns)
    public User() {
    }

    public User(String username) {
        this.username = username;
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

    public String toString() {
        return this.username;
    }


}