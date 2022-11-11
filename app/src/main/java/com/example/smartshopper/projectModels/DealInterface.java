package com.example.smartshopper.projectModels;

import java.util.List;

public interface DealInterface {
    // Name of item
    String getTitle();

    // Description of deal terms (e.g. "Buy 1 get 1 free")
    String getDescription();

    // Time deal was posted
    Long getTimePosted();

    // Get store name of deal
    String getStore();

    // Get UPC of deal
    Long getUPC();

    // Get original price of deal
    Double getPrice();

    // Get sale price of deal
    Double getSalePrice();

    // Get ammount saved
    Double getSavings();

    void addComment(Comment comment);

    List<Comment> getComments();

    void upvote();

    void downvote();

    Integer getVoteScore();

    Integer getCommentsCount();

    UserInterface getPoster();

    String getImageURI();
}
