package com.example.smartshopper.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.smartshopper.models.User;

public class LocalStorage {
  private Context context;
  private final int DISTANCE_CODE = 0;
  private final int POPULARITY_CODE = 1;

  public Boolean userIsLoggedIn() {
    return !context.getSharedPreferences("UserInfo", 0).getString("username", "").equals("");
  }

  public String getCurrentUser() {
    return context.getSharedPreferences("UserInfo", 0).getString("username", "");
  }

  public String getCurrentUserID() {
    return context.getSharedPreferences("UserInfo", 0).getString("userID", "");
  }

  public String getCurrentUserEmail() {
    return context.getSharedPreferences("UserInfo", 0).getString("userEmail", "");
  }

  public int getSortPreference() {
    return context.getSharedPreferences("UserInfo", 0).getInt("sortPreference", DISTANCE_CODE);
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

  public void setUserRank(int userRank){
    SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putInt("userRank", userRank);
    editor.commit();
  }

  public int getUserRank(){
    return context.getSharedPreferences("UserInfo", 0)
            .getInt("userRank", 9999);
  }

  public void signOut() {
    SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.clear();
    editor.commit();
  }

  public void setSortPreference(int code) {
    if (code != DISTANCE_CODE && code != POPULARITY_CODE) {
      Log.d("SORTPREFERENCE", "Code: " + code + " is not a valid sort preference code.");
    } else {
      SharedPreferences settings = context.getSharedPreferences("UserInfo", 0);
      SharedPreferences.Editor editor = settings.edit();
      editor.putInt("sortPreference", code);
      editor.commit();
    }
  }
}
