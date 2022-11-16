package com.example.smartshopper.common;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.smartshopper.MainActivity;
import com.example.smartshopper.R;
import com.example.smartshopper.projectModels.Comment;
import com.example.smartshopper.projectModels.Deal;
import com.example.smartshopper.projectModels.User;
import com.example.smartshopper.recyclerViews.DealAdapter;
import com.example.smartshopper.responseInterfaces.ListInterface;
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

    public void getPopularDeals(ListInterface listInterface) {
        Query query = rtdbDatabase.getBestDeals();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Deal> deals = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    deals.add(child.getValue(Deal.class));
                }
                // having trouble getting abstract Response Class to work here...
//                responseClass.onCallback(deals);
                listInterface.onCallback(deals);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateMainRecyclerView(Context context) {
        MainActivity mainActivity = (MainActivity) context;
        this.getPopularDeals(response -> {
            DealAdapter adapter = new DealAdapter(response, mainActivity);
//            adapter.updateData(response);
            mainActivity.rv_dealsRecyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
            mainActivity.rv_dealsRecyclerView.setAdapter(adapter);
        });
    }
}
