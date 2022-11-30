package com.example.smartshopper;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.smartshopper.models.Deal;
import com.example.smartshopper.services.RTDBService;
import com.example.smartshopper.utilities.LocalStorage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;


public class CreateDealActivity extends MenuActivity {
    FloatingActionButton fab_camera;
    FloatingActionButton fab_gallery;
    ImageView iv_imagePreview;

    Uri image_uri;
    private static final int LOAD_IMAGE_CODE = 123;
    public static final int IMAGE_CAPTURE_CODE = 654;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_deal);
        setCreateDealButtonListener();

        fab_camera = findViewById(R.id.camera_fab);
        fab_gallery = findViewById(R.id.gallery_fab);
        iv_imagePreview = findViewById(R.id.iv_imagePreview);

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
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == RESULT_OK) {
            try (InputStream in = getContentResolver().openInputStream(image_uri)) {
                // Set the image in imageview for display
                Glide.with(this)
                        .load(image_uri)
                        .into(iv_imagePreview);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        Button buttonOne = (Button) findViewById(R.id.createDealButton);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // Get the deal information from the form
                String title = ((EditText) findViewById(R.id.editTextCreateTitle)).getText().toString();
                String upc = ((EditText) findViewById(R.id.editTextCreateUPC)).getText().toString();
                String description = ((EditText) findViewById(R.id.editTextCreateDescription)).getText().toString();
                String store = ((EditText) findViewById(R.id.editTextStore)).getText().toString();
                String salePrice = ((EditText) findViewById(R.id.editTextSalePrice)).getText().toString();
                String originalPrice = ((EditText) findViewById(R.id.editTextPrice)).getText().toString();
                Double salePriceDouble = Double.parseDouble(salePrice);
                Double originalPriceDouble = Double.parseDouble(originalPrice);


                // Get currently logged in userUUID
                LocalStorage localStorage = new LocalStorage(CreateDealActivity.this);
                String userID = localStorage.getCurrentUserID();

                // Create a new deal object
                Deal deal = new Deal(upc, title, originalPriceDouble, salePriceDouble, description, store, userID);

                RTDBService rtdbService = new RTDBService();
                String dealID = rtdbService.writeDeal(deal);
                deal.setDealID(dealID);

                // Go to detailed view of the deal
                Intent intent = new Intent(CreateDealActivity.this, DealDetailsActivity.class);
                intent.putExtra("dealItem", deal);
                startActivity(intent);
            }
        });

    }
}

