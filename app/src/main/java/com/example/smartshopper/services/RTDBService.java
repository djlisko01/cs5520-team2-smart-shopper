package com.example.smartshopper.services;

import android.util.Log;
import androidx.annotation.NonNull;

import com.example.smartshopper.common.Constants;
import com.example.smartshopper.models.Comment;
import com.example.smartshopper.models.Deal;
import com.google.firebase.database.ServerValue;
import com.example.smartshopper.models.User;
import com.example.smartshopper.responseInterfaces.BoolInterface;
import com.example.smartshopper.responseInterfaces.ObjectInterface;
import com.example.smartshopper.responseInterfaces.UserInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class RTDBService {
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public RTDBService() {
    }

    // Define Queries here

    // Get all for particular
    public Query getAll(String child) {
        return database.getReference().child(child);
    }

    //Get user by username
    public Query getUser(String username) {
        return database.getReference().child(Constants.USERS).orderByChild(Constants.USERNAME).equalTo(username);
    }

    public Query getUserByEmailAddress(String emailAddress) {
        return database.getReference().child(Constants.USERS).orderByChild(Constants.EMAIL).equalTo(emailAddress);
    }

    //Get user by userID (key)
    public Query getUserByKey(String key) {
        return database.getReference().child(Constants.USERS).child(key);
    }

    // Get deal by dealID (key)
    public Query getDeal(String key) {
        return database.getReference().child(Constants.DEALS).child(key);
    }

    // Get comment by commentID (key)
    public Query getComment(String key) {
        return database.getReference().child(Constants.COMMENTS).child(key);
    }

    // Get most upvoted deals
    public Query getBestDeals() {
        return database.getReference().child(Constants.DEALS).orderByChild(Constants.UPVOTES);
    }

    // Get deals saved by user
    public Query getSavedDeals(String userID) {
        return database.getReference().child(Constants.USERS).child(userID).child(Constants.SAVED_DEALS);
    }

    // Get a specific saved deal
    public Query getSpecificSavedDeal(String userID, String dealID) {
        return database.getReference().child(Constants.USERS).child(userID).child(Constants.SAVED_DEALS).orderByValue().equalTo(dealID);
    }

    // Get deals posted by user
    public Query getPostedDeals(User user) {
        return database.getReference().child(Constants.DEALS).orderByChild(Constants.DEAL_POSTED_BY).equalTo(user.getUsername());
    }

    // Get comments posted by user
    public Query getComments(User user) {
        return database.getReference().child(Constants.DEALS).orderByChild(Constants.COMMENTS).equalTo(user.getUsername());
    }

    // Get comments for a deal
    public Query getComments(Deal deal) {
        return database.getReference().child(Constants.DEALS).child(deal.getDealID()).child(Constants.COMMENTS);
    }

    // Get friends for a user
    public Query getFriends(User user) {
        return database.getReference().child(Constants.USERS).child(user.getUsername()).child(Constants.FRIENDS);
    }

    // WRITE METHODS

    // Write a new user to the database
    public void writeUser(User user) {
        database.getReference().child(Constants.USERS).push().setValue(user);
    }

    // Write a new deal to the database
    public void writeDeal(Deal deal) {
        database.getReference().child(Constants.DEALS).push().setValue(deal);
    }

    // Write a new saved deal to the database
    public void writeSavedDeal(String userID, String dealID) {
        database.getReference().child(Constants.USERS).child(userID).child(Constants.SAVED_DEALS).push().setValue(dealID);
    }

    // Write comment to existing deal
    public void writeComment(Comment comment, String dealID) {
        database.getReference().child(Constants.DEALS).child(dealID).child(Constants.COMMENTS).push().setValue(comment);
    }

    // DELETE METHODS

    // Delete a saved deal
    public void deleteSavedDeal(String userID, String dealKey) {
        database.getReference().child(Constants.USERS).child(userID).child(Constants.SAVED_DEALS).child(dealKey).removeValue();
    }

    // Check if savedDeal exists
    public void isSaved(String userID, String dealID, BoolInterface boolInterface) {
        getStatusResponseFromQuery(getSpecificSavedDeal(userID, dealID), boolInterface);
    }

    // Use this to determine whether item exists (bool) for particular query
    public void getStatusResponseFromQuery(Query query, BoolInterface boolInterface) {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolInterface.onCallback(snapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // Use this to get a single response (object) from a query, may contain children
    public void getDataResponseFromQuery(Query query, ObjectInterface objectInterface) {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    objectInterface.onCallback(snapshot.getValue());
                } else {
                    objectInterface.onCallback(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void validateCredentials(Query query, String passwordInput, UserInterface userInterface) {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // TODO: This needs to be locked down by one result
                    User foundUser = new User();
                    for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                        foundUser = userSnapshot.getValue(User.class);
                    }
                    if (foundUser.getPassword().equals(passwordInput)) {
                        userInterface.onCallback(foundUser);
                    }
                    else {
                        userInterface.onCallback(null);
                    }
                } else {
                    userInterface.onCallback(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void upVoteDeal(String dealId) {
        database.getReference()
                .child(Constants.DEALS)
                .child(dealId)
                .child(Constants.UPVOTES)
                .setValue(ServerValue.increment(1));
    }

    public void downVoteDeal(String dealId) {
        database.getReference()
                .child(Constants.DEALS)
                .child(dealId)
                .child(Constants.DOWNVOTES)
                .setValue(ServerValue.increment(1));
    }

    public void createAccount(String username, String emailAddress, String password, UserInterface userInterface ) {
        String finalEmailAddress = emailAddress.toLowerCase(Locale.ROOT);
        Query emailQuery = getUserByEmailAddress(finalEmailAddress);
        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot emailExistsSnapshot) {
                if (emailExistsSnapshot.exists()) {
                    userInterface.onCallback(null);
                } else {
                    Query usernameQuery = getUser(username);
                    usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot usernameExistsSnapshot) {
                            if (usernameExistsSnapshot.exists()) {
                                userInterface.onCallback(null);
                            }
                            else {
                                User newUser = new User(username, finalEmailAddress, password);
                                String newUserKey = database.getReference()
                                        .child(Constants.USERS)
                                        .push()
                                        .getKey();
                                newUser.setUserID(newUserKey);
                                database.getReference()
                                        .child(Constants.USERS)
                                        .child(newUserKey)
                                        .setValue(newUser);
                                userInterface.onCallback(newUser);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}