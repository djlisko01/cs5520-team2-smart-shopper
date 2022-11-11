package com.example.smartshopper.common;

import com.example.smartshopper.projectModels.Deal;
import com.example.smartshopper.projectModels.User;
import com.example.smartshopper.projectModels.Comment;

public interface PlatformHelpersInterface {

    // User Methods to implement
    User getCurrentUser();

    User getUser(String username);

    void setUser(String username);


    // Deal Methods to implement
    Deal getDeal(String dealId);

    String getDealId(Deal deal);

    void addDeal(Deal deal);

    void addComment(Comment comment);

    void getDealsBySearch(String search);
}
