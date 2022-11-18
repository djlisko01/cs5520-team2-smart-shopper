package com.example.smartshopper.models;

import org.json.JSONObject;

import java.io.Serializable;

public class Comment implements Serializable {
    String author;
    String text;
    Long timePosted;

    // Constructor for use with initialization
    public Comment(String author, String text) {
        this.author = author;
        this.text = text;
        this.timePosted = System.currentTimeMillis();
    }

    // For use with firebase to make a comment object from a json object (what snapshot.getValue returns)
    public Comment() {
    }

    public String getText() {
        return this.text;
    }

    public Long getTimePosted() {
        return this.timePosted;
    }

    public String getAuthor() {
        return this.author;
    }
}
