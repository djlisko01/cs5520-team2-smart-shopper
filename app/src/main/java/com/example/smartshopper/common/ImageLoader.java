package com.example.smartshopper.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;


import androidx.core.content.ContextCompat;

import com.example.smartshopper.R;
import com.squareup.picasso.Picasso;
import java.io.Serializable;

public class ImageLoader implements Serializable {

    public Picasso loadPicassoImg(Context context, String imgUri, ImageView view) {
        Picasso picasso = new Picasso.Builder(context).build();
        int defaultImg = R.drawable.ic_baseline_shopping_basket_24 ;
        picasso.load(imgUri)
                .placeholder(defaultImg)
                .error(defaultImg).into(view);
        return picasso;
    }
}
