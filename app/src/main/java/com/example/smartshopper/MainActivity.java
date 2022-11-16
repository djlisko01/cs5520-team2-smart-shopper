package com.example.smartshopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.recyclerViews.DealAdapter;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    PlatformHelpers platformHelpers;
    public RecyclerView rv_dealsRecyclerView;
    DealAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate platforhelpers
        platformHelpers = new PlatformHelpers(this);


        // https://www.geeksforgeeks.org/navigation-drawer-in-android/
        // drawer layout instance to toggle the menu icon to open drawer and back button to close drawer
        drawerLayout = findViewById(R.id.home_page_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        // pass the Open and Close toggle for the drawer layout listener to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        // to make the Navigation drawer icon always appear on the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Get deals from firebase:
        platformHelpers.updateMainRecyclerView(this);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}