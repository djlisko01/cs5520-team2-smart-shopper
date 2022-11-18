package com.example.smartshopper;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.ImageView;

import com.example.smartshopper.common.ImageLoader;
import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.models.Deal;
import com.squareup.picasso.Picasso;

public class DealDetails extends AppCompatActivity {
    PlatformHelpers platformHelpers;
    ImageView iv_deal_img;
    ImageLoader imgLoader = new ImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_details);

        platformHelpers = new PlatformHelpers(this);
        iv_deal_img = findViewById(R.id.iv_deal);

        if (getIntent().getExtras() != null) {
            Deal deal = (Deal) getIntent().getSerializableExtra("dealItem");
            platformHelpers.getCommentsAndUpdateRv(deal);
            
        //  This works, but maybe a better way is to pass a picasso obj.
        Picasso picasso = imgLoader.loadPicassoImg(this, deal.getProductImg(), iv_deal_img);

        }
    }
}