package com.example.smartshopper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;

import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.models.Comment;
import com.example.smartshopper.models.Deal;
import com.example.smartshopper.models.User;
import com.example.smartshopper.recyclerViews.CommentsAdapter;
import com.example.smartshopper.services.RTDBService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class DealDetailsActivity extends AppCompatActivity {
    PlatformHelpers platformHelpers;
    ImageView iv_deal_img;
    FloatingActionButton btn_AddComment;
    TextInputEditText commentInput;
    RecyclerView rv_comments;
    CommentsAdapter adapter;
    Deal deal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_details);

        platformHelpers = new PlatformHelpers(this);
        iv_deal_img = findViewById(R.id.iv_deal);
        btn_AddComment = findViewById(R.id.btn_add_comment);
        commentInput = findViewById(R.id.commentInput);

        if (getIntent().getExtras() != null) {
            deal = (Deal) getIntent().getSerializableExtra("dealItem");

            adapter = new CommentsAdapter(this);
            rv_comments = findViewById(R.id.rv_comments);
            rv_comments.setLayoutManager(new LinearLayoutManager(this));
            rv_comments.setAdapter(adapter);
            platformHelpers.getCommentsAndUpdateRv(deal, adapter);

            //  This works, but maybe a better way is to pass a picasso obj.
            PlatformHelpers.loadPicassoImg(this,
                    deal.getProductImg(),
                    iv_deal_img,
                    R.drawable.ic_baseline_shopping_basket_24);
        }


        btn_AddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RTDBService rtdbService = new RTDBService();

                Comment comment = new Comment(
                        new User("JaeAndDan"),
                        commentInput.getText().toString(),
                        System.currentTimeMillis());
                commentInput.setText("");
                commentInput.onEditorAction(EditorInfo.IME_ACTION_DONE);

                rtdbService.writeComment(comment, deal.getDealID());

                List<Comment> comments = adapter.getComments();
                comments.add(0, comment);
                adapter.updateComments(comments);
                rv_comments.scrollToPosition(0);
            }


        });
    }
}