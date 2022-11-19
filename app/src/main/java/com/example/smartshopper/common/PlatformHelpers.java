package com.example.smartshopper.common;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.smartshopper.models.Comment;
import com.example.smartshopper.models.Deal;
import com.example.smartshopper.models.User;
import com.example.smartshopper.recyclerViews.CommentsAdapter;
import com.example.smartshopper.recyclerViews.DealAdapter;
import com.example.smartshopper.responseInterfaces.CommentInterface;
import com.example.smartshopper.responseInterfaces.DealInterface;
import com.example.smartshopper.responseInterfaces.ListInterface;
import com.example.smartshopper.responseInterfaces.UserInterface;
import com.example.smartshopper.services.RTDBService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PlatformHelpers {
    private final RTDBService rtdbDatabase;
    private final Context context;

    public PlatformHelpers(Context context) {
        this.rtdbDatabase = new RTDBService();
        this.context = context;
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


    public void getDealsBySearch(String search, ListInterface listInterface) {
        Query query = rtdbDatabase.getDealsBySearch(search);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Deal> deals = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    deals.add(child.getValue(Deal.class));
                }
                listInterface.onCallback(deals);
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
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Comment> comments = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    Comment comment = child.getValue(Comment.class);
                    assert comment != null;
                    comment.setCommentID(child.getKey());
                    comments.add(comment);
                }
                adapter.updateComments(comments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getDealsAndUpdateMainRV(DealAdapter adapter) {
        //TODO case switch queryEnum to get the correct query from FireBase
        Query query = rtdbDatabase.getBestDeals();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Deal> deals = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Deal deal = child.getValue(Deal.class);  // I think this causes the issue
                    assert deal != null;
                    deal.setDealID(child.getKey());
                    deals.add(deal);
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
}
