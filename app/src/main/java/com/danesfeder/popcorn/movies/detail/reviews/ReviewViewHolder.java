package com.danesfeder.popcorn.movies.detail.reviews;

import android.content.Context;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.danesfeder.popcorn.R;

class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

  private ConstraintLayout itemView;
  private TextView authorTextView;
  private TextView contentTextView;
  private ImageView arrowImageView;
  private ReviewClickListener listener;
  private boolean isExpanded;

  ReviewViewHolder(View itemView, ReviewClickListener listener) {
    super(itemView);
    this.itemView = (ConstraintLayout) itemView;
    this.listener = listener;
    authorTextView = itemView.findViewById(R.id.tv_author);
    contentTextView = itemView.findViewById(R.id.tv_content);
    arrowImageView = itemView.findViewById(R.id.iv_arrow);
    itemView.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    listener.onReviewClick(getAdapterPosition());
    isExpanded = isExpanded ? collapseView() : expandView();
  }

  void setAuthorText(String author) {
    authorTextView.setText(author);
  }

  void setContentText(String content) {
    contentTextView.setText(content);
  }

  void setExpanded(boolean isExpanded) {
    this.isExpanded = isExpanded;
    if (isExpanded) {
      expandView();
    } else {
      collapseView();
    }
  }

  private boolean expandView() {
    TransitionManager.beginDelayedTransition(itemView);
    updateViewHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    contentTextView.setMaxLines(Integer.MAX_VALUE);
    contentTextView.setEllipsize(null);
    updateArrow(R.drawable.ic_arrow_drop_down);
    updateConstraintSet(itemView.getContext(), R.layout.review_viewholder_layout_alt);
    return true;
  }

  private boolean collapseView() {
    TransitionManager.beginDelayedTransition(itemView);
    Resources resources = itemView.getContext().getResources();
    int height = resources.getDimensionPixelSize(R.dimen.review_viewholder_collapsed_height);
    updateViewHeight(height);
    contentTextView.setMaxLines(1);
    contentTextView.setEllipsize(TextUtils.TruncateAt.END);
    updateArrow(R.drawable.ic_arrow_drop_up);
    updateConstraintSet(itemView.getContext(), R.layout.review_viewholder_layout);
    return false;
  }

  private void updateArrow(int resId) {
    arrowImageView.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), resId));
  }

  private void updateViewHeight(int height) {
    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
    params.height = height;
    itemView.setLayoutParams(params);
  }

  private void updateConstraintSet(Context context, int resId) {
    ConstraintSet constraintSet = new ConstraintSet();
    constraintSet.clone(context, resId);
    constraintSet.applyTo(itemView);
  }
}
