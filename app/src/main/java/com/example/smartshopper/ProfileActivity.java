package com.example.smartshopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.smartshopper.utilities.NavigationDrawer;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationDrawer navigationDrawer;
    LocalStorage localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localStorage = new LocalStorage(this);
        setContentView(R.layout.activity_profile);
        // https://www.geeksforgeeks.org/navigation-drawer-in-android/
        // drawer layout instance to toggle the menu icon to open drawer and back button to close drawer
        drawerLayout = findViewById(R.id.home_page_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        navigationDrawer = new NavigationDrawer(this, drawerLayout, actionBarDrawerToggle);
        // to make the Navigation drawer icon always appear on the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setNavigationViewListener();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
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