package com.example.smartshopper.services;

import com.example.smartshopper.common.Constants;
import com.example.smartshopper.models.Comment;
import com.example.smartshopper.models.Deal;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.example.smartshopper.models.User;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class RTDBService {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();

    public RTDBService() {
    }

    // Define Queries here

    // Get all for particular child
    public Query getAll(String child) {
        return ref.child(child);
    }

    // Get user by username
    public Query getUser(String username) {
        return ref.child(Constants.USERS).orderByChild(Constants.USERNAME).equalTo(username);
    }

    // Get user by email
    public Query getUserByEmailAddress(String emailAddress) {
        return ref.child(Constants.USERS).orderByChild(Constants.EMAIL).equalTo(emailAddress);
    }

    //Get user by userID (key)
    public Query getUserByKey(String key) {
        return ref.child(Constants.USERS).child(key);
    }

    // Get deal by dealID (key)
    public Query getDeal(String key) {
        return ref.child(Constants.DEALS).child(key);
    }

    // Get comment by commentID (key)
    public Query getComment(String key) {
        return ref.child(Constants.COMMENTS).child(key);
    }

    // Get most upvoted deals
    public Query getBestDeals() {
        return ref.child(Constants.DEALS).orderByChild(Constants.UPVOTES);
    }

    // Get deals saved by user
    // TODO: Change argument from String to User once we are able to getCurrentUser
    public Query getSavedDeals(String userID) {
        return ref.child(Constants.USERS).child(userID).child(Constants.SAVED_DEALS);
    }

    // Get deals posted by user
    public Query getPostedDeals(User user) {
        return ref.child(Constants.DEALS).orderByChild(Constants.DEAL_POSTED_BY).equalTo(user.getUsername());
    }

    // Get comments posted by user
    public Query getComments(User user) {
        return ref.child(Constants.DEALS).orderByChild(Constants.COMMENTS).equalTo(user.getUsername());
    }

    // Get comments for a deal
    public Query getComments(Deal deal) {
        return ref.child(Constants.DEALS).child(deal.getDealID()).child(Constants.COMMENTS);
    }

    // Get friends for a user
    public Query getFriends(User user) {
        return ref.child(Constants.USERS).child(user.getUsername()).child(Constants.FRIENDS);
    }

    // WRITE METHODS

    /**
     * Writes a user to the database
     *
     * @param user User object to be written
     * @return userID ID that was generated by the database
     */
    public String writeUser(User user) {
        String newUserKey = ref.child(Constants.USERS).push().getKey();
        user.setUserID(newUserKey);
        assert newUserKey != null;
        ref.child(Constants.USERS).child(newUserKey).setValue(user);
        return newUserKey;
    }

    /**
     * Write a new deal to the database
     *
     * @param deal Deal object to be written
     * @return dealID ID that was generated by the database
     */
    public String writeDeal(Deal deal) {
        String key = ref.child(Constants.DEALS).push().getKey();
        deal.setDealID(key);
        assert key != null;
        ref.child(Constants.DEALS).child(key).setValue(deal);
        return key;
    }

    /**
     * Write a new comment to the database
     *
     * @param comment Comment object to be written
     * @return commentID ID that was generated by the database
     */
    public String writeComment(Comment comment, String dealID) {
        String key = ref.child(dealID).child(Constants.COMMENTS).push().getKey();
        comment.setCommentID(key);
        assert key != null;
        ref.child(dealID).child(Constants.COMMENTS).child(key).setValue(comment);
        return key;
    }

    /**
     * Write a saved deal to the database
     *
     * @param userID ID of the user saving the deal
     * @param deal   the deal being saved
     */
    public void writeSavedDeal(String userID, Deal deal) {
        ref.child(Constants.USERS).child(userID).child(Constants.SAVED_DEALS).push().setValue(deal.getDealID());
    }

    /**
     * Write a FCM token to User in database
     *
     * @param userID ID of the user
     * @param fcmToken the token to be written
     */
    public void writeFCMTokenToUser(String userID, String fcmToken) {
        ref.child(Constants.USERS).child(userID).child(Constants.FCM_TOKEN).setValue(fcmToken);
    }

    /**
     * Write a FCM token to database
     *
     * @param token the token to be written
     */
    public void writeFCMToken(String token) {
        ref.child(Constants.FCM_TOKENS).push().setValue(token);
    }

    /**
     * Write a upvote to the database
     *
     * @param dealId ID of the deal being upvoted
     */
    public void upVoteDeal(String dealId) {
        ref.child(Constants.DEALS).child(dealId).child(Constants.UPVOTES).setValue(ServerValue.increment(1));
    }


    /**
     * Write a downvote to the database
     *
     * @param dealId ID of the deal being downvoted
     */
    public void downVoteDeal(String dealId) {
        ref.child(Constants.DEALS).child(dealId).child(Constants.DOWNVOTES).setValue(ServerValue.increment(1));
    }

    // DELETE METHODS

    /**
     * Delete a saved deal from user's saved deals
     *
     * @param dealID ID of the deal to be deleted
     */
    public void deleteSavedDeal(String userID, String dealID) {
        ref.child(Constants.USERS).child(userID).child(Constants.SAVED_DEALS).child(dealID).removeValue();
    }

}