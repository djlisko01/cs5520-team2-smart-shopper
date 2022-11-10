package com.example.team2.projectModels;

import java.util.List;

public interface DealInterface {
    // Name of item
    String getTitle();

    // Description of deal terms (e.g. "Buy 1 get 1 free")
    String getDescription();

    // Time deal was posted
    String getTimePosted();

    // Get store name of deal
    String getStore();

    // Get UPC of deal
    Integer getUPC();

    // Get original price of deal
    Double getPrice();

    // Get sale price of deal
    Double getSalePrice();

    Void addComment(CommentInterface comment);

    List<Comment> getComments();

    Void upvote();

    Void downvote();

    Integer getVoteScore();

    Integer getCommentsCount();

    UserInterface getPoster();
}
