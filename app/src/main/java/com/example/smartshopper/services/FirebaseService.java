package com.example.smartshopper.services;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.smartshopper.responseInterfaces.BoolInterface;
import com.example.smartshopper.responseInterfaces.SetStrInterface;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;

public class FirebaseService {

  public String getCurrentUser() {
    return currentUser;
  }

  private String currentUser;

  public RTDBService getDatabase() {
    return database;
  }

  private RTDBService database;
  private Context context;
  private ChildEventListener messageReceivedListener;

  public FirebaseService(Context context) {
    SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
    this.currentUser = settings.getString("username", "");
    this.database = new RTDBService();
    this.context = context;
  }

  public void setUser(String username) {
    this.currentUser = username;
    SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putString("username", username);
    editor.commit();
  }

  public void checkUserInfo(String emailAddress, BoolInterface boolInterface) {
    Query singleUserQuery = database.getUserByEmailAddress(emailAddress);
    database.getStatusResponseFromQuery(singleUserQuery, boolInterface);
  }

  public void getAllUsers(SetStrInterface strInterface) {
    Query allUsersQuery = database.getAll("users");

    allUsersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        HashSet<String> allUsers = new HashSet<>();
        if (snapshot.exists()) {

          for (DataSnapshot ds : snapshot.getChildren()) {
            allUsers.add(ds.child("username").getValue(String.class));
          }
          strInterface.onCallback(allUsers);
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {
      }
    });
  }

}
