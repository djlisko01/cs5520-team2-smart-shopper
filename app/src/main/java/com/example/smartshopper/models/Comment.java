package com.example.smartshopper.models;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Comment implements Serializable, Comparable<Comment> {
    String commentID;
    User author;
    String text;
    Long timePosted;
    Map<String, Comment> responses;
    List<Comment> listReplies;

    // For use with firebase to make a deal object from a json object (what snapshot.getValue returns)
    public Comment() {
    }

    public Comment(User author, String text, Long timePosted) {
        this.author = author;
        this.text = text;
        this.timePosted = timePosted;
    }

    // Constructor for responses to main comment in a thread.
    public Comment(User author, String text, Long timePosted,  Map<String, Comment> responses) {
        this.author = author;
        this.text = text;
        this.timePosted = timePosted;
        this.responses = responses;
    }

    public String getCommentID() {
        return commentID;
    }

    public Map<String, Comment> getResponses() {
        return responses;
    }

    public List<Comment> getListReplies() {
        if (listReplies == null){
            listReplies = RepliesMapToList();
        }
        return listReplies;
    }

    public void setListReplies(List<Comment> listReplies) {
        this.listReplies = listReplies;
    }

    public List<Comment> RepliesMapToList() {
        List<Comment> listReplies = new ArrayList<>();
        if (responses != null) {
            for (String key:  responses.keySet()){
                Comment currReply =  responses.get(key);
                listReplies.add(currReply);
            }
        }
        Collections.sort(listReplies);
        Collections.reverse(listReplies);
        return listReplies;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getText() {
        return this.text;
    }

//    public void addResponse(Comment response){
//        this.responses.add(response);
//    }
//
//    public void addResponse(int index, Comment response){
//        this.responses.add(index, response);
//    }


    public Long getTimePosted() {
        return this.timePosted;
    }

    public User getAuthor() {
        return this.author;
    }

    // Used to sort by time posted.
    @Override
    public int compareTo(Comment comment) {
        if (getTimePosted() == null || comment.getTimePosted() == null){
            return 0;
        }
        return getTimePosted().compareTo(comment.getTimePosted());
    }
}
