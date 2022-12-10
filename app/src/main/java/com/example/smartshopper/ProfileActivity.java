package com.example.smartshopper;

import android.app.AlertDialog;
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

import java.util.Locale;

public class ProfileActivity extends MenuActivity {
    RecyclerView rv_activity;
    ProfileAdapter adapter;
    PlatformHelpers platformHelpers;
    TextView tv_username;
    TextView tv_userRank;
    TextView tv_noActivities;
    Button changeIcon;
    ImageView profilePic;
    ImageView rankInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        LocalStorage localStorage = new LocalStorage(this);

        changeIcon= findViewById(R.id.editButton);
        tv_userRank = findViewById(R.id.tv_userRank);
        rankInfo = findViewById(R.id.iv_rankInfo);

        changeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileActivity.this, ChangIconActivity.class);
                startActivity(intent);
            }
        });

        platformHelpers = new PlatformHelpers(this);

        adapter = new ProfileAdapter(this);
        rv_activity = findViewById(R.id.profile_recycler_view);
        rv_activity.setLayoutManager(new LinearLayoutManager(this));
        rv_activity.setAdapter(adapter);

        profilePic = findViewById(R.id.iv_user);
        platformHelpers.getUserImg(localStorage.getCurrentUserID(), new StringInterface() {
            @Override
            public void onCallback(String response) {
                PlatformHelpers.loadImg(getApplicationContext(), response, profilePic, R.drawable.ic_user);
            }
        });

        tv_userRank.setText(String.format(Locale.US, "%d", localStorage.getUserRank()));

        tv_username = findViewById(R.id.tv_username);
        tv_username.setText(localStorage.getCurrentUser());

        tv_noActivities = findViewById(R.id.tv_noActivities);

        platformHelpers.getActivities(localStorage.getCurrentUser(), adapter, tv_noActivities);
    }

    public void displayRankInfo(View view) {
        AlertDialog rankInfoDialog = new AlertDialog.Builder(this)
                .setMessage("Posting rank is determined by how many deals you have posted relative to other users." +
                        "\nRaise your rank by posting more deals!")
                .setPositiveButton("Got It", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

}