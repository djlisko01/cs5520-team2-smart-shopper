package com.example.smartshopper.recyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopper.R;
import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.models.Comment;
import com.example.smartshopper.models.Deal;

import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileViewHolder>{

    private List<String> activities;
    private final Context context;
    private final PlatformHelpers platformHelpers;

    public ProfileAdapter(Context context) {
        this.activities = new ArrayList<>();
        this.context = context;
        this.platformHelpers = new PlatformHelpers(this.context);
    }


    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfileViewHolder(LayoutInflater.from(context).inflate(R.layout.activities,null),context);

    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        holder.tv_activities.setText(activities.get(position));
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public void updateTitle(List<String> activities){
        this.activities = activities;
        notifyDataSetChanged();
    }


}
