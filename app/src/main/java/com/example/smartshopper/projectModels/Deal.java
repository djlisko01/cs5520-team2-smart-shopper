package com.example.smartshopper.projectModels;

import org.json.JSONObject;

import java.util.List;

public class Deal implements DealInterface {
    Long upc;
    String title;
    String description;
    Long timePosted;
    String store;
    Double price;
    Double salePrice;
    User poster;
    List<Comment> comments;
    Integer voteScore;
    String imageURI;

    // Initial Creation - For deals that have a UPC
    public Deal(Long upc, String title, Double price, Double salePrice, String description, String store, User poster) {
        this.upc = upc;
        this.title = title;
        this.price = price;
        this.salePrice = salePrice;
        this.description = description;
        this.timePosted = System.currentTimeMillis();
        this.store = store;
        this.poster = poster;
        this.voteScore = 0;
        this.imageURI = "";
    }

    // Initial Creation - For inserting a deal that lacks a UPC (e.g. Tomatoes 0.99/lb)
    public Deal(String title, Double price, Double salePrice, String description, String store, User poster) {
        this.upc = null;
        this.title = title;
        this.price = price;
        this.salePrice = salePrice;
        this.description = description;
        this.timePosted = System.currentTimeMillis();
        this.store = store;
        this.poster = poster;
        this.voteScore = 0;
        this.imageURI = "";
    }

    // For use with firebase to make a deal object from a json object (what snapshot.getValue returns)
    // TODO: implement
    public Deal(JSONObject json) {
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getTimePosted() {
        return null;
    }

    @Override
    public String getStore() {
        return null;
    }

    @Override
    public Integer getUPC() {
        return null;
    }

    @Override
    public Double getPrice() {
        return null;
    }

    @Override
    public Double getSalePrice() {
        return null;
    }

    @Override
    public Void addComment(CommentInterface comment) {
        return null;
    }

    @Override
    public List<Comment> getComments() {
        return null;
    }

    @Override
    public Void upvote() {
        return null;
    }

    @Override
    public Void downvote() {
        return null;
    }

    @Override
    public Integer getVoteScore() {
        return null;
    }

    @Override
    public Integer getCommentsCount() {
        return null;
    }

    @Override
    public UserInterface getPoster() {
        return null;
    }

    @Override
    public String getImageURI() {
        return imageURI;
    }


}
