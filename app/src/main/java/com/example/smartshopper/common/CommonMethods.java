package com.example.smartshopper.common;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class CommonMethods {

    /**
     * Loads picture using Piccasso Library
     *
     * @param context    view you are loading into
     * @param imgUri     string of the URI of the image
     * @param view       ImageView you are trying to load the picture into
     * @param defaultImg default image if there is an error (should be a local asset)
     */
    public static Picasso loadPicassoImg(Context context, String imgUri, ImageView view, int defaultImg) {
        Picasso picasso = new Picasso.Builder(context).build();
        picasso.load(imgUri)
                .error(defaultImg)
                .into(view);
        return picasso;
    }
}
