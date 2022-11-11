package com.example.smartshopper.recyclerViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopper.R;
import com.example.smartshopper.projectModels.Deal;

import java.util.List;

public class DealAdapter extends RecyclerView.Adapter<DealViewHolder> {
    private List<Deal> deals;
    private final Context context;

    public DealAdapter(List<Deal> deals, Context context) {
        this.deals = deals;
        this.context = context;
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DealViewHolder(LayoutInflater.from(context).inflate(R.layout.deal, null));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        String stringUri = deals.get(position).getImageURI().isEmpty() ?
                "@drawable/ic_baseline_shopping_basket_24" : "@drawable/" + deals.get(position).getImageURI();
        Drawable drawableReference = ContextCompat.getDrawable(context, context.getResources().getIdentifier(stringUri, "drawable", context.getPackageName()));
        holder.iv_itemPicture.setImageDrawable(drawableReference);

        holder.tv_dealPostedBy.setText(deals.get(position).getTimePosted().toString());
        holder.tv_dealPostedBy.setText(String.valueOf(deals.get(position).getPoster()));
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    public void updateData(List<Deal> messages) {
        this.deals = messages;
        notifyDataSetChanged();
    }
}
