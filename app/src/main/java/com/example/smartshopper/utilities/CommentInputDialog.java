package com.example.smartshopper.utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import com.example.smartshopper.services.RTDBService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;

public class CommentInputDialog extends DialogFragment {
    Comment comment;
    TextInputEditText commentInput;
    LocalStorage localStorage;
    PlatformHelpers platformHelpers;
    Deal deal;
    CommentsAdapter adapter;

    public CommentInputDialog(Deal deal, CommentsAdapter adapter){
        this.deal = deal;
        this.adapter = adapter;
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
            comment = new Comment(
                new User(currUser),
                Objects.requireNonNull(commentInput.getText()).toString(),
                System.currentTimeMillis()
            );

            rtdbService.writeComment(comment, deal.getDealID());
            List<Comment> comments = adapter.getComments();
            comments.add(0, comment);
            adapter.updateComments(comments);
            Objects.requireNonNull(CommentInputDialog.this.getDialog()).cancel();

        });

        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Objects.requireNonNull(CommentInputDialog.this.getDialog()).cancel();
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }

}
