package com.example.smartshopper;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.recyclerViews.ProfileAdapter;
import com.example.smartshopper.responseInterfaces.StringInterface;
import com.example.smartshopper.services.RTDBService;
import com.example.smartshopper.utilities.LocalStorage;

public class ProfileActivity extends MenuActivity {
    RecyclerView rv_activity;
    ProfileAdapter adapter;
    PlatformHelpers platformHelpers;
    TextView tv_username;
    Button changeIcon;
    ImageView profilePic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        changeIcon= findViewById(R.id.editButton);

        changeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileActivity.this, ChangIconActivity.class);
                startActivity(intent);
            }
        });

        platformHelpers = new PlatformHelpers(this);
        adapter = new ProfileAdapter(this);

        // RecyclerView
        adapter = new ProfileAdapter(this);
        rv_activity= findViewById(R.id.profile_recycler_view);
        rv_activity.setLayoutManager(new LinearLayoutManager(this));
        rv_activity.setAdapter(adapter);

        LocalStorage localStorage = new LocalStorage(this);

        profilePic = findViewById(R.id.iv_user);
        platformHelpers.getUserImg(localStorage.getCurrentUserID(), new StringInterface() {
            @Override
            public void onCallback(String response) {
                PlatformHelpers.loadImg(getApplicationContext(), response, profilePic, R.drawable.ic_user);
                Log.d("IMG URL:::", response);
            }
        });




        tv_username = findViewById(R.id.tv_username);
        tv_username.setText(localStorage.getCurrentUser());

        platformHelpers.getActivities(localStorage.getCurrentUser(), adapter);


    }



}