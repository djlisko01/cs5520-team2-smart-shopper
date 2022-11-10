package com.example.team2.projectModels;

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
        return null;
    }

    @Override
    public Long getTimePosted() {
        return null;
    }

    @Override
    public User getPoster() {
        return null;
    }
}
