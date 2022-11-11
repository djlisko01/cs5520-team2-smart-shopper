package com.example.smartshopper.recyclerViews;

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


  public DealViewHolder(@NonNull View itemView) {
    super(itemView);
    this.iv_itemPicture = itemView.findViewById(R.id.iv_itemPicture);
    this.tv_dealPostedBy = itemView.findViewById(R.id.tv_dealPostedBy);
    this.tv_dealPostedTime = itemView.findViewById(R.id.tv_dealPostedTime);
  }
}

