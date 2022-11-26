package com.example.smartshopper.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Deal implements Serializable {
    //TODO NOTE FIREBASE DOES NOT LIKE ARRAYS
    // https://firebase.blog/posts/2014/04/best-practices-arrays-in-firebase
    String dealID;
    String upc;
    String gtin;
    String asin;
    String isbn;
    String tags;
    Integer totalWatched;
    String title;
    String description;
    Long timePosted;
    String store;
    Double originalPrice;
    Double salePrice;
    String userUUID;
    List<Comment> comments;
    Integer numUpVotes;
    Integer numDownVotes;
    String productImg;

    // For use with firebase to make a deal object from a json object (what snapshot.getValue returns)
    public Deal() {
    }

    // Initial Creation - For deals that have a UPC
    public Deal(String upc, String title, Double originalPrice, Double salePrice, String description, String store, String userUUID) {
        this.upc = upc;
        this.title = title;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.description = description;
        this.timePosted = System.currentTimeMillis();
        this.store = store;
        this.userUUID = userUUID;
        this.numUpVotes = 0;
        this.numDownVotes = 0;
        this.productImg = "";
        this.comments = new ArrayList<>();
    }

    // Initial Creation - For inserting a deal that lacks a UPC (e.g. Tomatoes 0.99/lb)
    public Deal(String title, Double originalPrice, Double salePrice, String description, String store, String userUUID) {
        this.upc = null;
        this.title = title;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.description = description;
        this.timePosted = System.currentTimeMillis();
        this.store = store;
        this.userUUID = userUUID;
        this.numUpVotes = 0;
        this.numDownVotes = 0;
        this.productImg = "";
    }

    public void setDealID(String dealID){
        this.dealID = dealID;
    }
    public String getDealID(){
        return this.dealID;
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

    public String getUPC() {
        return this.upc;
    }

    public Double getOriginalPrice() {
        return this.originalPrice;
    }

    public Double getSalePrice() {
        return this.salePrice;
    }

//    public Double getSavings() {
//        return this.originalPrice - this.salePrice;
//    }

//    public void addComment(Comment comment) {
//        this.comments.add(comment);
//    }
//
//    public List<Comment> getComments() {
//
//        return new ArrayList<>();
//    }

    public void upvote() {
        numUpVotes++;
    }

    public void downvote() {
        numDownVotes++;
    }

    public Integer getNumUpVotes() {
        return this.numUpVotes;
    }

    public Integer getNumDownVotes() {
        return this.numDownVotes;
    }

    public Integer getCommentsCount() {
        return comments.size();
    }

    public String getUserUUID() {
        return this.userUUID;
    }

    public String getProductImg() {
        return productImg;
    }
}
