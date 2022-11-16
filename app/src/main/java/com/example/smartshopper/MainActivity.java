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

import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.models.Deal;
import com.example.smartshopper.recyclerViews.DealAdapter;
import com.example.smartshopper.responseInterfaces.RecyclerViewInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    PlatformHelpers platformHelpers;
    public RecyclerView rv_dealsRecyclerView;
    DealAdapter adapter;
    List<Deal> deals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate platforhelpers
        platformHelpers = new PlatformHelpers(this);
        rv_dealsRecyclerView = findViewById(R.id.rv_dealsRecyclerView);


        // https://www.geeksforgeeks.org/navigation-drawer-in-android/
        // drawer layout instance to toggle the menu icon to open drawer and back button to close drawer
        drawerLayout = findViewById(R.id.home_page_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        // pass the Open and Close toggle for the drawer layout listener to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        // to make the Navigation drawer icon always appear on the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Recycler View setup
        deals = new ArrayList<>(); // initiate an empty stickers array (begins with empty recycler view)
        adapter = new DealAdapter(deals, this, this);
        rv_dealsRecyclerView = findViewById(R.id.rv_dealsRecyclerView);
        rv_dealsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_dealsRecyclerView.setAdapter(adapter);

        // Get deals from firebase:
        platformHelpers.getDealsAndUpdateMainRV(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {
        Deal dealClicked = adapter.getDeal(position);

        Intent intent = new Intent(this, DealDetails.class);
        intent.putExtra("dealItem", dealClicked);
        startActivity(intent);
    }
}