package com.example.smartshopper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.recyclerViews.DealAdapter;
import com.example.smartshopper.utilities.LocalStorage;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends MenuActivity {
  RecyclerView rv_dealsRecyclerView;
  View loadingAnimation;
  PlatformHelpers platformHelpers;
  DealAdapter adapter;
  LocalStorage localStorage;
  FusedLocationProviderClient fusedLocationProviderClient;
  private final static int COARSE_REQUEST_CODE = 100;
  Context context = this;
  Location currentLocation;
  ToggleButton toggleSort;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      // Instantiate objects
        localStorage = new LocalStorage(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        platformHelpers = new PlatformHelpers(this);
        adapter = new DealAdapter(this);
        loadingAnimation = findViewById(R.id.loadingAnimation);
        toggleSort = findViewById(R.id.toggle_sortByDistance);

        // Recycler View setup
        rv_dealsRecyclerView = findViewById(R.id.rv_dealsRecyclerView);
        rv_dealsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_dealsRecyclerView.setAdapter(adapter);

        // Setup Search Listener
        setSearchListener();

        // Setup button listener on add deal (+) button
        setCreateDealButtonListener();

        // Notification channel
        if (localStorage.userIsLoggedIn()) {
          platformHelpers.createNotifChannel();
        }

        platformHelpers.getDealsAndUpdateMainRV(adapter, null, null, loadingAnimation);

        // method puts MainActivity onPause()
        checkLocationPermissionAndGetLocation();
    }

    private void setSearchListener() {
        SearchView searchView = findViewById(R.id.sv_searchBar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty()) {
                    toggleSort.setVisibility(View.VISIBLE);
                } else {
                    toggleSort.setVisibility(View.GONE);
                }
                platformHelpers.getDealsAndUpdateMainRV(adapter, query, null,loadingAnimation);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newQuery) {
                if (newQuery.isEmpty()) {
                    toggleSort.setVisibility(View.VISIBLE);
                } else {
                    toggleSort.setVisibility(View.GONE);
                }
                platformHelpers.getDealsAndUpdateMainRV(adapter, newQuery,  null,loadingAnimation);
                return false;
            }
        });
    }

    private void setCreateDealButtonListener() {
        FloatingActionButton buttonOne = (FloatingActionButton) findViewById(R.id.fab);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateDealActivity.class));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == COARSE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                platformHelpers.getDealsAndUpdateMainRV(adapter, null, null, loadingAnimation);
            }
        }
    }

    private void askCoarsePermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, COARSE_REQUEST_CODE);
    }

    public void checkLocationPermissionAndGetLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askCoarsePermission();
        } else {
            platformHelpers.getCurrentLocation(location -> {
                if (location != null) {
                    currentLocation = location;
                }
            });
        }
    }

    public void sortDealsBy(View view) {
      if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          toggleSort.setChecked(false);
          Toast.makeText(getApplicationContext(), "Please allow location permission to sort by distance.", Toast.LENGTH_SHORT).show();
      } else if (toggleSort.isChecked() && currentLocation != null) {
          platformHelpers.getDealsAndUpdateMainRV(adapter, null, currentLocation, loadingAnimation);
      } else {
          platformHelpers.getDealsAndUpdateMainRV(adapter, null, null, loadingAnimation);
      }
    }
}