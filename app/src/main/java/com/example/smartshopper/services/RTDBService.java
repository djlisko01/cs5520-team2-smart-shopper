package com.example.smartshopper.services;

import androidx.annotation.NonNull;

import com.example.smartshopper.common.Constants;
import com.example.smartshopper.projectModels.Deal;
import com.example.smartshopper.projectModels.User;
import com.example.smartshopper.responseInterfaces.BoolInterface;
import com.example.smartshopper.responseInterfaces.ObjectInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RTDBService {
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public RTDBService() {
    }

    // Define Queries here
    public Query getAll(String child) {
        return database.getReference().child(child);
    }
    public Query getUser(String username) {
        return database.getReference().child(Constants.USERS).orderByChild(Constants.USERNAME).equalTo(username);
    }


    public Query getDeal(String dealID) {
        return null;
    }

    public Query getSavedDeals(User user) {
        return null;
    }

    public Query getPostedDeals(User user) {
        return null;
    }

    public Query getComments(User user) {
        return null;
    }

    public Query getFriends(User user) {
        return null;
    }

    public void writeUser(User user) {
        database.getReference().child(Constants.USERS).child(user.getUsername()).setValue(user);
    }

    public void writeDeal(Deal deal) {

    }

    public Query getDealsBySearch(String search) {
        return null;
    }


    // Use this to determine whether item exists (bool) for particular query
    public void getStatusResponseFromQuery(Query query, BoolInterface boolInterface) {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    boolInterface.onCallback(true);
                } else {
                    boolInterface.onCallback(false);
                }
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

}