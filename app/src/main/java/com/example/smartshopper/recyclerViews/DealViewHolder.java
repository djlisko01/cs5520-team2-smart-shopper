package com.example.smartshopper.recyclerViews;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartshopper.R;

public class DealViewHolder extends RecyclerView.ViewHolder {

    public ImageView iv_itemPicture;
    public TextView tv_dealPostedBy;
    public TextView tv_dealPostedTime;
    public TextView tv_dealTitle;
    public TextView tv_originalPrice;
    public TextView tv_salePrice;
    public TextView tv_store;
    public TextView tv_numDownVotes;
    public TextView tv_numUpvotes;
    public ImageView iv_upVote;
    public ImageView iv_downVote;

    public DealViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.iv_itemPicture = itemView.findViewById(R.id.iv_itemPicture);
        this.tv_dealPostedBy = itemView.findViewById(R.id.tv_dealPostedBy);
        this.tv_dealPostedTime = itemView.findViewById(R.id.tv_dealPostedTime);
        this.tv_dealTitle = itemView.findViewById(R.id.tv_dealTitle);
        this.tv_originalPrice = itemView.findViewById(R.id.tv_originalPrice);
        this.tv_salePrice = itemView.findViewById(R.id.tv_salePrice);
        this.tv_store = itemView.findViewById(R.id.tv_store);
        this.tv_numDownVotes = itemView.findViewById(R.id.tv_numDownVotes);
        this.tv_numUpvotes = itemView.findViewById(R.id.tv_numUpvotes);
        this.iv_upVote = itemView.findViewById(R.id.iv_upVote);
    }
}

