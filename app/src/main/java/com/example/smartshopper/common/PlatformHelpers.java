package com.example.smartshopper.common;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.example.smartshopper.models.ActivityTimestamp;
import com.example.smartshopper.models.Comment;
import com.example.smartshopper.models.Deal;
import com.example.smartshopper.models.DealDistance;
import com.example.smartshopper.models.User;
import com.example.smartshopper.recyclerViews.CommentsAdapter;
import com.example.smartshopper.recyclerViews.DealAdapter;
import com.example.smartshopper.recyclerViews.ProfileAdapter;
import com.example.smartshopper.responseInterfaces.BoolInterface;
import com.example.smartshopper.responseInterfaces.CommentInterface;
import com.example.smartshopper.responseInterfaces.DealInterface;
import com.example.smartshopper.responseInterfaces.IntegerInterface;
import com.example.smartshopper.responseInterfaces.ListInterface;
import com.example.smartshopper.responseInterfaces.LocationInterface;
import com.example.smartshopper.responseInterfaces.StringInterface;
import com.example.smartshopper.responseInterfaces.UserInterface;
import com.example.smartshopper.services.RTDBService;
import com.example.smartshopper.utilities.LocalStorage;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import okhttp3.internal.Internal;

public class PlatformHelpers {
    private final RTDBService rtdbDatabase;
    private LocalStorage localStorage;
    private Context context;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private static final String API_KEY = "key=AAAAK0dsXYI:APA91bEA8XruUT0Lyd6WCdmnrae9tsppGI3Rs0_sG6iJMm5EfCG9nMCIPcuzdGcdC8BzFxdXBQ7mpKt_r-g2IQRH96d348MH3oHaxDFK0SjYMabmTbA8ieMDWoVU-Cbie6PqvVlK2pTm";

    public PlatformHelpers(Context context) {
        this.rtdbDatabase = new RTDBService();
        this.context = context;
        this.localStorage = new LocalStorage(context);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    // Get logged in user
    public String getCurrentUser() {
        return localStorage.getCurrentUser();
    }

    // Get logged in user
    public String getCurrentUserID() {
        return localStorage.getCurrentUserID();
    }

//    // Set logged in user
//    public void setCurrentUser(String username) {
//        // TODO: Implement
////        this.currentUser = username;
//
//    }

    public void getUserImg(String userID, StringInterface stringInterface) {
        Query query = rtdbDatabase.getUserByKey(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stringInterface.onCallback(snapshot.child("profileImgUrl").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void getUserByUsername (String username, StringInterface stringInterface){
        Query query = rtdbDatabase.getUser(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    stringInterface.onCallback(child.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

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

    public void getSavedDealsAndUpdateRV(DealAdapter adapter, TextView noSavedDeals, View loadingAnimation) {
        if (localStorage.getCurrentUserID() == "") {
            loadingAnimation.setVisibility(View.GONE);
            noSavedDeals.setVisibility(View.VISIBLE);
        } else {
            Query query = rtdbDatabase.getSavedDeals(localStorage.getCurrentUserID());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (loadingAnimation.getVisibility() != View.GONE) {
                        loadingAnimation.setVisibility(View.GONE);
                    }
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

    public void getNumUpVotesAndUpdateDeal(String dealID, IntegerInterface integerInterface) {
        Query query = rtdbDatabase.getNumUpVotes(dealID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                integerInterface.onCallback(snapshot.getValue(Integer.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getNumDownVotesAndUpdateDeal(String dealID, IntegerInterface integerInterface) {
        Query query = rtdbDatabase.getNumDownVotes(dealID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                integerInterface.onCallback(snapshot.getValue(Integer.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void checkIfUserDownVotedDeal(String dealID, String userID, BoolInterface boolInterface) {
        Query query = rtdbDatabase.getDeal(dealID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(Constants.USERS_DOWN_VOTED)) {
                    for (DataSnapshot user : snapshot.child(Constants.USERS_DOWN_VOTED).getChildren()) {
                        if (Objects.equals(user.getKey(), userID)) {
                            boolInterface.onCallback(true);
                            return;
                        }
                    }
                }
                boolInterface.onCallback(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DB_ERROR", error.getMessage());
            }
        });
    }

    public void checkIfUserUpVotedDeal(String dealID, String userID, BoolInterface boolInterface) {
        Query query = rtdbDatabase.getDeal(dealID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(Constants.USERS_UP_VOTED)) {
                    for (DataSnapshot user : snapshot.child(Constants.USERS_UP_VOTED).getChildren()) {
                        if (Objects.equals(user.getKey(), userID)) {
                            boolInterface.onCallback(true);
                            return;
                        }
                    }
                }
                boolInterface.onCallback(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DB_ERROR", error.getMessage());
            }
        });
    }

    public void upVoteDeal(String dealID, String userID, String username) {
        if (userID.equals("")) {
            Toast.makeText(context, "Please sign in to vote", Toast.LENGTH_SHORT).show();
            return;
        }
        checkIfUserUpVotedDeal(dealID, userID, upVotedAlready -> {
            rtdbDatabase.upVoteDeal(dealID, userID, username, upVotedAlready);
        });
    }

    public void downVoteDeal(String dealID, String userID, String username) {
        if (userID.equals("")) {
            Toast.makeText(context, "Please sign in to vote", Toast.LENGTH_SHORT).show();
            return;
        }
        checkIfUserDownVotedDeal(dealID, userID, downVotedAlready -> {
            rtdbDatabase.downVoteDeal(dealID, userID, username, downVotedAlready);
        });
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
                    List<Comment> responses = comment.RepliesMapToList();
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

    public void getDealsAndUpdateMainRV(DealAdapter adapter, String search, Location userLocation, View loadingAnimation) {
        //TODO case switch queryEnum to get the correct query from FireBase
        Query query = rtdbDatabase.getBestDeals();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (loadingAnimation.getVisibility() != View.GONE) {
                    loadingAnimation.setVisibility(View.GONE);
                }
                List<Deal> deals = new ArrayList<>();
                List<DealDistance> dealsByDistance = new ArrayList<>(); // Only used for location sorting

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
                    // location
                    else if (userLocation != null) {
                        if (deal.getLatitude() != null && deal.getLongitude() != null) {
                            assert deal != null;
                            Location dealLocation = new Location("deal");
                            dealLocation.setLatitude(deal.getLatitude());
                            dealLocation.setLongitude(deal.getLongitude());
                            float distanceBetweenUserAndDeal = userLocation.distanceTo(dealLocation);
                            DealDistance tempDeal = new DealDistance(distanceBetweenUserAndDeal, deal);
                            dealsByDistance.add(tempDeal);
                        } else {
                            // if deal has no location, make it INFINITY meters
                            dealsByDistance.add(new DealDistance(Float.POSITIVE_INFINITY, deal));
                        }
                    } // If no search term is provided, add all deals
                    else {
                        deals.add(deal);
                    }
                }

                // If user is sorting by distance
                if (!dealsByDistance.isEmpty()) {
                    Collections.sort(dealsByDistance);
                    for (DealDistance dealDistance : dealsByDistance) {
                        deals.add(dealDistance.getDeal());
                    }
                } else {
                    Collections.reverse(deals);
                }

                adapter.updateData(deals);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    public void getDealAddedAndUpdateRv(String userID, ProfileAdapter adapter) {
//        Query query = rtdbDatabase.getPostedDeals(userID);
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<String> deals = new ArrayList<>();
//
//                for (DataSnapshot child : snapshot.getChildren()) {
//                    Deal deal = child.getValue(Deal.class);
//                    assert deal != null;
//                    deals.add(deal);
//                }
//                adapter.updateTitle(deals);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }

    public void getActivities(String username, ProfileAdapter adapter, TextView noActivities) {
        Query query = rtdbDatabase.getAllDeals();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ActivityTimestamp> activities = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    Deal deal = child.getValue(Deal.class);
                    assert deal != null;

                    if(deal.getUserUUID().equals(localStorage.getCurrentUserID())){
                        activities.add(new ActivityTimestamp("You posted "+ deal.getTitle(), deal.getTimePosted()));
                    }

                    if(deal.getComments() != null){
                        for (Map.Entry<String, Comment> set :
                                deal.getComments().entrySet()){
                            if(set.getValue().getAuthor().getUsername().equals(localStorage.getCurrentUser())){
                                activities.add(new ActivityTimestamp("You commented on " + deal.getTitle(),set.getValue().getTimePosted()));
                            }
                        }

                    }
                }
                if (!activities.isEmpty()) {
                    noActivities.setVisibility(View.GONE);
                }
                Collections.sort(activities);
                Collections.reverse(activities);
                adapter.updateTitle(activities);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**
     * Loads picture using Glide Library
     *
     * @param context    view you are loading into
     * @param imgUri     string of the URI of the image
     * @param view       ImageView you are trying to load the picture into
     * @param defaultImg default image if there is an error (should be a local asset)
     */
    public static void loadImg(Context context, String imgUri, ImageView view, int defaultImg) {
        CircularProgressDrawable loading = new CircularProgressDrawable(context);
        loading.setStyle(CircularProgressDrawable.LARGE);
        loading.setStrokeWidth(3);
        loading.start();
        if (imgUri != null && !imgUri.isEmpty()) {
            Glide.with(context).load(imgUri).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(loading).into(view);
        } else if (imgUri == null || imgUri.isEmpty()) {
            Glide.with(context).load(defaultImg).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(loading).into(view);
        }
    }

    // NOTIFICATIONS

    public void createNotifChannel() {
        CharSequence name = "Saved Deal";
        String description = "Notification channel for saved deal activity.";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("Saved_Deal_Channel", name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public void getFcmToken(StringInterface stringInterface) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String fcmToken = task.getResult();
                stringInterface.onCallback(fcmToken);
            }
        });
    }

    public void subscribeToDeal(Deal deal) {
        FirebaseMessaging.getInstance().subscribeToTopic(deal.getDealID()).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d("SUBSCRIBE", "Failed to subscribe to deal (" + deal.getDealID() + ").");
            } else {
                Log.d("SUBSCRIBE", "Successfully subscribed to deal (" + deal.getDealID() + ").");
                Toast.makeText(context, "You'll now receive notifications for " + deal.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void unsubscribeFromDeal(Deal deal) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(deal.getDealID()).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d("SUBSCRIBE", "Failed to unsubscribe from deal (" + deal.getDealID() + ")");
            } else {
                Log.d("SUBSCRIBE", "Successfully unsubscribed from deal (" + deal.getDealID() + ").");
                Toast.makeText(context, "You'll no longer receive notifications for " + deal.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendNewCommentNotification(Deal deal, Comment comment) {
        new Thread(() -> {
            JSONObject notification = new JSONObject();
            JSONObject payload = new JSONObject();

            try {
                notification.put("title", "New Comment on " + deal.getTitle());
                notification.put("body", comment.getAuthor().getUsername() + " commented: " + comment.getText());

                payload.put("to", "/topics/" + deal.getDealID());
                payload.put("priority", "high");
                payload.put("notification", notification);
            } catch (Exception e) {
                Log.e("NOTIFICATION", e.getMessage());
            }

            postToHTTPConnection(payload);
        }).start();
    }

    private void postToHTTPConnection(JSONObject payload) {
        try {
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", API_KEY);
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(payload.toString().getBytes());
            outputStream.close();

            // Read FCM response.
            InputStream inputStream = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public void getUserRank(ListInterface listInterface){
        Query allDeals = rtdbDatabase.getAllDeals();
        allDeals.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Integer> dealPostCounts = new HashMap<>();
                for (DataSnapshot dealSnapshot : snapshot.getChildren()) {
                    Deal deal = dealSnapshot.getValue(Deal.class);
                    Integer postCount = 0;
                    assert deal != null;
                    String userID = deal.getUserUUID();
                    if (userID.length() <= 0){
                        continue;
                    }
                    if (dealPostCounts.containsKey(userID)) {
                        postCount = dealPostCounts.get(userID);
                        postCount += 1;
                    }
                    dealPostCounts.put(userID, postCount);
                }

                ArrayList<String> sortedUsers = sortPostTotal(dealPostCounts);
                listInterface.onCallback(sortedUsers);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Helper for sorting user total posted deals
    private static ArrayList<String> sortPostTotal(HashMap<String, Integer> dealPostCounts){
        ArrayList<String> sortedUsers = new ArrayList<>();
        ArrayList <Integer> list = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : dealPostCounts.entrySet()) {
            list.add(entry.getValue());
        }

        list.sort((o1, o2) -> o2.compareTo(o1));

        for (Integer count : list){
            for (Map.Entry<String, Integer> entry: dealPostCounts.entrySet()){
                if (entry.getValue().equals(count)){
                    sortedUsers.add(entry.getKey());
                }
            }
        }
        return sortedUsers;
    }
    // Location
    public void getCurrentLocation(LocationInterface locationInterface) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null).addOnSuccessListener(location -> {
                locationInterface.onCallback(location);
            });
        }
    }
}