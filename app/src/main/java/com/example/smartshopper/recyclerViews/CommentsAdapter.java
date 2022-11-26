package com.example.smartshopper.recyclerViews;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopper.DealDetailsActivity;
import com.example.smartshopper.R;
import com.example.smartshopper.common.Constants;
import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.models.Comment;
import com.example.smartshopper.models.Deal;
import com.example.smartshopper.models.User;
import com.example.smartshopper.utilities.CommentInputDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsViewHolder> {
    List<Comment> comments;
    List<Comment> responses = new ArrayList<>();
    Context context;
    boolean isShowingResponses = false;
    CommentsAdapter response_adapter;
    Deal deal;
    int depth = 0;

    // For new comments
    public CommentsAdapter(Context context, Deal deal){
        this.comments = new ArrayList<>();
        this.context = context;
        this.deal = deal;
    }

    // For response to comments
    public CommentsAdapter(Context context, Deal deal, int depth){
        this.comments = new ArrayList<>();
        this.context = context;
        this.depth = depth;
        this.deal = deal;
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
        responses = comments.get(holder.getAbsoluteAdapterPosition()).getResponses();

        // Load user profile picture.
        PlatformHelpers.loadPicassoImg(context,
                "/hello/",
                holder.img_profilePic,
                R.drawable.missing_profile_pic);

        // Hides Reply and Comments if Max Depth reach
        this.hideReply(holder, depth);


        // Toggle Responses to comment:
        if (responses.size() > 0) {
            holder.iv_toggleResponses.setOnClickListener(v -> {
                response_adapter = new CommentsAdapter(v.getContext(), deal,depth + 1);
                holder.rv_responses.setAdapter(response_adapter);

                isShowingResponses = !isShowingResponses;
                if (isShowingResponses) {
                    holder.iv_toggleResponses.setRotation(0);
                    response_adapter.updateComments(responses);
                } else {
                    holder.iv_toggleResponses.setRotation(90);
                }
            });
        }

        holder.tv_reply.setOnClickListener(v -> {
            response_adapter = new CommentsAdapter(v.getContext(), deal,depth + 1);
            CommentInputDialog commentInputDialog = new CommentInputDialog(
                    deal,
                    comments,
                    holder.getAbsoluteAdapterPosition(),
                    response_adapter,
                    this);
            this.notifyItemChanged(holder.getAbsoluteAdapterPosition());
            commentInputDialog.show(((AppCompatActivity)context).getSupportFragmentManager(), "Title");
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
        if (depth >= Constants.MAX_DEPTH){
            holder.iv_toggleResponses.setVisibility(View.INVISIBLE);
            holder.tv_reply.setVisibility(View.INVISIBLE);
        }
    }


}
