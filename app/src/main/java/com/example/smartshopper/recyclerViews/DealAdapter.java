package com.example.smartshopper.recyclerViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopper.DealDetails;
import com.example.smartshopper.R;
import com.example.smartshopper.common.Constants;
import com.example.smartshopper.models.Deal;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class DealAdapter extends RecyclerView.Adapter<DealViewHolder> {
    private List<Deal> deals;
    private final Context context;

    public DealAdapter(List<Deal> deals, Context context) {
        this.deals = deals;
        this.context = context;
    }

    public DealAdapter(Context context) {
        this.deals = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DealViewHolder(LayoutInflater.from(context).inflate(R.layout.deal, null),
                context);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        String imageURI = deals.get(position).getProductImg().isEmpty() ?
                Constants.DEFAULT_ITEM_IMAGE : deals.get(position).getProductImg();
        // no imageURI - set default picture from drawable
        if (imageURI.equals(Constants.DEFAULT_ITEM_IMAGE)) {
            Drawable drawable = ContextCompat.getDrawable(context, context.getResources()
                    .getIdentifier(imageURI, "drawable", context.getPackageName()));
            holder.iv_itemPicture.setImageDrawable(drawable);
            // imageURI - set to image URI from Firebase
        } else {
            Picasso.get().load(imageURI).into(holder.iv_itemPicture);
        }

        holder.tv_dealPostedTime.setText(formatDate(deals.get(position).getTimePosted()));
        holder.tv_dealPostedBy.setText(String.valueOf(deals.get(position).getDealPostedBy()));
        holder.tv_dealTitle.setText(deals.get(position).getTitle());
        holder.tv_store.setText(deals.get(position).getStore());
        holder.tv_originalPrice.setText(NumberFormat.getCurrencyInstance().format(deals.get(position).getOriginalPrice()));
        holder.tv_originalPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG); // strike-through OG price
        holder.tv_salePrice.setText(NumberFormat.getCurrencyInstance().format(deals.get(position).getSalePrice()));

        holder.itemView.setOnClickListener(v -> {
            if (context != null){
                Intent intent = new Intent(context, DealDetails.class);
                intent.putExtra("dealItem", deals.get(holder.getAbsoluteAdapterPosition()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    public void updateData(List<Deal> deals) {
        this.deals = deals;
        notifyDataSetChanged();
    }

    public String formatDate(long timestamp) {
        return DateFormat.getDateInstance(DateFormat.LONG).format(timestamp);
    }
}
