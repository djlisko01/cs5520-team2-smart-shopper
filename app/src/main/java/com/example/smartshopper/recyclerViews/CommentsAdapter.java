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
import com.example.smartshopper.utilities.LocalStorage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsViewHolder> {
    List<Comment> comments;
    List<Comment> responses;
    Context context;
    LocalStorage localStorage;
    boolean isShowingResponses = false;
    CommentsAdapter response_adapter;
    Deal deal;
    int depth = 0;

    // For new comments
    public CommentsAdapter(Context context, Deal deal) {
        this.comments = new ArrayList<>();
        this.context = context;
        this.deal = deal;
    }

    // For response to comments
    public CommentsAdapter(Context context, Deal deal, int depth) {
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
        localStorage = new LocalStorage(context);
        holder.tv_comment.setText(comments.get(position).getText());
        String username;

        if (localStorage.userIsLoggedIn()) {
            username = localStorage.getCurrentUser();
        } else{
            username = "";
        }

        holder.tv_userName.setText(username);
        holder.rv_responses.setLayoutManager(new LinearLayoutManager(context));


        // Load user profile picture.
        PlatformHelpers.loadPicassoImg(context,
                "/hello/",
                holder.img_profilePic,
                R.drawable.missing_profile_pic);

        // Hide Reply for users that aren't logged in.
        if (!localStorage.userIsLoggedIn()) {
            holder.tv_reply.setVisibility(View.INVISIBLE);
        }

        // Hides Reply and toggle for Comments if Max Depth reach
        if (depth >= Constants.MAX_DEPTH){
            holder.tv_reply.setVisibility(View.INVISIBLE);
            holder.iv_toggleResponses.setVisibility(View.INVISIBLE);
            holder.img_replyBubble.setVisibility(View.INVISIBLE);
            holder.tv_replyCount.setVisibility(View.INVISIBLE);
        }

        int numResponse = comments.get(position).getListReplies().size();
        holder.tv_replyCount.setText("" + numResponse);
        if (numResponse < 1){
            holder.iv_toggleResponses.setVisibility(View.INVISIBLE);
        }

        // Toggle Responses to comment:
        holder.iv_toggleResponses.setOnClickListener(v -> {
            responses = comments.get(holder.getAbsoluteAdapterPosition()).getListReplies();
            response_adapter = new CommentsAdapter(v.getContext(), deal,depth + 1);
            holder.rv_responses.setAdapter(response_adapter);
            showReplies(holder);
        });

        // Allows user to reply to a comment.
        holder.tv_reply.setOnClickListener(v -> {
            response_adapter = new CommentsAdapter(v.getContext(), deal, depth + 1);
            CommentInputDialog commentInputDialog = new CommentInputDialog(
                    deal,
                    comments,
                    holder.getAbsoluteAdapterPosition(),
                    response_adapter,
                    this);
            this.notifyItemChanged(position);
            commentInputDialog.show(((AppCompatActivity)context).getSupportFragmentManager(), "reply");
        });
    }

    @Override
    public int getItemCount() {
        return this.comments.size();
    }

    public List<Comment> getComments() {
        return this.comments;
    }

    public void updateComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    public void showReplies(CommentsViewHolder holder){
        isShowingResponses = !isShowingResponses;
        if (isShowingResponses) {
            holder.iv_toggleResponses.setRotation(0);
            response_adapter.updateComments(responses);
        } else {
            holder.iv_toggleResponses.setRotation(90);
        }
    }
}
