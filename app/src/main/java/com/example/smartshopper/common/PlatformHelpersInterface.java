package com.example.smartshopper.common;

import com.example.smartshopper.projectModels.Deal;
import com.example.smartshopper.projectModels.User;

public interface PlatformHelpersInterface {

    // User Methods to implement
    User getCurrentUser();
    User getUser(String username);
    void setUser(String username);


    // Deal Methods to implement
    Deal getDeal(String dealId);
    String getDealId(Deal deal);
    void addDeal(Deal deal);
    void addComment(String comment);
    void getDealsBySearch(String search);
}
