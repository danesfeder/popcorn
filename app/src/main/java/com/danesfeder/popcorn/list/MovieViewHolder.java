package com.danesfeder.popcorn.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.danesfeder.popcorn.R;
import com.squareup.picasso.Picasso;

class MovieViewHolder extends RecyclerView.ViewHolder {

  private ImageView movieImageView;

  MovieViewHolder(View itemView) {
    super(itemView);
    movieImageView = itemView.findViewById(R.id.iv_movie);
  }

  void setMovieImage(String movieUrl) {
    Picasso.with(movieImageView.getContext())
      .load(movieUrl)
      .fit()
      .centerCrop()
      .error(R.drawable.ic_error)
      .into(movieImageView);
  }
}
