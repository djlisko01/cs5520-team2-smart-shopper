package com.example.smartshopper;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopper.models.Tag;
import com.example.smartshopper.recyclerViews.TagsAdapter;

import java.util.ArrayList;
import java.util.List;

public class FollowTagsActivity extends AppCompatActivity {
    RecyclerView rv_tagsRecyclerView;
//    PlatformHelpers platformHelpers;
    TagsAdapter adapter;
//    LocalStorage localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_tags);

        adapter = new TagsAdapter(this);

        // Recycler View setup
        rv_tagsRecyclerView = findViewById(R.id.rv_tagsRecyclerView);
        rv_tagsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        rv_tagsRecyclerView.setAdapter(adapter);

        List<Tag> tagList = new ArrayList<>();

        Tag apparelTag = new Tag("Apparel", false, "ic_baseline_dry_cleaning_24");
        Tag electronicsTag = new Tag("Electronics", false, "ic_baseline_radio_24");
        Tag homeGoodsTag = new Tag("Home Goods", false, "ic_baseline_home_24");
        Tag hardwareTag = new Tag("Hardware", false, "ic_baseline_construction_24");
        Tag furnitureTag = new Tag("Furniture", false, "ic_baseline_chair_24");
        Tag foodTag = new Tag("Food", false, "ic_baseline_ramen_dining_24");
        Tag cookingTag = new Tag("Cooking", false, "ic_baseline_local_dining_24");
        Tag petTag = new Tag("Pets", false, "ic_baseline_pets_24");
        Tag artSuppliesTag = new Tag("Art Supplies", false, "ic_baseline_color_lens_24");
        Tag travelTag = new Tag("Travel", false, "ic_baseline_connecting_airports_24");

        tagList.add(apparelTag);
        tagList.add(electronicsTag);
        tagList.add(homeGoodsTag);
        tagList.add(hardwareTag);
        tagList.add(furnitureTag);
        tagList.add(foodTag);
        tagList.add(cookingTag);
        tagList.add(petTag);
        tagList.add(artSuppliesTag);
        tagList.add(travelTag);
        adapter.updateData(tagList);
        // Get deals from firebase:
//        platformHelpers.getDealsAndUpdateMainRV(adapter, null);
    }
}
