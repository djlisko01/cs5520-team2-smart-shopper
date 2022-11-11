package com.example.smartshopper.projectModels;

import org.json.JSONObject;

class Comment implements CommentInterface {
    User poster;
    String text;
    Long timePosted;

    // Constructor for use with initialization
    public Comment(User poster, String text) {
        this.poster = poster;
        this.text = text;
        this.timePosted = System.currentTimeMillis();
    }

    // For use with firebase to make a comment object from a json object (what snapshot.getValue returns)
    public Comment(JSONObject json) {
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public Long getTimePosted() {
        return this.timePosted;
    }

    @Override
    public User getPoster() {return this.poster;}
}
