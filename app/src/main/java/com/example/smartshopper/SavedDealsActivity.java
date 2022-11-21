package com.example.smartshopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.models.Deal;
import com.example.smartshopper.recyclerViews.DealAdapter;
import com.example.smartshopper.responseInterfaces.DealInterface;
import com.example.smartshopper.responseInterfaces.ListInterface;
import com.example.smartshopper.utilities.NavigationDrawer;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SavedDealsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    SearchView sv_searchSavedDeals;
    List<Deal> filteredDeals;
    RecyclerView rv_savedDeals;
    DealAdapter adapter;
    LocalStorage localStorage;
    PlatformHelpers platformHelpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_deals);
        localStorage = new LocalStorage(this);
        platformHelpers = new PlatformHelpers(this);

        // Search
        sv_searchSavedDeals = findViewById(R.id.sv_searchSavedDeals);
        sv_searchSavedDeals.setOnQueryTextListener(this);

        // RecyclerView
        adapter = new DealAdapter(this);
        rv_savedDeals = findViewById(R.id.rv_savedDeals);
        rv_savedDeals.setLayoutManager(new LinearLayoutManager(this));
        rv_savedDeals.setAdapter(adapter);
        platformHelpers.getSavedDeals(adapter);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if (query.isEmpty()) {
            platformHelpers.getSavedDeals(adapter);
        } else {
            adapter.searchByTitle(query);
        }
        return false;
    }
}