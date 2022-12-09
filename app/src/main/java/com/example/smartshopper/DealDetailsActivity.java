package com.example.smartshopper;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.models.Comment;
import com.example.smartshopper.models.Deal;
import com.example.smartshopper.recyclerViews.CommentsAdapter;
import com.example.smartshopper.utilities.CommentInputDialog;
import com.example.smartshopper.utilities.LocalStorage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;


public class DealDetailsActivity extends MenuActivity {
    PlatformHelpers platformHelpers;
    ImageView iv_deal_img;
    TextView tv_dealTitle;
    TextView tv_salePrice;
    TextView tv_numUpvotes;
    TextView tv_numDownvotes;
    TextView tv_productDescription;
    ImageView iv_downVote;
    ImageView iv_upVote;
    SearchView sv_searchBar;
    FloatingActionButton btn_AddComment;
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
        sv_searchBar = findViewById(R.id.sv_searchBar);
        tv_productDescription = findViewById(R.id.tv_productDescription);

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

        // Search for user posts
        sv_searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String textInput) {
                if (deal.getComments() != null) {
                    final List<Comment> filteredList = new ArrayList<>();
                    Collection<Comment> comments = deal.getComments().values();
                    for (Comment comment : comments) {
                        String userName = comment.getAuthor().getUsername().toLowerCase();

                        if (userName.contains(textInput)) {
                            filteredList.add(comment);
                        }
                    }

                    adapter.updateComments(filteredList);
                }
                return false;
            }
        });


        // BUTTONS For Activity
        btn_AddComment.setOnClickListener(v -> {
            CommentInputDialog commentFrag = new CommentInputDialog(deal, adapter);
            commentFrag.show(getSupportFragmentManager(), "comment_dialog");
        });

        iv_upVote.setOnClickListener(v -> {
            platformHelpers.upVoteDeal(deal.getDealID(), platformHelpers.getCurrentUserID(), platformHelpers.getCurrentUser());
        });

        iv_downVote.setOnClickListener(v -> {
            // Send vote results to db after voting.
            platformHelpers.downVoteDeal(deal.getDealID(), platformHelpers.getCurrentUserID(), platformHelpers.getCurrentUser());
            });
    }

    public void setDealDetails(Deal data){
        // UP VOTES
        platformHelpers.getNumUpVotesAndUpdateDeal(deal.getDealID(), response -> {
            tv_numUpvotes.setText(String.valueOf(response));
        });

        // DOWN VOTES
        platformHelpers.getNumDownVotesAndUpdateDeal(deal.getDealID(), response -> {
                    tv_numDownvotes.setText(String.valueOf(response));
                });

        tv_salePrice.setText(
                String.format(Locale.US, "$%.2f", data.getSalePrice())
        );
        tv_dealTitle.setText(data.getTitle());

        tv_productDescription.setText(data.getDescription());
    }

    public void sendToCreateAccountActivity(View view) {
        Intent createAccountIntent = new Intent(this, CreateAccountActivity.class);
        startActivity(createAccountIntent);
    }
}