package com.example.smartshopper.models;

import java.io.Serializable;
import java.util.List;

public class Comment implements Serializable {
    String commentID;
    User author;
    String text;
    Long timePosted;
    List<Comment> response;

    // For use with firebase to make a deal object from a json object (what snapshot.getValue returns)
    public Comment() {
    }

//    // Constructor for use with initialization
//    public Comment(User author, String text) {
//        this.author = author;
//        this.text = text;
//        this.timePosted = System.currentTimeMillis();
//    }

    public Comment(User author, String text, Long timePosted) {
        this.author = author;
        this.text = text;
        this.timePosted = timePosted;
    }

    // Constructor for responses to main comment in a thread.
    public Comment(User author, String text, Long timePosted, List<Comment> response) {
        this.author = author;
        this.text = text;
        this.timePosted = timePosted;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
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
