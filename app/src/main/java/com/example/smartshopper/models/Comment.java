package com.example.smartshopper.models;

import org.json.JSONObject;

import java.io.Serializable;

public class Comment implements Serializable {
    User author;
    String text;
    Long timePosted;

    public Comment (){

    }

    // Constructor for use with initialization
    public Comment(User author, String text) {
        this.author = author;
        this.text = text;
        this.timePosted = System.currentTimeMillis();
    }

    // For use with firebase to make a comment object from a json object (what snapshot.getValue returns)
    public Comment(User author, String text, Long timePosted) {
        this.author = author;
        this.text = text;
        this.timePosted = timePosted;
    }

    public String getText() {
        return this.text;
    }

    public Long getTimePosted() {
        return this.timePosted;
    }

    public User getAuthor() {
        return this.author;
    }
}
