package com.example.smartshopper;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.recyclerViews.DealAdapter;

public class SavedDealsActivity extends MenuActivity {
    RecyclerView rv_savedDeals;
    DealAdapter adapter;
    PlatformHelpers platformHelpers;
    TextView tv_noSavedDeals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_deals);
        platformHelpers = new PlatformHelpers(this);

        tv_noSavedDeals = findViewById(R.id.tv_noSavedDeals);

        // RecyclerView
        adapter = new DealAdapter(this);
        rv_savedDeals = findViewById(R.id.rv_savedDeals);
        rv_savedDeals.setLayoutManager(new LinearLayoutManager(this));
        rv_savedDeals.setAdapter(adapter);
        platformHelpers.getSavedDealsAndUpdateRV(adapter, tv_noSavedDeals);
    }
}