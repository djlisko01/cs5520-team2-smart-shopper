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

    private List<Deal> deals;
    private final Context context;

    public ProfileAdapter(Context context) {
        this.deals = new ArrayList<>();
        this.context = context;
    }


    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfileViewHolder(LayoutInflater.from(context).inflate(R.layout.activities, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        holder.tv_activities.setText(deals.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public List<Deal> getDeals(){
        return this.deals;
    }

    public void updateTitle(List<Deal> deals){
        this.deals = deals;
        notifyDataSetChanged();
    }


}
