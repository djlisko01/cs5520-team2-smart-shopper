package com.example.smartshopper.projectModels;

public interface CommentInterface {
    // Get comment text
    String getText();

    // Get comment time
    Long getTimePosted();

    // Get comment poster user
    User getPoster();
}
