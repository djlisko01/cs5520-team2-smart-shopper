package com.example.smartshopper.recyclerViews;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopper.R;

public class TagsViewHolder extends RecyclerView.ViewHolder {

    public TextView tv_tagText;
    public RadioButton tagRadio;
    public ImageView iv_tagImage;

    public TagsViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tv_tagText = itemView.findViewById(R.id.tv_tagText);
        this.tagRadio = itemView.findViewById(R.id.tagRadio);
        this.iv_tagImage = itemView.findViewById(R.id.iv_tagImage);
    }
}
