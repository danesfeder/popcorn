package com.danesfeder.popcorn.movies.detail.reviews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danesfeder.popcorn.R;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

  private List<Review> reviewList = new ArrayList<>();

  @Override
  public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.review_viewholder_layout, parent, false);
    return new ReviewViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ReviewViewHolder holder, int position) {
    Review review = reviewList.get(position);
    holder.setAuthorText(review.getAuthor());
    holder.setContentText(review.getContent());
  }

  @Override
  public int getItemCount() {
    return reviewList.size();
  }

  public void updateReviewList(List<Review> reviews) {
    reviewList.clear();
    reviewList.addAll(reviews);
    notifyDataSetChanged();
  }
}
