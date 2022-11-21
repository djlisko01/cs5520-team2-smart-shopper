package com.example.smartshopper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.example.smartshopper.models.User;

public class LocalStorage {
  private Context context;

  public String getCurrentUser() {
    return context.getSharedPreferences("UserInfo", 0).getString("username", "");
  }

  public String getCurrentUserID() {
    return context.getSharedPreferences("UserInfo", 0).getString("userID", "");
  }

  public String getCurrentUserEmail() {
    return context.getSharedPreferences("UserInfo", 0).getString("userEmail", "");
  }

  public LocalStorage(Context context) {
    SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
    this.context = context;
  }

  public void setUser(User user) {
    SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putString("username", user.getUsername());
    editor.putString("userEmail", user.getEmail());
    editor.putString("userID", user.getUserID());
    editor.commit();
  }
  public void signOut() {
    SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.clear();
    editor.commit();
  }
}
