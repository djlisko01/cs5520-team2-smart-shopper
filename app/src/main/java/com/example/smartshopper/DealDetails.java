package com.example.smartshopper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.widget.ImageView;

import com.example.smartshopper.common.ImageLoader;
import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.models.Deal;
import com.example.smartshopper.recyclerViews.CommentsAdapter;
import com.squareup.picasso.Picasso;

public class DealDetails extends AppCompatActivity {
    PlatformHelpers platformHelpers;
    ImageView iv_deal_img;
    RecyclerView rv_comments;
    ImageLoader imgLoader = new ImageLoader();
    CommentsAdapter adapter;
    Deal deal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_details);

        platformHelpers = new PlatformHelpers(this);
        iv_deal_img = findViewById(R.id.iv_deal);

        if (getIntent().getExtras() != null) {
            deal = (Deal) getIntent().getSerializableExtra("dealItem");

        adapter = new CommentsAdapter(this);
        rv_comments = findViewById(R.id.rv_comments);
        rv_comments.setLayoutManager(new LinearLayoutManager(this));
        rv_comments.setAdapter(adapter);
        platformHelpers.getCommentsAndUpdateRv(deal, adapter);

            //  This works, but maybe a better way is to pass a picasso obj.
        Picasso picasso = imgLoader.loadPicassoImg(this,
                deal.getProductImg(),
                iv_deal_img,
                R.drawable.ic_baseline_shopping_basket_24);

        }
    }
}