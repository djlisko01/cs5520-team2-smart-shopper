package com.example.smartshopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;
import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.recyclerViews.DealAdapter;
import com.example.smartshopper.utilities.NavigationDrawer;
import com.google.android.material.navigation.NavigationView;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    RecyclerView rv_dealsRecyclerView;
    PlatformHelpers platformHelpers;
    DealAdapter adapter;
    NavigationDrawer navigationDrawer;
    LocalStorage localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localStorage = new LocalStorage(this);

        // Instantiate objects
        platformHelpers = new PlatformHelpers(this);
        adapter = new DealAdapter(this);

        // https://www.geeksforgeeks.org/navigation-drawer-in-android/
        // drawer layout instance to toggle the menu icon to open drawer and back button to close drawer
        drawerLayout = findViewById(R.id.home_page_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        navigationDrawer = new NavigationDrawer(this, drawerLayout, actionBarDrawerToggle);
        // to make the Navigation drawer icon always appear on the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setNavigationViewListener();

        // Recycler View setup
        rv_dealsRecyclerView = findViewById(R.id.rv_dealsRecyclerView);
        rv_dealsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_dealsRecyclerView.setAdapter(adapter);

        // Get deals from firebase:
        platformHelpers.getDealsAndUpdateMainRV(adapter, null);

        // Setup Search Listener
        setSearchListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        platformHelpers.getDealsAndUpdateMainRV(adapter, null);
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

    private void setSearchListener() {
        SearchView searchView = findViewById(R.id.sv_searchBar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                platformHelpers.getDealsAndUpdateMainRV(adapter, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newQuery) {
                platformHelpers.getDealsAndUpdateMainRV(adapter, newQuery);
                return false;
            }
        });
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



}