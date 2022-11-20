package com.example.smartshopper;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.smartshopper.responseInterfaces.UserInterface;
import com.example.smartshopper.services.FirebaseService;
import com.example.smartshopper.utilities.NavigationDrawer;
import com.google.android.material.internal.NavigationMenuItemView;
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
  LocalStorage localStorage;

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

  @SuppressLint("RestrictedApi")
  public void loginUser() {
    emailAddressET = findViewById(R.id.editTextEmailAddress);
    passwordET = findViewById(R.id.editTextPassword);
    String emailAddress = emailAddressET.getText().toString();
    String password = passwordET.getText().toString();
    localStorage = new LocalStorage(this);

    firebaseService.checkEmail(emailAddress, password, userInterface -> {
      if (userInterface != null) {
        try {
          Log.v("userInterface", userInterface.toString());
          localStorage.setUser(userInterface.toString());
          NavigationMenuItemView signinButton = findViewById(R.id.nav_logout);
          signinButton.setTitle("Sign Out");
          Toast.makeText(getApplicationContext(), "Successful Login", Toast.LENGTH_LONG).show();
        } catch (Exception error) {
          Log.v("error", error.toString());
        }

      } else {
        Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
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
    if (localStorage.getCurrentUser() != null) {
      Intent loginIntent = new Intent(this, LoginActivity.class);
      startActivity(loginIntent);
    }
    else {
      localStorage.signOut();
    }
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
