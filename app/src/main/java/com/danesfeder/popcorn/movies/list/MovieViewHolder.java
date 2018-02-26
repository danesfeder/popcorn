package com.danesfeder.popcorn.movies.list;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.danesfeder.popcorn.R;
import com.squareup.picasso.Picasso;

class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

  private ImageView movieImageView;
  private ViewHolderClickListener listener;

  MovieViewHolder(View itemView, ViewHolderClickListener listener) {
    super(itemView);
    this.listener = listener;
    movieImageView = itemView.findViewById(R.id.iv_movie);
    CardView movieCardView = itemView.findViewById(R.id.cv_movie);
    movieCardView.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    listener.onViewHolderClick(getAdapterPosition());
  }

  void setMovieImage(String movieUrl) {
    Picasso.with(movieImageView.getContext())
      .load(movieUrl)
      .fit()
      .error(R.drawable.ic_error)
      .into(movieImageView);
  }

  interface ViewHolderClickListener {
    void onViewHolderClick(int position);
  }
}
