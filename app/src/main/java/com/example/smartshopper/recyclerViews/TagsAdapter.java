package com.example.smartshopper.recyclerViews;

import static java.util.Objects.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopper.R;
import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.models.Tag;
import com.google.android.material.radiobutton.MaterialRadioButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TagsAdapter extends RecyclerView.Adapter<TagsViewHolder>{
    List<Tag> tags;
    Context context;
    public TagsAdapter(Context context) {
        this.tags = new ArrayList<>();
        this.context = context;
    }


    @NonNull
    @Override
    public TagsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TagsViewHolder(LayoutInflater.from(context).inflate(R.layout.tag, null));
    }

    @Override
    public void onBindViewHolder(@NonNull TagsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String tagText = tags.get(position).getTagText();
        String tagImage = tags.get(position).getTagIcon();
        Boolean tagIsChecked = tags.get(position).tagIsChecked();

        holder.tv_tagText.setText(tagText);
        holder.tagRadio.setChecked(tagIsChecked);
        String stringUri = "@drawable/" + tagImage;
        Drawable drawableReference = ContextCompat.getDrawable(context, context.getResources().getIdentifier(stringUri, "drawable", context.getPackageName()));
        holder.iv_tagImage.setImageDrawable(drawableReference);
        holder.tagRadio.setOnClickListener(v -> {
            tags.get(position).setTagIsChecked(!tags.get(position).tagIsChecked());
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public void updateData(List<Tag> tags) {
        this.tags = tags;
        notifyDataSetChanged();
    }
}
