package com.danesfeder.popcorn.movies.list;

import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
  private SharedPreferences preferences;

  MovieViewHolder(View itemView, ViewHolderClickListener listener) {
    super(itemView);
    this.listener = listener;
    movieImageView = itemView.findViewById(R.id.iv_movie);
    movieRatingBar = itemView.findViewById(R.id.rb_movie_rating);
    favoriteAnimationView = itemView.findViewById(R.id.lav_favorite);
    preferences = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
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

  void setupFavoriteAnimation(String movieTitle) {
    boolean isFavorite = preferences.getBoolean(movieTitle, false);
    favoriteAnimationView.setProgress(isFavorite ? 1f : 0f);
  }

  private void initListeners() {
    movieImageView.setOnClickListener(v -> listener.onViewHolderClick(getAdapterPosition()));
    favoriteAnimationView.setOnClickListener(v -> {
      animateFavoriteView();
    });
  }

  private void animateFavoriteView() {
    ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(1000);
    animator.setInterpolator(new FastOutSlowInInterpolator());
    animator.addUpdateListener(valueAnimator -> favoriteAnimationView.setProgress((Float) valueAnimator.getAnimatedValue()));
    if (favoriteAnimationView.getProgress() == 0f) {
      listener.onViewHolderFavorite(getAdapterPosition());
      animator.start();
    } else {
      listener.onViewHolderFavoriteRemoved(getAdapterPosition());
      favoriteAnimationView.setProgress(0f);
    }
  }

  interface ViewHolderClickListener {
    void onViewHolderClick(int position);

    void onViewHolderFavorite(int position);

    void onViewHolderFavoriteRemoved(int position);
  }
}
