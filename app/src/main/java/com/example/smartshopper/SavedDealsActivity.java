package com.example.smartshopper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.recyclerViews.DealAdapter;

public class SavedDealsActivity extends AppCompatActivity {
    RecyclerView rv_savedDeals;
    DealAdapter adapter;
    PlatformHelpers platformHelpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_deals);
        platformHelpers = new PlatformHelpers(this);

        // RecyclerView
        adapter = new DealAdapter(this);
        rv_savedDeals = findViewById(R.id.rv_savedDeals);
        rv_savedDeals.setLayoutManager(new LinearLayoutManager(this));
        rv_savedDeals.setAdapter(adapter);
        platformHelpers.getSavedDealsAndUpdateRV(adapter);
    }
}