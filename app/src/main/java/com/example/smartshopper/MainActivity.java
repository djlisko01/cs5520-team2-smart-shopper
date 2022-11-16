package com.example.smartshopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.projectModels.Deal;
import com.example.smartshopper.recyclerViews.DealAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    PlatformHelpers platformHelpers;
    RecyclerView rv_dealsRecyclerView;
//    List<Deal> deals;
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


//        // Recycler View setup
//        deals = new ArrayList<>(); // initiate an empty stickers array (begins with empty recycler view)
//        // Temporary placeholder to display cards in recyclerView
//        User tempuser = new User("megaDeals100");
//        for (int i=0; i < 10; i++) {
//            String dealNumber = String.valueOf(i);
//            Deal tempDeal = new Deal("Deal"+ dealNumber , (double) (i+10), (double) (i+3),dealNumber + "description", "Target", tempuser);
//            deals.add(tempDeal);
//        }
//
//        adapter = new DealAdapter(deals, this);
//        rv_dealsRecyclerView = findViewById(R.id.rv_dealsRecyclerView);
//        rv_dealsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        rv_dealsRecyclerView.setAdapter(adapter);



        rv_dealsRecyclerView = findViewById(R.id.rv_dealsRecyclerView);


        platformHelpers.getPopularDeals(deals -> {
            adapter = new DealAdapter(deals, this);
            adapter.updateData(deals);
            updateRecycler(adapter);
        });

    }

    public void updateRecycler(DealAdapter adapter) {
        rv_dealsRecyclerView = findViewById(R.id.rv_dealsRecyclerView);
        rv_dealsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_dealsRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}