package com.example.smartshopper.common;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.smartshopper.models.Comment;
import com.example.smartshopper.models.Deal;
import com.example.smartshopper.models.User;
import com.example.smartshopper.recyclerViews.CommentsAdapter;
import com.example.smartshopper.recyclerViews.DealAdapter;
import com.example.smartshopper.responseInterfaces.UserInterface;
import com.example.smartshopper.services.RTDBService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
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

    public User getCurrentUser() {
        return null;
    }

    public void getUserByUUID(String userUUID, UserInterface userInterface) {
        Query query = rtdbDatabase.getUserByUUID(userUUID);
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

    public void setUser(String username) {

    }

    public Deal getDeal(String dealId) {
        return null;
    }

    public String getDealId(Deal deal) {
        return null;
    }

    public void addDeal(Deal deal) {

    }


    public void getDealsBySearch(String search) {

    }

    public void getCommentsAndUpdateRv(Deal deal, CommentsAdapter adapter) {
        Query query = rtdbDatabase.getComments(deal);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Comment> comments = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    String author = (String) child.child("author").child("username").getValue();
                    String text = (String) child.child("text").getValue();
                    Long timeStamp = (Long) child.child("timePosted").getValue();

                    Comment comment = new Comment(new User(author), text, timeStamp);
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
        query.addListenerForSingleValueEvent(new ValueEventListener() {
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
     *
     * @param context    view you are loading into
     * @param imgUri     string of the URI of the image
     * @param view       ImageView you are trying to load the picture into
     * @param defaultImg default image if there is an error (should be a local asset)
     */
    public static Picasso loadPicassoImg(Context context, String imgUri, ImageView view, int defaultImg) {
        Picasso picasso = new Picasso.Builder(context).build();
        picasso.load(imgUri)
                .error(defaultImg) // removed .placeholder just left .error
                .into(view);
        return picasso;
    }
}
