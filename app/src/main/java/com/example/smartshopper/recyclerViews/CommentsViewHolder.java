package com.example.smartshopper.recyclerViews;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopper.R;

public class CommentsViewHolder extends RecyclerView.ViewHolder {

    public TextView tv_comment;
    public TextView tv_userName;
    public ImageView img_profilePic;
    public ImageView iv_toggleResponses;
    public TextView tv_reply;
    RecyclerView rv_responses;

    public CommentsViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tv_comment = itemView.findViewById(R.id.tv_comment);
        this.tv_userName = itemView.findViewById(R.id.tv_commentBy);
        this.img_profilePic = itemView.findViewById(R.id.iv_profilePic);
        this.iv_toggleResponses = itemView.findViewById(R.id.iv_toggleResponses);
        this.rv_responses = itemView.findViewById(R.id.rv_responses);
        this.tv_reply = itemView.findViewById(R.id.tv_reply);
    }
}
