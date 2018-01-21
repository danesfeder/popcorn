package com.danesfeder.popcorn.list;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter {

  private List<MovieInfo> movieInfoList;

  MovieAdapter() {
    movieInfoList = new ArrayList<>();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    return null;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return movieInfoList.size();
  }

  public void resetMovieList(List<MovieInfo> movies) {
    movieInfoList.clear();
    movieInfoList.addAll(movies);
    notifyDataSetChanged();
  }
}
