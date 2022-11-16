package com.example.smartshopper.projectModels;

import org.json.JSONObject;

import java.util.List;

public class Deal {
    Long upc;
    String title;
    String description;
    Long timePosted;
    String store;
    Double price;
    Double salePrice;
    User poster;
    List<Comment> comments;
    Integer upvotes;
    Integer downvotes;
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
        this.upvotes = 0;
        this.downvotes = 0;
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
        this.upvotes = 0;
        this.downvotes = 0;
        this.imageURI = "";
    }

    // For use with firebase to make a deal object from a json object (what snapshot.getValue returns)
    // TODO: implement
    public Deal() {
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public Long getTimePosted() {
        return this.timePosted;
    }

    public String getStore() {
        return this.store;
    }

    public Long getUPC() {
        return this.upc;
    }

    public Double getPrice() {
        return this.price;
    }

    public Double getSalePrice() {
        return this.salePrice;
    }

    public Double getSavings() {
        return this.price - this.salePrice;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public List<Comment> getComments() {
        return this.comments;
    }

    public void upvote() {
        upvotes++;
    }

    public void downvote() {
        downvotes--;
    }

    public Integer getUpvotes() {
        return this.upvotes;
    }

    public Integer getDownvotes() { return this.downvotes; }

    public Integer getCommentsCount() { return comments.size(); }

    public User getPoster() { return this.poster; }

    public String getImageURI() { return imageURI; }
}
