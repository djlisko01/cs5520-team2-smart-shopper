package com.example.smartshopper.recyclerViews;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopper.R;

public class ProfileViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_username;
    public TextView tv_activities;

    public ProfileViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.tv_username = itemView.findViewById(R.id.tv_username);
        this.tv_activities = itemView.findViewById(R.id.activities);
    }
}
