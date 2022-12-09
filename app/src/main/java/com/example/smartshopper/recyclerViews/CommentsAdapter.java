package com.example.smartshopper.recyclerViews;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;


import com.example.smartshopper.R;

import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.models.Comment;
import com.example.smartshopper.models.Deal;

import com.example.smartshopper.responseInterfaces.StringInterface;
import com.example.smartshopper.utilities.LocalStorage;

import java.util.ArrayList;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsViewHolder> {
    PlatformHelpers platformHelpers;
    List<Comment> comments;
//    List<Comment> responses;
    Context context;
    LocalStorage localStorage;
//    boolean isShowingResponses = false;
//    CommentsAdapter response_adapter;
    Deal deal;
//    int depth;
//    boolean isChildComment;

    // For new comments
    public CommentsAdapter(Context context, Deal deal) {
        this.comments = new ArrayList<>();
        this.context = context;
        this.deal = deal;
        platformHelpers = new PlatformHelpers(context);
//        this.depth = 0;
//        this.isChildComment = false;
    }

//    // For response to comments
//    public CommentsAdapter(Context context, Deal deal, int depth) {
//        this.comments = new ArrayList<>();
//        this.context = context;
//        this.depth = depth;
//        this.deal = deal;
//        this.isChildComment = true;
//    }

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
        String username = comments.get(position).getAuthor().getUsername();
        holder.tv_userName.setText(username);
//        holder.rv_responses.setLayoutManager(new LinearLayoutManager(context));

        // Load user profile picture.

        platformHelpers.getUserByUsername(username, new StringInterface() {
            @Override
            public void onCallback(String userID) {
                platformHelpers.getUserImg(userID, new StringInterface() {
                    @Override
                    public void onCallback(String response) {
                        PlatformHelpers.loadImg(context, response, holder.img_profilePic, R.drawable.missing_profile_pic);
                    }
                });

            }
        });

        // Hide Reply for users that aren't logged in.
//        if (!localStorage.userIsLoggedIn()) {
//            holder.tv_reply.setVisibility(View.INVISIBLE);
//        }

        // Hides Reply and toggle for Comments if Max Depth reach
//        if (depth >= Constants.MAX_DEPTH){
//            holder.tv_reply.setVisibility(View.INVISIBLE);
//            holder.iv_toggleResponses.setVisibility(View.INVISIBLE);
//            holder.img_replyBubble.setVisibility(View.INVISIBLE);
//            holder.tv_replyCount.setVisibility(View.INVISIBLE);
//        }

//        int numResponse = comments.get(position).getListReplies().size();
//        holder.tv_replyCount.setText("" + numResponse);
//
//        if (!this.isChildComment) {
//            if(numResponse <= 0){
//                holder.iv_toggleResponses.setVisibility(View.INVISIBLE);
//            }
//        }



        // Toggle Responses to comment:
//        holder.iv_toggleResponses.setOnClickListener(v -> {
//            responses = comments.get(holder.getAbsoluteAdapterPosition()).getListReplies();
//            response_adapter = new CommentsAdapter(v.getContext(), deal,depth + 1);
//            holder.rv_responses.setAdapter(response_adapter);
//            showReplies(holder);
//        });

        // Allows user to reply to a comment.
//        holder.tv_reply.setOnClickListener(v -> {
//            response_adapter = new CommentsAdapter(v.getContext(), deal, depth + 1);
//            CommentInputDialog commentInputDialog = new CommentInputDialog(
//                    deal,
//                    comments,
//                    holder,
//                    response_adapter,
//                    this);
//            commentInputDialog.show(((AppCompatActivity)context).getSupportFragmentManager(), "reply");

//            this.notifyItemChanged(position);
//        });
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

//    public void showReplies(CommentsViewHolder holder){
//        isShowingResponses = !isShowingResponses;
//        if (isShowingResponses) {
//            holder.iv_toggleResponses.setRotation(0);
//            response_adapter.updateComments(responses);
//        } else {
//            holder.iv_toggleResponses.setRotation(90);
//        }
//    }
}
