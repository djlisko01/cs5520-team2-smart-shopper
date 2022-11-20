package com.example.smartshopper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.smartshopper.services.RTDBService;

public class LocalStorage {
  private String currentUser;
  private RTDBService database;
  private Context context;

  public String getCurrentUser() {
    return context.getSharedPreferences("UserInfo", 0).getString("username", "");
  }

  public LocalStorage(Context context) {
    SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
    this.currentUser = settings.getString("username", "");
    this.database = new RTDBService();
    this.context = context;
  }

  public void setUser(String username) {
    try {
      this.currentUser = username;
      SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
      SharedPreferences.Editor editor = settings.edit();
      editor.putString("username", username);
      editor.commit();
    } catch (Exception error) {
      Log.v("error", error.toString());
    }
  }
  public void signOut() {
    SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.clear();
    editor.commit();
  }
}
