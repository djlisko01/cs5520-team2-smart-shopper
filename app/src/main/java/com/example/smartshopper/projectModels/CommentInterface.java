package com.example.team2.projectModels;

public interface CommentInterface {
    // Get comment text
    String getText();

    // Get comment time
    Long getTimePosted();

    // Get comment poster user
    User getPoster();
}
