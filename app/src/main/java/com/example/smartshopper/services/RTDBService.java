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

public class RTDBService implements RTDBServiceInterface {
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public RTDBService() {
    }

    // Define Queries here
    @Override
    public Query getAll(String child) {
        return database.getReference().child(child);
    }
    @Override
    public Query getUser(String username) {
        return database.getReference().child(Constants.USERS).orderByChild(Constants.USERNAME).equalTo(username);
    }


    @Override
    public Query getDeal(String dealID) {
        return null;
    }

    @Override
    public void writeUser(User user) {
        database.getReference().child(Constants.USERS).child(user.getUsername()).setValue(user);
    }

    @Override
    public void writeDeal(Deal deal) {

    }

    @Override
    public Query getDealsBySearch(String search) {
        return null;
    }


    // Use this to determine whether item exists (bool) for particular query
    @Override
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
    @Override
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