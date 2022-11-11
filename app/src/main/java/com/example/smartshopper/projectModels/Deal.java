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
        return this.title;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public Long getTimePosted() {
        return this.timePosted;
    }

    @Override
    public String getStore() {
        return this.store;
    }

    @Override
    public Long getUPC() {
        return this.upc;
    }

    @Override
    public Double getPrice() {
        return this.price;
    }

    @Override
    public Double getSalePrice() { return this.salePrice; }

    @Override
    public void addComment(Comment comment) { this.comments.add(comment); }

    @Override
    public List<Comment> getComments() {
        return this.comments;
    }

    @Override
    public void upvote() {
        voteScore++;
    }

    @Override
    public void downvote() {
        voteScore--;
    }

    @Override
    public Integer getVoteScore() {
        return this.voteScore;
    }

    @Override
    public Integer getCommentsCount() {
        return comments.size();
    }

    @Override
    public User getPoster() {
        return this.poster;
    }

    @Override
    public String getImageURI() {
        return imageURI;
    }


}
