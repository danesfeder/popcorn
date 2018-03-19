package com.danesfeder.popcorn.movies.detail.reviews;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Reviews {

  @SerializedName("results")
  private ArrayList<Review> reviews = new ArrayList<>();

  public ArrayList<Review> getReviews() {
    return reviews;
  }
}
