package com.example.smartshopper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.smartshopper.utilities.LocalStorage;
import com.example.smartshopper.utilities.NavigationDrawer;
import com.google.android.material.internal.NavigationMenuItemView;
import com.google.android.material.navigation.NavigationView;
import java.util.Objects;

public class MenuActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationDrawer navigationDrawer;
    LocalStorage localStorage;
    NavigationView navigationView;
    NavigationMenuItemView loginMenuItem;

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        localStorage = new LocalStorage(this);
        loginMenuItem = findViewById(R.id.nav_logout);
        if (loginMenuItem != null && localStorage != null && localStorage.userIsLoggedIn()) {
            loginMenuItem.setTitle("Sign Out");
        }
        else if (loginMenuItem != null && localStorage != null && !localStorage.userIsLoggedIn()) {
            loginMenuItem.setTitle("Sign In");
        }
        // https://www.geeksforgeeks.org/navigation-drawer-in-android/
        // drawer layout instance to toggle the menu icon to open drawer and back button to close drawer
        drawerLayout = findViewById(R.id.home_page_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        navigationDrawer = new NavigationDrawer(this, drawerLayout, actionBarDrawerToggle);
        // to make the Navigation drawer icon always appear on the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.navigation_view);
        setNavigationViewListener();
        View navHeader = navigationView.getHeaderView(0);
        if (localStorage.userIsLoggedIn()) {
            TextView greetingText = navHeader.findViewById(R.id.tv_greeting);
            TextView rankText = navHeader.findViewById(R.id.tv_postingRank);
            greetingText.setText("Hi " + localStorage.getCurrentUser() + "!");
            rankText.setText("Posting Rank: " + localStorage.getUserRank());
        } else {
            navHeader.setVisibility(View.GONE);
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_account).setVisible(false);
            nav_Menu.findItem(R.id.nav_savedDeals).setVisible(false);
        }
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item){
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void setNavigationViewListener() {
        navigationView.setNavigationItemSelectedListener(navigationDrawer);
    }

    @SuppressLint("RestrictedApi")
    public void sendToLoginActivity(MenuItem item) {
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        if (localStorage != null && !localStorage.userIsLoggedIn()) {
            startActivity(loginIntent);
        }
        else {
            AlertDialog signOutAlert = new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to sign out?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dialog.dismiss();
                        if (loginMenuItem != null) {
                            loginMenuItem.setTitle("Sign In");
                        }
                        localStorage.signOut();
                        Intent logoutIntent = new Intent(getApplicationContext(), MainActivity.class);
                        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(logoutIntent);
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        }
    }
}
