package com.example.smartshopper;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.recyclerViews.ProfileAdapter;
import com.example.smartshopper.utilities.LocalStorage;

public class ProfileActivity extends MenuActivity {
    RecyclerView rv_activity;
    ProfileAdapter adapter;
    PlatformHelpers platformHelpers;
    TextView tv_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        platformHelpers = new PlatformHelpers(this);
        adapter = new ProfileAdapter(this);

        // RecyclerView
        adapter = new ProfileAdapter(this);
        rv_activity= findViewById(R.id.profile_recycler_view);
        rv_activity.setLayoutManager(new LinearLayoutManager(this));
        rv_activity.setAdapter(adapter);

        LocalStorage localStorage = new LocalStorage(this);
        // Username
        tv_username = findViewById(R.id.tv_username);
        tv_username.setText(localStorage.getCurrentUser());

        platformHelpers.getDealAddedAndUpdateRv(localStorage.getCurrentUserID(), adapter);


    }
}