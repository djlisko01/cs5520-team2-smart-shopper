package com.example.smartshopper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.smartshopper.models.Deal;
import com.example.smartshopper.services.RTDBService;
import com.example.smartshopper.utilities.LocalStorage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreateDealActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_deal);
        setCreateDealButtonListener();


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
                String salePrice = ((EditText) findViewById(R.id.editTextPrice)).getText().toString();
                Double salePriceDouble = Double.parseDouble(salePrice);


                // Get currently logged in userUUID
                LocalStorage localStorage = new LocalStorage(CreateDealActivity.this);
                String userID = localStorage.getCurrentUserID();

                // Create a new deal object
                Deal deal = new Deal(upc, title, salePriceDouble, salePriceDouble, description, store, userID);


                RTDBService rtdbService = new RTDBService();
                String dealID = rtdbService.writeDeal(deal);

                // Go back to the main activity
                startActivity(new Intent(CreateDealActivity.this, MainActivity.class));

            }
        });

    }
}

