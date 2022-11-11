package com.example.smartshopper.common;

import com.example.smartshopper.projectModels.Comment;
import com.example.smartshopper.projectModels.Deal;
import com.example.smartshopper.projectModels.User;

public class PlatformHelpers implements PlatformHelpersInterface {
    @Override
    public User getCurrentUser() {
        return null;
    }

    @Override
    public User getUser(String username) {
        return null;
    }

    @Override
    public void setUser(String username) {

    }

    @Override
    public Deal getDeal(String dealId) {
        return null;
    }

    @Override
    public String getDealId(Deal deal) {
        return null;
    }

    @Override
    public void addDeal(Deal deal) {

    }

    @Override
    public void addComment(Comment comment) {

    }

    @Override
    public void getDealsBySearch(String search) {

    }
}
