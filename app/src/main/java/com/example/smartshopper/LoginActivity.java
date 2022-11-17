package com.example.smartshopper;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
  DrawerLayout drawerLayout;
  ActionBarDrawerToggle actionBarDrawerToggle;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    // https://www.geeksforgeeks.org/navigation-drawer-in-android/
    // drawer layout instance to toggle the menu icon to open drawer and back button to close drawer
    drawerLayout = findViewById(R.id.home_page_drawer_layout);
    actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
    // pass the Open and Close toggle for the drawer layout listener to toggle the button
    drawerLayout.addDrawerListener(actionBarDrawerToggle);
    actionBarDrawerToggle.syncState();
    // to make the Navigation drawer icon always appear on the action bar
    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
  }

  public void sendToForgotPasswordActivity(View view) {
    Intent forgotPasswordIntent = new Intent(this, ForgotPasswordActivity.class);
    startActivity(forgotPasswordIntent);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  public void sendToLoginActivity(MenuItem item) {
    Intent loginIntent = new Intent(this, LoginActivity.class);
    startActivity(loginIntent);
  }
}
