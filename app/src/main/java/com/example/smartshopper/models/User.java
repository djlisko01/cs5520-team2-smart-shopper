package com.example.smartshopper.models;

import java.io.Serializable;

public class User implements Serializable {
    String userID;
    String username;
    String email;
    String password;
    String fcmToken;
    String profileImgUrl;

    // For use with firebase to make a user object from a json object (what snapshot.getValue returns)
    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String email, String password, String fcmToken) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fcmToken = fcmToken;
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

    public String getFcmToken() {
        return fcmToken;
    }

    public String toString() {
        return this.username;
    }

    public String getProfileImgUrl() { return profileImgUrl;}

    public void setProfileImgUrl(String profileImgUrl) { this.profileImgUrl=profileImgUrl; }

}