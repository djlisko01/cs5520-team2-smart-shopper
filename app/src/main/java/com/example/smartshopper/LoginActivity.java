package com.example.smartshopper;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.smartshopper.services.FirebaseService;
import com.example.smartshopper.utilities.NavigationDrawer;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
  DrawerLayout drawerLayout;
  ActionBarDrawerToggle actionBarDrawerToggle;
  NavigationDrawer navigationDrawer;
  Button loginButton;
  FirebaseService firebaseService;
  EditText emailAddressET;
  EditText passwordET;
  Context context;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    // https://www.geeksforgeeks.org/navigation-drawer-in-android/
    // drawer layout instance to toggle the menu icon to open drawer and back button to close drawer
    drawerLayout = findViewById(R.id.home_page_drawer_layout);
    actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
    navigationDrawer = new NavigationDrawer(this, drawerLayout, actionBarDrawerToggle);
    // to make the Navigation drawer icon always appear on the action bar
    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    setNavigationViewListener();
    firebaseService = new FirebaseService(getApplicationContext());
    loginButton = findViewById(R.id.loginButton);
    loginButton.setOnClickListener(view -> {
      loginUser();
    });
  }

  public void loginUser() {
    emailAddressET = findViewById(R.id.editTextEmailAddress);
    passwordET = findViewById(R.id.editTextPassword);
    String emailAddress = emailAddressET.getText().toString();
    String password = passwordET.getText().toString();
    Toast.makeText(this, "in loginUser method", Toast.LENGTH_LONG).show();

    firebaseService.checkUserInfo(emailAddress, isValid -> {
      Toast.makeText(this, "checking user exists..", Toast.LENGTH_SHORT).show();
      if (isValid) {
        try {
        firebaseService.setUser(emailAddress);
        Toast.makeText(this, firebaseService.getCurrentUser(), Toast.LENGTH_LONG).show();
        } catch (ActivityNotFoundException e) {
          Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
      } else {
        Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show();
      }
    });
  }
  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void setNavigationViewListener() {
    NavigationView navigationView = findViewById(R.id.navigation_view);
    navigationView.setNavigationItemSelectedListener(navigationDrawer);
  }

  public void sendToLoginActivity(MenuItem item) {
    Intent loginIntent = new Intent(this, LoginActivity.class);
    startActivity(loginIntent);
  }

  public void sendToForgotPasswordActivity(View view) {
    Intent forgotPasswordIntent = new Intent(this, ForgotPasswordActivity.class);
    startActivity(forgotPasswordIntent);
  }

  public void sendToCreateAccountActivity(View view) {
    Intent createAccountIntent = new Intent(this, CreateAccountActivity.class);
    startActivity(createAccountIntent);
  }

}
