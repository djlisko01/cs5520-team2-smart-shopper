package com.example.smartshopper.common;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;



import androidx.annotation.NonNull;

import com.example.smartshopper.LocalStorage;
import com.example.smartshopper.R;
import com.example.smartshopper.models.Comment;
import com.example.smartshopper.models.Deal;
import com.example.smartshopper.models.User;
import com.example.smartshopper.recyclerViews.CommentsAdapter;
import com.example.smartshopper.recyclerViews.DealAdapter;
import com.example.smartshopper.responseInterfaces.BoolInterface;
import com.example.smartshopper.responseInterfaces.CommentInterface;
import com.example.smartshopper.responseInterfaces.DealInterface;
import com.example.smartshopper.responseInterfaces.StringInterface;
import com.example.smartshopper.responseInterfaces.UserInterface;
import com.example.smartshopper.services.RTDBService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class PlatformHelpers {
    private final RTDBService rtdbDatabase;
    private Context context;
    private LocalStorage localStorage;

    public PlatformHelpers(Context context) {
        this.rtdbDatabase = new RTDBService();
        this.context = context;
        this.localStorage = new LocalStorage(this.context);
    }

    // Get logged in user
    public User getCurrentUser() {
        return null;
    }

    // Set logged in user
    public void setCurrentUser(String username) {
        // TODO: Implement
//        this.currentUser = username;

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

    public void saveDeal(String dealID) {
        rtdbDatabase.writeSavedDeal(localStorage.getCurrentUserID(), dealID);
    }

    public void removeDeal(String dealID) {
        rtdbDatabase.deleteSavedDeal(localStorage.getCurrentUserID(), dealID);
    }

    public void isSaved(String dealID, BoolInterface boolInterface) {
        rtdbDatabase.isSaved(localStorage.getCurrentUserID(), dealID, boolInterface);
    }

    public void getSavedDealKey(String dealID, StringInterface stringInterface) {
        Query query = rtdbDatabase.getSpecificSavedDeal(localStorage.getCurrentUserID(), dealID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ss : snapshot.getChildren()) {
                    stringInterface.onCallback(ss.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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
                    comment.setCommentID(child.getKey());
                    comments.add(comment);
                }
                Collections.reverse(comments);
                adapter.updateComments(comments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getDealsAndUpdateMainRV(DealAdapter adapter, String search) {
        //TODO case switch queryEnum to get the correct query from FireBase
        Query query = rtdbDatabase.getBestDeals();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Deal> deals = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Deal deal = child.getValue(Deal.class);
                    assert deal != null;
                    deal.setDealID(child.getKey());
                    // Filter deals by search query
                    if (search != null) {
                       // convert deal title to list of strings
                        for (String s : deal.getTitle().split(" ")) {
                            if (s.toLowerCase().startsWith(search.toLowerCase()) && !deals.contains(deal)) {
                                deals.add(deal);
                            }
                        }

                    // If no search term is provided, add all deals
                    } else {
                        deals.add(deal);
                    }
                }

                adapter.updateData(deals);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Loads picture using Piccasso Library
     *  @param context    view you are loading into
     * @param imgUri     string of the URI of the image
     * @param view       ImageView you are trying to load the picture into
     * @param defaultImg default image if there is an error (should be a local asset)
     */
    public static void loadPicassoImg(Context context, String imgUri, ImageView view, int defaultImg) {
        Picasso picasso = new Picasso.Builder(context).build();
        picasso.load(imgUri)
          .error(defaultImg) // removed .placeholder just left .error
          .into(view);
    }

    public void checkEmail(String emailAddress, String password, UserInterface userInterface) {
        String finalEmailAddress = emailAddress.toLowerCase(Locale.ROOT);
        Query singleUserQuery = rtdbDatabase.getUserByEmailAddress(finalEmailAddress);
        rtdbDatabase.validateCredentials(singleUserQuery, password, userInterface);
    }

    public void createAccount(String username, String emailAddress, String password, UserInterface userInterface) {
        rtdbDatabase.createAccount(username, emailAddress, password, userInterface);
    }

}