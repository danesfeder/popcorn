package com.danesfeder.popcorn.movies.favorite.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.danesfeder.popcorn.R;
import com.squareup.picasso.Picasso;

class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

  private ImageView favoritePosterImageView;
  private TextView favoriteTitleTextView;
  private TextView favoriteOverviewTextView;

  private ViewHolderClickListener listener;

  FavoriteViewHolder(View itemView, ViewHolderClickListener listener) {
    super(itemView);
    favoritePosterImageView = itemView.findViewById(R.id.iv_favorite_movie_poster);
    favoriteTitleTextView = itemView.findViewById(R.id.tv_favorite_title);
    favoriteOverviewTextView = itemView.findViewById(R.id.tv_favorite_overview);

    this.listener = listener;
    itemView.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    listener.onViewHolderClick(getAdapterPosition());
  }

  void setMovieImage(String movieUrl) {
    Picasso.with(favoriteOverviewTextView.getContext())
      .load(movieUrl)
      .fit()
      .error(R.drawable.ic_error)
      .into(favoritePosterImageView);
  }

  void setFavoriteTitleText(String favoriteTitleText) {
    favoriteTitleTextView.setText(favoriteTitleText);
  }

  void setFavoriteOverviewText(String favoriteOverviewText) {
    favoriteOverviewTextView.setText(favoriteOverviewText);
  }

  interface ViewHolderClickListener {

    void onViewHolderClick(int position);
  }
}
