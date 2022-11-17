package com.example.smartshopper;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.smartshopper.models.Deal;

public class DealDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_details);

        if (getIntent().getExtras() != null) {
            Deal deal = (Deal) getIntent().getSerializableExtra("dealItem");
            Log.v("DEAL_PAGE", deal.getTitle());
        }
    }
}