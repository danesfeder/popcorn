package com.danesfeder.popcorn.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danesfeder.popcorn.R;
import com.danesfeder.popcorn.network.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

  private List<Movie> movieList;

  MovieAdapter() {
    movieList = new ArrayList<>();
  }

  @Override
  public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.movie_viewholder_layout, parent, false);
    return new MovieViewHolder(view);
  }

  @Override
  public void onBindViewHolder(MovieViewHolder holder, int position) {
    Movie movie = movieList.get(position);
    holder.setMovieImage(movie.getPosterUrl(holder.itemView.getContext()));
  }

  @Override
  public int getItemCount() {
    return movieList.size();
  }

  void updateMovieList(List<Movie> movies) {
    movieList.clear();
    movieList.addAll(movies);
    notifyDataSetChanged();
  }
}
