package com.example.smartshopper.utilities;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.smartshopper.ProfileActivity;
import com.example.smartshopper.R;
import com.google.android.material.navigation.NavigationView;

public class NavigationDrawer implements NavigationView.OnNavigationItemSelectedListener  {
    Context context;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    public NavigationDrawer(Context context, DrawerLayout drawerLayout, ActionBarDrawerToggle actionBarDrawerToggle) {
        this.context = context;
        this.drawerLayout = drawerLayout;
        this.actionBarDrawerToggle = actionBarDrawerToggle;
        // pass the Open and Close toggle for the drawer layout listener to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //TODO add the other naviagtion clicks here
        if (item.getItemId() == R.id.nav_account) {
            Intent intent = new Intent(context, ProfileActivity.class);
            context.startActivity(intent);
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
