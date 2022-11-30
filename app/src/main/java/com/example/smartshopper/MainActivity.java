package com.example.smartshopper;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

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
    PlatformHelpers platformHelpers;
    DealAdapter adapter;
    LocalStorage localStorage;
  FusedLocationProviderClient fusedLocationProviderClient;
  LocationManager locationManager;
  private final static int FINE_REQUEST_CODE = 200;
  private final static int COARSE_REQUEST_CODE = 200;
  Context context = this;
  Location currentLocation;

  private void askCoarsePermission() {
    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, COARSE_REQUEST_CODE);
  }

  private void askFinePermission() {
    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_REQUEST_CODE);
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      askCoarsePermission();
    }
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      askFinePermission();
    }
    getLocationAndUpdateRV();
    // Setup Search Listener
    setSearchListener();
    // Setup button listener on add deal (+) button
    setCreateDealButtonListener();
    Toast.makeText(context, "RESUMING", Toast.LENGTH_LONG).show();
  }


  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localStorage = new LocalStorage(this);
        // Instantiate objects

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        platformHelpers = new PlatformHelpers(this);
        adapter = new DealAdapter(this);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Toast.makeText(context, "onCreate", Toast.LENGTH_LONG).show();
        // Recycler View setup
        rv_dealsRecyclerView = findViewById(R.id.rv_dealsRecyclerView);
        rv_dealsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_dealsRecyclerView.setAdapter(adapter);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
          new AlertDialog.Builder(context)
            .setMessage("This app uses device location. Please turn on your devices location for best experience.")
            .setPositiveButton("OK", (paramDialogInterface, paramInt) -> {
              startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            })
            .setNegativeButton("No thanks", (dialog, which) -> {
              return;
            })
            .show();
        }
        getLocationAndUpdateRV();

        // Setup Search Listener
        setSearchListener();

        // Setup button listener on add deal (+) button
        setCreateDealButtonListener();
        // Notification channel
        if (localStorage.userIsLoggedIn()) {
          platformHelpers.createNotifChannel();
        }
    }

    private void setSearchListener() {
        SearchView searchView = findViewById(R.id.sv_searchBar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                platformHelpers.getDealsAndUpdateMainRV(adapter,null, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newQuery) {
                platformHelpers.getDealsAndUpdateMainRV(adapter, null, newQuery);
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
    if (grantResults.length > 0 && grantResults[0] == -1) {
      Toast.makeText(context, "Unable to retrieve location information. Your results will not be local.", Toast.LENGTH_LONG).show();
    }
    else {
      Log.v("onRequestPermissionsResult", "updating");
      getLocationAndUpdateRV();
    }
  }

  public void getLocationAndUpdateRV() {
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
      if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        askCoarsePermission();
      }
      if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        askFinePermission();
      }
      currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
      platformHelpers.getDealsAndUpdateMainRV(adapter, currentLocation, null);
    }
    else {
      currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
      platformHelpers.getDealsAndUpdateMainRV(adapter, currentLocation, null);
    }
  }
}