package com.example.smartshopper;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.models.Deal;
import com.example.smartshopper.responseInterfaces.StringInterface;
import com.example.smartshopper.services.CloudStorageService;
import com.example.smartshopper.services.RTDBService;
import com.example.smartshopper.utilities.LocalStorage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;


public class CreateDealActivity extends MenuActivity {
    private static final int LOAD_IMAGE_CODE = 123;
    public static final int IMAGE_CAPTURE_CODE = 654;

    PlatformHelpers platformHelpers;
    CloudStorageService cloudStorageService;
    FloatingActionButton fab_camera;
    FloatingActionButton fab_gallery;
    ImageView iv_imagePreview;
    RTDBService rtdbService = new RTDBService();
    Uri image_uri;
    Location currentLocation;
    View loadingAnimation;
    ArrayList<View> formElements = new ArrayList();
    Button buttonOne;
    EditText titleET;
    EditText descriptionET;
    EditText storeET;
    EditText salePriceET;
    EditText originalPriceET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_deal);
        cloudStorageService = new CloudStorageService();
        platformHelpers = new PlatformHelpers(this);
        fab_camera = findViewById(R.id.camera_fab);
        fab_gallery = findViewById(R.id.gallery_fab);
        iv_imagePreview = findViewById(R.id.iv_imagePreview);
        iv_imagePreview = findViewById(R.id.iv_imagePreview);

        loadingAnimation = findViewById(R.id.loadingAnimation);
        loadingAnimation.setVisibility(View.INVISIBLE);

        fab_camera = findViewById(R.id.camera_fab);
        fab_gallery = findViewById(R.id.gallery_fab);
        buttonOne = findViewById(R.id.createDealButton);
        titleET = findViewById(R.id.editTextCreateTitle);
        descriptionET = findViewById(R.id.editTextCreateDescription);
        storeET = findViewById(R.id.editTextStore);
        salePriceET = findViewById(R.id.editTextSalePrice);
        originalPriceET = findViewById(R.id.editTextPrice);
        Collections.addAll(formElements, buttonOne, titleET, fab_camera, fab_gallery, descriptionET, storeET, salePriceET, originalPriceET);

        if (buttonOne != null) {
            setCreateDealButtonListener();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            currentLocation = null;
        }
        else {
            platformHelpers.getCurrentLocation(location -> {
                if (location != null) {
                    currentLocation = location;
                }
            });
        }

        // Camera image
        fab_camera.setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.CAMERA};
                requestPermissions(permission, 1);
            } else {
                openCamera();
            }
        });

        // Choose image from gallery
        fab_gallery.setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permission, 2);
            } else {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, LOAD_IMAGE_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Camera Flow
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == RESULT_OK) {
            try (InputStream in = getContentResolver().openInputStream(image_uri)) {
                // Set the image in imageview for display
                Glide.with(this)
                        .load(image_uri)
                        .into(iv_imagePreview);
            } catch (IOException e) {
                e.printStackTrace();
            }
        // Gallery Flow
        } else if (requestCode == LOAD_IMAGE_CODE && resultCode == RESULT_OK && data != null) {
            image_uri = data.getData();
            iv_imagePreview.setImageURI(image_uri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // Camera request code 1
            case 1:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(getApplicationContext(), "Please allow access to camera if you wish to upload pictures.", Toast.LENGTH_LONG).show();
                }
                break;
            // Gallery request code 2
            case 2:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, LOAD_IMAGE_CODE);
                } else {
                    Toast.makeText(getApplicationContext(), "Please allow access to gallery if you wish to upload pictures.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    private void setCreateDealButtonListener() {
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                loadingAnimation.setVisibility(View.VISIBLE);
                for (View view : formElements) {
                    view.setEnabled(false);
                }
                // Get the deal information from the form
                String title = titleET.getText().toString();
                String description = descriptionET.getText().toString();
                String store = storeET.getText().toString();
                String salePrice = salePriceET.getText().toString();
                String originalPrice = originalPriceET.getText().toString();

                // Check for required fields
                if (title.trim().isEmpty() || store.trim().isEmpty() || salePrice.isEmpty() || originalPrice.isEmpty()) {
                    loadingAnimation.setVisibility(View.GONE);
                    for (View view : formElements) {
                        view.setEnabled(true);
                    }
                    Toast.makeText(getApplicationContext(), "Please fill out required fields.", Toast.LENGTH_SHORT).show();
                } else {
                    Double salePriceDouble = Double.parseDouble(salePrice);
                    Double originalPriceDouble = Double.parseDouble(originalPrice);
                    Double latitude = 0.0;
                    Double longitude = 0.0;

                    if (currentLocation != null) {
                        latitude = new Double(currentLocation.getLatitude());
                        longitude = new Double(currentLocation.getLongitude());
                    }
                    // Get currently logged in userUUID
                    LocalStorage localStorage = new LocalStorage(CreateDealActivity.this);
                    String userID = localStorage.getCurrentUserID();

                    // Create a new deal object
                    Deal deal;
                    if (currentLocation == null) {
                        deal = new Deal("", title, originalPriceDouble, salePriceDouble, description, store, userID);
                    }
                    else {
                        deal = new Deal("",
                                title,
                                originalPriceDouble,
                                salePriceDouble,
                                description,
                                store,
                                userID,
                                latitude,
                                longitude);
                    }
                    Intent intent = new Intent(CreateDealActivity.this, DealDetailsActivity.class);
                    // Check if image uploaded flow
                    if (image_uri != null && !image_uri.toString().isEmpty()) {
                        // Get image
                        cloudStorageService.uploadWithURI("images/" + image_uri.getLastPathSegment(), image_uri, new StringInterface() {
                            @Override
                            public void onCallback(String response) {
                                String downloadURL = response;
                                deal.setProductImg(downloadURL);
                                String dealID = rtdbService.writeDeal(deal);
                                deal.setDealID(dealID);
                                // Go to detailed view of the deal
                                intent.putExtra("dealItem", deal);
                                loadingAnimation.setVisibility(View.GONE);
                                startActivity(intent);
                                CreateDealActivity.this.finish();
                            }
                        });
                        // no image uploaded flow
                    } else {
                        String dealID = rtdbService.writeDeal(deal);
                        deal.setDealID(dealID);
                        // Go to detailed view of the deal
                        intent.putExtra("dealItem", deal);
                        startActivity(intent);
                        CreateDealActivity.this.finish();
                    }
                }
            }
        });

    }
}

