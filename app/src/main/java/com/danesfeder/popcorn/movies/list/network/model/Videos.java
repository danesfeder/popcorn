package com.danesfeder.popcorn.movies.list.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Videos {

  @SerializedName("results")
  private List<Video> videos = new ArrayList<>();

  public List<Video> getVideos() {
    return videos;
  }
}
