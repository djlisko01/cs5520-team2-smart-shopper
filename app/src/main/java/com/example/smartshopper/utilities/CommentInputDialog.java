package com.example.smartshopper.utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.smartshopper.DealDetailsActivity;
import com.example.smartshopper.R;
import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.models.Comment;
import com.example.smartshopper.models.Deal;
import com.example.smartshopper.models.User;
import com.example.smartshopper.recyclerViews.CommentsAdapter;
import com.example.smartshopper.recyclerViews.CommentsViewHolder;
import com.example.smartshopper.services.RTDBService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommentInputDialog extends DialogFragment {
    List<Comment> comments;
    TextInputEditText commentInput;
    LocalStorage localStorage;
    int position;
    PlatformHelpers platformHelpers;
    Deal deal;
    CommentsAdapter adapter;
    CommentsAdapter parent;
    CommentsViewHolder holder;

    public CommentInputDialog(Deal deal, CommentsAdapter adapter){
        this.deal = deal;
        this.adapter = adapter;
    }

    public CommentInputDialog(Deal deal, List<Comment> comments,
                              CommentsViewHolder holder,
                              CommentsAdapter adapter, CommentsAdapter parent){
        this.comments = comments;
        this.deal = deal;
        this.adapter = adapter;
        this.position = holder.getAbsoluteAdapterPosition();
        this.parent = parent;
        this.holder = holder;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Get current logged in user;
        localStorage = new LocalStorage(requireContext());
        String currUser = localStorage.getCurrentUser();
        platformHelpers = new PlatformHelpers(getContext());

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Your Comment");
        // Get Layout inflater
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = layoutInflater.inflate(R.layout.comment_input_fragment, null);
        builder.setView(view);
        RTDBService rtdbService = new RTDBService();
        commentInput = view.findViewById(R.id.commentInput);

        builder.setPositiveButton("Submit", (dialog, which) -> {
            Comment newComment = new Comment(
                new User(currUser),
                Objects.requireNonNull(commentInput.getText()).toString(),
                System.currentTimeMillis()
            );

            if (this.comments == null) {
                rtdbService.writeComment(newComment, deal.getDealID());
                comments = adapter.getComments();
                comments.add(0, newComment);
            } else {
                Comment selectedComment = comments.get(position);
                comments.set(position, selectedComment);
                parent.notifyItemChanged(position);
                rtdbService.writeResponse(
                        selectedComment.getCommentID(),
                        deal.getDealID(),
                        newComment);

                parent.notifyItemChanged(position);
                holder.iv_toggleResponses.setVisibility(View.VISIBLE);
            }
            adapter.updateComments(comments);
        });

        builder.setNegativeButton("Close", (dialog, which) -> Objects.requireNonNull(
                CommentInputDialog.this.getDialog()).cancel()
        );

        // Create the AlertDialog object and return it
        return builder.create();
    }

}
