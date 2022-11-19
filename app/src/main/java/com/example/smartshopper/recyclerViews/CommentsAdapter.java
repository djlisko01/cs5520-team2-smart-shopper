package com.example.smartshopper.recyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopper.R;
import com.example.smartshopper.common.ImageLoader;
import com.example.smartshopper.models.Comment;
import com.example.smartshopper.models.User;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsViewHolder> {
    List<Comment> comments;
    Context context;

    public CommentsAdapter(Context context){
        comments = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.comment, null);

        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        User user = comments.get(position).getAuthor();
        holder.tv_comment.setText(comments.get(position).getText());
        holder.tv_userName.setText(user.getUsername());

        ImageLoader imageLoader = new ImageLoader();
        imageLoader.loadPicassoImg(context,
                "/hello/",
                holder.img_profilePic,
                R.drawable.missing_profile_pic);
    }

    @Override
    public int getItemCount() {
        return this.comments.size();
    }

    public List<Comment> getComments(){
        return this.comments;
    }

    public void updateComments(List<Comment> comments){
        this.comments = comments;
        notifyDataSetChanged();
    }
}
