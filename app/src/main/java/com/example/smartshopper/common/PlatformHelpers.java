package com.example.smartshopper.common;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.example.smartshopper.models.Comment;
import com.example.smartshopper.models.Deal;
import com.example.smartshopper.models.User;
import com.example.smartshopper.recyclerViews.CommentsAdapter;
import com.example.smartshopper.recyclerViews.DealAdapter;
import com.example.smartshopper.recyclerViews.ProfileAdapter;
import com.example.smartshopper.responseInterfaces.BoolInterface;
import com.example.smartshopper.responseInterfaces.CommentInterface;
import com.example.smartshopper.responseInterfaces.DealInterface;
import com.example.smartshopper.responseInterfaces.StringInterface;
import com.example.smartshopper.responseInterfaces.UserInterface;
import com.example.smartshopper.services.RTDBService;
import com.example.smartshopper.utilities.LocalStorage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PlatformHelpers {
    private final RTDBService rtdbDatabase;
    private LocalStorage localStorage;

    public PlatformHelpers(Context context) {
        this.rtdbDatabase = new RTDBService();
        this.localStorage = new LocalStorage(context);
    }

    // Get logged in user
    public String getCurrentUser() {
        return localStorage.getCurrentUser();
    }

//    // Set logged in user
//    public void setCurrentUser(String username) {
//        // TODO: Implement
////        this.currentUser = username;
//
//    }

    // Get user by id
    public void getUserByUUID(String userUUID, UserInterface userInterface) {
        Query query = rtdbDatabase.getUserByKey(userUUID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userInterface.onCallback(snapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // get deal by id (key)
    public void getDealByKey(String key, DealInterface dealInterface) {
        Query query = rtdbDatabase.getDeal(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dealInterface.onCallback(snapshot.getValue(Deal.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // get comment by id (key)
    public void getCommentByKey(String key, CommentInterface commentInterface) {
        Query query = rtdbDatabase.getComment(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentInterface.onCallback(snapshot.getValue(Comment.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // Saved Deals methods
    public void saveDeal(Deal deal) {
        rtdbDatabase.writeSavedDeal(localStorage.getCurrentUserID(), deal);
    }

    public void deleteSavedDeal(String savedDealKey) {
        rtdbDatabase.deleteSavedDeal(localStorage.getCurrentUserID(), savedDealKey);
    }

    public void checkSavedDealExists(Deal deal, BoolInterface boolInterface) {
        Query query = rtdbDatabase.getSavedDeals(localStorage.getCurrentUserID()).orderByValue().equalTo(deal.getDealID());
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

    public void getSavedDealKey(String dealID, StringInterface stringInterface) {
        Query query = rtdbDatabase.getSavedDeals(localStorage.getCurrentUserID());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.getValue(String.class).equals(dealID)) {
                        stringInterface.onCallback(child.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getSavedDealsAndUpdateRV(DealAdapter adapter, TextView noSavedDeals) {
        if (localStorage.getCurrentUserID() == "") {
            noSavedDeals.setVisibility(View.VISIBLE);
        } else {
            Query query = rtdbDatabase.getSavedDeals(localStorage.getCurrentUserID());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Deal> savedDeals = new ArrayList<>();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        getDealByKey(child.getValue(String.class), deal -> {
                            assert deal != null;
                            deal.setDealID(child.getValue(String.class));
                            savedDeals.add(deal);
                            if (savedDeals.size() == snapshot.getChildrenCount()) {
                                noSavedDeals.setVisibility(View.GONE); // remove the noDeals text
                                adapter.updateData(savedDeals);
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("DB_ERROR", error.getMessage());
                }
            });
        }
    }

    public void upVoteDeal(String dealID) {
        rtdbDatabase.upVoteDeal(dealID);
    }

    public void downVoteDeal(String dealID) {
        rtdbDatabase.downVoteDeal(dealID);
    }

    public void getCommentsAndUpdateRv(Deal deal, CommentsAdapter adapter) {
        Query query = rtdbDatabase.getComments(deal);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Comment> comments = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    Comment comment = child.getValue(Comment.class);
                    assert comment != null;
                    // Converts nested responses to a list.
                    List <Comment> responses = comment.RepliesMapToList();
                    comment.setListReplies(responses);
                    comments.add(comment);
                }
                Collections.sort(comments);
                Collections.reverse(comments);
                adapter.updateComments(comments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getDealsAndUpdateMainRV(DealAdapter adapter, Location location, String search) {
        //TODO case switch queryEnum to get the correct query from FireBase
        Query query = rtdbDatabase.getBestDeals();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Deal> deals = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Deal deal = child.getValue(Deal.class);
                    assert deal != null;
                    // Filter deals by search query
                    if (search != null) {
                        // convert deal title to list of strings
                        for (String deal_term : deal.getTitle().split(" ")) {
                            for (String search_term : search.split(" ")) {
                                if (deal_term.toLowerCase(Locale.ROOT).startsWith(search_term.toLowerCase(Locale.ROOT)) && !deals.contains(deal)) {
                                    deals.add(deal);
                                    break;
                                }
                            }
                        }

                    }
                    else if (location != null){
                        // TODO: this check for null needs to be improved.
                        // it can't just be checking 0.0

                        if (deal.getLatitude() == null || deal.getLongitude() == null) {
                            Log.v("is nan", "true");
                        }
                        else {
                            Log.v("deal.getLatitude()", deal.getLatitude().toString());
                            Location dealLocation = new Location("deal");
                            dealLocation.setLatitude(new Double(deal.getLatitude()));
                            dealLocation.setLongitude(new Double(deal.getLongitude()));
                            Log.v("dealLocation 2", dealLocation.getProvider());
                            float distance = location.distanceTo(dealLocation);
                            if ( distance > (float)10 ) {
                                deals.add(deal);
                            }
                        }


                    }
                    // If no search term is provided, add all deals
                    else {
                        Log.v("adding all", "true");
                        deals.add(deal);
                    }
                }
                Collections.reverse(deals);
                adapter.updateData(deals);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getDealAddedAndUpdateRv(String userID, ProfileAdapter adapter) {
        Query query = rtdbDatabase.getTitle(userID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Deal> deals = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    Deal deal = child.getValue(Deal.class);
                    assert deal != null;
                    deals.add(deal);
                }
                adapter.updateTitle(deals);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /**
     * Loads picture using Piccasso Library
     *
     * @param context    view you are loading into
     * @param imgUri     string of the URI of the image
     * @param view       ImageView you are trying to load the picture into
     * @param defaultImg default image if there is an error (should be a local asset)
     */
    public static void loadPicassoImg(Context context, String imgUri, ImageView view, int defaultImg) {
        Picasso picasso = new Picasso.Builder(context).build();
        if (imgUri != null && !imgUri.isEmpty()) {
            picasso.load(imgUri).placeholder(defaultImg).error(defaultImg).into(view);
        } else {
            picasso.load(defaultImg).placeholder(defaultImg).error(defaultImg).into(view);
        }
    }

    public void getFcmToken(StringInterface stringInterface) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String fcmToken = task.getResult();
                stringInterface.onCallback(fcmToken);
            }
        });
    }

    public void validateCredentials(String finalEmailAddress, String passwordInput, UserInterface userInterface) {
        Query query = rtdbDatabase.getUserByEmailAddress(finalEmailAddress);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // TODO: This needs to be locked down by one result
                    User foundUser = new User();
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        foundUser = userSnapshot.getValue(User.class);
                    }
                    assert foundUser != null;
                    if (foundUser.getPassword().equals(passwordInput)) {
                        // If FCM Token is new after logging in -- update in DB
                        User finalFoundUser = foundUser;
                        getFcmToken(response -> {
                            if (!Objects.equals(response, finalFoundUser.getFcmToken())) {
                                rtdbDatabase.writeFCMTokenToUser(finalFoundUser.getUserID(), response);
                            }
                        });

                        userInterface.onCallback(foundUser);
                    } else {
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

    public void checkEmail(String emailAddress, String password, UserInterface userInterface) {
        String finalEmailAddress = emailAddress.toLowerCase(Locale.ROOT);
        this.validateCredentials(finalEmailAddress, password, userInterface);
    }

    public void checkIfEmailExists(String emailAddress, BoolInterface boolInterface) {
        String finalEmailAddress = emailAddress.toLowerCase(Locale.ROOT);
        Query emailQuery = rtdbDatabase.getUserByEmailAddress(finalEmailAddress);
        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot emailExistsSnapshot) {
                boolInterface.onCallback(emailExistsSnapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void createAccount(String username, String emailAddress, String password, UserInterface userInterface) {
        String finalEmailAddress = emailAddress.toLowerCase(Locale.ROOT);
        Query emailQuery = rtdbDatabase.getUserByEmailAddress(finalEmailAddress);
        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot emailExistsSnapshot) {
                checkIfEmailExists(emailAddress, response -> {
                    if (response) {
                        userInterface.onCallback(null);
                    } else {
                        // Store user with FCM Token upon account creation in DB
                        getFcmToken(response1 -> {
                            User newUser = new User(username, finalEmailAddress, password, response1);
                            rtdbDatabase.writeUser(newUser);
                            userInterface.onCallback(newUser);
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}