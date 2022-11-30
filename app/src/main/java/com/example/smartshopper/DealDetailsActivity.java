package com.example.smartshopper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.models.Deal;
import com.example.smartshopper.recyclerViews.CommentsAdapter;
import com.example.smartshopper.utilities.CommentInputDialog;
import com.example.smartshopper.utilities.LocalStorage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

public class DealDetailsActivity extends AppCompatActivity {
    PlatformHelpers platformHelpers;
    ImageView iv_deal_img;
    TextView tv_dealTitle;
    TextView tv_salePrice;
    TextView tv_numUpvotes;
    TextView tv_numDownvotes;
    ImageView iv_downVote;
    ImageView iv_upVote;
    FloatingActionButton btn_AddComment;
    TextInputEditText commentInput;
    RecyclerView rv_comments;
    CommentsAdapter adapter;
    Deal deal;
    LocalStorage localStorage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_details);

        localStorage = new LocalStorage(this);
        // Find views
        iv_deal_img = findViewById(R.id.iv_deal);
        btn_AddComment = findViewById(R.id.btn_add_comment);
        tv_dealTitle = findViewById(R.id.tv_dealTitle);
        tv_salePrice = findViewById(R.id.tv_salePrice);
        tv_numUpvotes = findViewById(R.id.tv_numUpvotes);
        tv_numDownvotes = findViewById(R.id.tv_numDownVotes);
        iv_upVote = findViewById(R.id.iv_upVote);
        iv_downVote = findViewById(R.id.iv_downVote);

        if(!localStorage.userIsLoggedIn()){
            btn_AddComment.setVisibility(View.GONE);
        }


        platformHelpers = new PlatformHelpers(this);

        // Set view for current deal
        if (getIntent().getExtras() != null) {
            deal = (Deal) getIntent().getSerializableExtra("dealItem");
            adapter = new CommentsAdapter(this, deal);

            rv_comments = findViewById(R.id.rv_comments);
            rv_comments.setLayoutManager(new LinearLayoutManager(this));
            rv_comments.setAdapter(adapter);
            platformHelpers.getCommentsAndUpdateRv(deal, adapter);

            setDealDetails(deal); // Set the deal details views;

            PlatformHelpers.loadImg(this,
                    deal.getProductImg(),
                    iv_deal_img,
                    R.drawable.ic_baseline_shopping_basket_large);


        }


        // BUTTONS For Activity
        btn_AddComment.setOnClickListener(v -> {
            CommentInputDialog commentFrag = new CommentInputDialog(deal, adapter);
            commentFrag.show(getSupportFragmentManager(), "comment_dialog");
        });

        iv_upVote.setOnClickListener(v -> {
            platformHelpers.upVoteDeal(deal.getDealID(), platformHelpers.getCurrentUserID(), platformHelpers.getCurrentUser());
            deal.upvote();
            tv_numUpvotes.setText(String.format(Locale.US,"%d", deal.getNumUpVotes()));
        });

        iv_downVote.setOnClickListener(v -> {
            platformHelpers.downVoteDeal(deal.getDealID(), platformHelpers.getCurrentUserID(), platformHelpers.getCurrentUser());
            deal.downvote();
            tv_numDownvotes.setText(String.format(Locale.US,"%d", deal.getNumDownVotes()));
        });
    }

    public void setDealDetails(Deal data){
        tv_numDownvotes.setText(String.format(Locale.US, "%d", data.getNumDownVotes()));
        tv_numUpvotes.setText(String.format(Locale.US, "%d", data.getNumUpVotes()));
        tv_salePrice.setText(
                String.format(Locale.US, "$%.2f", data.getSalePrice())
        );
        tv_dealTitle.setText(data.getTitle());
    }
}