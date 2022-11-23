package com.example.smartshopper.recyclerViews;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopper.R;
import com.example.smartshopper.common.Constants;
import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.models.Comment;
import com.example.smartshopper.models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsViewHolder> {
    List<Comment> comments;
    Context context;
    boolean isShowingResponses = false;
    CommentsAdapter response_adapter;
    int depth;

    public CommentsAdapter(Context context, int depth){
        this.depth=depth;
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
        holder.rv_responses.setLayoutManager(new LinearLayoutManager(context));
        this.hideReply(holder, depth);
        PlatformHelpers.loadPicassoImg(context,
                "/hello/",
                holder.img_profilePic,
                R.drawable.missing_profile_pic);

        // Toggle Responses to comment:
        holder.iv_toggleResponses.setOnClickListener(v -> {
            List<Comment> comments = getComments();
            response_adapter = new CommentsAdapter(v.getContext(), this.depth + 1);
            holder.rv_responses.setAdapter(response_adapter);

            isShowingResponses = !isShowingResponses;
            if (isShowingResponses) {
                holder.iv_toggleResponses.setRotation(0);
                response_adapter.updateComments(comments);
            } else{
                holder.iv_toggleResponses.setRotation(90);
            }
        });
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

    public void hideReply(CommentsViewHolder holder, int depth){
        if (depth > Constants.MAX_DEPTH){
            holder.iv_toggleResponses.setVisibility(View.INVISIBLE);
            holder.tv_reply.setVisibility(View.INVISIBLE);
        }
    }

}
