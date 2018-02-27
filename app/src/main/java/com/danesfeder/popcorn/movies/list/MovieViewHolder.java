package com.danesfeder.popcorn.movies.list;

import android.animation.ValueAnimator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.airbnb.lottie.LottieAnimationView;
import com.danesfeder.popcorn.R;
import com.squareup.picasso.Picasso;

class MovieViewHolder extends RecyclerView.ViewHolder {

  private ImageView movieImageView;
  private RatingBar movieRatingBar;
  private LottieAnimationView favoriteAnimationView;
  private ViewHolderClickListener listener;

  MovieViewHolder(View itemView, ViewHolderClickListener listener) {
    super(itemView);
    this.listener = listener;
    movieImageView = itemView.findViewById(R.id.iv_movie);
    movieRatingBar = itemView.findViewById(R.id.rb_movie_rating);
    favoriteAnimationView = itemView.findViewById(R.id.lav_favorite);
    initListeners();
  }

  void setMovieImage(String movieUrl) {
    Picasso.with(movieImageView.getContext())
      .load(movieUrl)
      .fit()
      .error(R.drawable.ic_error)
      .into(movieImageView);
  }

  void setMovieRating(float movieRating) {
    movieRatingBar.setRating(movieRating);
  }

  interface ViewHolderClickListener {
    void onViewHolderClick(int position);
    void onViewHolderFavorite(int position);
  }

  private void initListeners() {
    movieImageView.setOnClickListener(v -> listener.onViewHolderClick(getAdapterPosition()));
    favoriteAnimationView.setOnClickListener(v -> {
      animateFavoriteView();
      listener.onViewHolderFavorite(getAdapterPosition());
    });
  }

  private void animateFavoriteView() {
    ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(1000);
    animator.setInterpolator(new FastOutSlowInInterpolator());
    animator.addUpdateListener(valueAnimator -> favoriteAnimationView.setProgress((Float) valueAnimator.getAnimatedValue()));
    if (favoriteAnimationView.getProgress() == 0f) {
      animator.start();
    } else {
      favoriteAnimationView.setProgress(0f);
    }
  }
}
