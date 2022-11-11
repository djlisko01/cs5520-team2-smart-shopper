package com.example.smartshopper.projectModels;

import org.json.JSONObject;

public class User implements UserInterface {
    String username;

    public User(String username) {
        this.username = username;
    }

    // For use with firebase to make a user object from a json object (what snapshot.getValue returns)
    // TODO: implement
    public User(JSONObject json) {
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String toString() { return this.username; }


    // Deals the user has saved
//    public abstract List<AbstractDeal> getSavedDeals();
//
//    // Deals the user has posted
//    public abstract List<AbstractDeal> getPostedDeals();
//
//    // List of user's comments
//    public abstract List<AbstractComment> getComments();
//
//    // List of user's friends - does this go here or in a separate class?
//    public abstract List<AbstractUser> getFriends();


}