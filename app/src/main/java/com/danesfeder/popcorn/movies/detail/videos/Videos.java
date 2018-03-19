package com.danesfeder.popcorn.movies.detail.videos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Videos {

  @SerializedName("results")
  private ArrayList<Video> videos = new ArrayList<>();

  public ArrayList<Video> getVideos() {
    return videos;
  }
}
