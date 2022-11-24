package com.example.smartshopper.recyclerViews;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopper.R;

public class ProfileViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_activities;

    public ProfileViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tv_activities = itemView.findViewById(R.id.activities);
    }
}
