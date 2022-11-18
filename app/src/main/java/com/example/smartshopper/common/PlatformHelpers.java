package com.example.smartshopper.common;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.smartshopper.models.Comment;
import com.example.smartshopper.models.Deal;
import com.example.smartshopper.models.User;
import com.example.smartshopper.recyclerViews.DealAdapter;
import com.example.smartshopper.services.RTDBService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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

    public User getUser(String username) {
        return null;
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

    public void addComment(Comment comment) {

    }

    public void getDealsBySearch(String search) {

    }

    public void getCommentsAndUpdateRv(Deal deal){
        Query query = rtdbDatabase.getComments(deal);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Comment> comments = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()){
                    Log.v("COMMENT", child.toString());
                    String author = (String) child.child("author").getValue();
                    String text = (String) child.child("text").getValue();
                    Long timeStamp = (Long) child.child("timePosted").getValue();

                    Comment comment = new Comment(new User(author), text, timeStamp);

                    Log.v("COMMENTS", comment.getTimePosted().toString());
                    comments.add(comment);
                }

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
                    Deal deal = child.getValue(Deal.class);
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
}
