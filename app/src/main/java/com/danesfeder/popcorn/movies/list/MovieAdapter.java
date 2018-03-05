package com.danesfeder.popcorn.movies.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danesfeder.popcorn.R;
import com.danesfeder.popcorn.movies.list.network.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> implements
  MovieViewHolder.ViewHolderClickListener {

  private List<Movie> movieList;
  private MovieClickListener listener;

  MovieAdapter(MovieClickListener listener) {
    movieList = new ArrayList<>();
    this.listener = listener;
  }

  @Override
  public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.movie_viewholder_layout, parent, false);
    return new MovieViewHolder(view, this);
  }

  @Override
  public void onBindViewHolder(MovieViewHolder holder, int position) {
    Movie movie = movieList.get(position);
    holder.setMovieImage(movie.getPosterUrl(holder.itemView.getContext()));
    holder.setMovieRating(movie.getRating());
  }

  @Override
  public int getItemCount() {
    return movieList.size();
  }

  @Override
  public void onViewHolderClick(int position) {
    Movie clickedMovie = movieList.get(position);
    listener.onMovieClick(clickedMovie);
  }

  @Override
  public void onViewHolderFavorite(int position) {
    Movie favoriteMovie = movieList.get(position);
    listener.onMovieFavorite(favoriteMovie);
  }

  @Override
  public void onViewHolderFavoriteRemoved(int position) {
    Movie favoriteMovieRemoved = movieList.get(position);
    listener.onMovieFavoriteRemoved(favoriteMovieRemoved);
  }

  void updateMovieList(List<Movie> movies) {
    movieList.clear();
    movieList.addAll(movies);
    notifyDataSetChanged();
  }
}
