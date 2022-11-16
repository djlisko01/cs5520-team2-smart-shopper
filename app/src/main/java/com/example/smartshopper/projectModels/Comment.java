package com.example.smartshopper.projectModels;

import org.json.JSONObject;

public class Comment {
    User author;
    String text;
    Long timePosted;

    // Constructor for use with initialization
    public Comment(User author, String text) {
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

    public User getAuthor() {
        return this.author;
    }
}
