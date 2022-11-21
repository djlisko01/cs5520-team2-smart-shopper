package com.example.smartshopper.recyclerViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopper.DealDetailsActivity;
import com.example.smartshopper.R;
import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.models.Deal;
import com.example.smartshopper.responseInterfaces.StringInterface;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DealAdapter extends RecyclerView.Adapter<DealViewHolder> {
    private List<Deal> deals;
    private final Context context;
    private final PlatformHelpers platformHelpers;

    public DealAdapter(Context context) {
        this.deals = new ArrayList<>();
        this.context = context;
        this.platformHelpers = new PlatformHelpers(this.context);
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
        // Image
        PlatformHelpers.loadPicassoImg(context,
          deals.get(position).getProductImg(),
          holder.iv_itemPicture,
          R.drawable.ic_baseline_shopping_basket_24);

        // Username
        String userUUID = deals.get(position).getUserUUID();
        platformHelpers.getUserByUUID(userUUID,
          user -> holder.tv_dealPostedBy.setText(user.getUsername()));

        holder.tv_dealPostedTime.setText(formatDate(deals.get(position).getTimePosted()));
        holder.tv_dealTitle.setText(deals.get(position).getTitle());
        holder.tv_store.setText(deals.get(position).getStore());
        holder.tv_originalPrice.setText(NumberFormat.getCurrencyInstance().format(deals.get(position).getOriginalPrice()));
        holder.tv_originalPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG); // strike-through OG price
        holder.tv_salePrice.setText(NumberFormat.getCurrencyInstance().format(deals.get(position).getSalePrice()));
        holder.tv_numDownVotes.setText(deals.get(position).getNumDownVotes().toString());
        holder.tv_numUpvotes.setText(deals.get(position).getNumUpVotes().toString());

        // Save deals
        platformHelpers.isSaved(deals.get(position).getDealID(), response -> {
            Log.d("SAVED", response + "");
            holder.tb_saveDeal.setChecked(response);
        });

        holder.tb_saveDeal.setOnClickListener(v -> {
            if (holder.tb_saveDeal.isChecked()) {
                platformHelpers.saveDeal(deals.get(position).getDealID());
            } else {
                platformHelpers.getSavedDealKey(deals.get(position).getDealID(), key -> {
                    platformHelpers.removeDeal(key);
                });
            }
        });

        // Up vote down vote listeners
        holder.iv_downVote.setOnClickListener(v -> {
            platformHelpers.downVoteDeal(deals.get(position).getDealID());
        });
        holder.iv_upVote.setOnClickListener(v -> {
            platformHelpers.upVoteDeal(deals.get(position).getDealID());
        });
        holder.tv_numDownVotes.setOnClickListener(v -> {
            platformHelpers.downVoteDeal(deals.get(position).getDealID());
        });

        holder.tv_numUpvotes.setOnClickListener(v -> {
            platformHelpers.upVoteDeal(deals.get(position).getDealID());
        });

        // Entire deal card listener
        holder.itemView.setOnClickListener(v -> {
            if (context != null) {
                Intent intent = new Intent(context, DealDetailsActivity.class);
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

    public void searchByTitle(String query) {
        List<Deal> filteredDeals = new ArrayList<>();

        for (Deal d : deals) {
            if (d.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredDeals.add(d);
            }
        }

        updateData(filteredDeals);
    }
}