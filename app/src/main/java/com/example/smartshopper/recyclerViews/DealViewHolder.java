package com.example.smartshopper.recyclerViews;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

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
    public FrameLayout frame_upVote;
    public FrameLayout frame_downVote;
    public ToggleButton tb_saveDeal;

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
        this.iv_downVote = itemView.findViewById(R.id.iv_downVote);
        this.tb_saveDeal = itemView.findViewById(R.id.tb_saveDeal);
        this.frame_upVote = itemView.findViewById(R.id.frame_upVote);
        this.frame_downVote = itemView.findViewById(R.id.frame_downVote);
    }
}

