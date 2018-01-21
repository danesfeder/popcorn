package com.danesfeder.popcorn.network;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.google.gson.annotations.SerializedName;

public class Movie {

  public static final String LOG_TAG = Movie.class.getSimpleName();

  @SerializedName("id")
  private long id;
  @SerializedName("original_title")
  private String title;
  @SerializedName("poster_path")
  private String posterUrl;
  @SerializedName("overview")
  private String overview;
  @SerializedName("vote_average")
  private String rating;
  @SerializedName("release_date")
  private String releaseDate;
  @SerializedName("backdrop_path")
  private String backdropUrl;

  public Movie(long id, String title, String posterUrl, String overview, String rating,
               String releaseDate, String backdropUrl) {
    this.id = id;
    this.title = title;
    this.posterUrl = posterUrl;
    this.overview = overview;
    this.rating = rating;
    this.releaseDate = releaseDate;
    this.backdropUrl = backdropUrl;
  }

  public long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  @Nullable
  public String getPosterUrl(Context context) {
    return buildUrl(posterUrl, context);
  }

  public String getOverview() {
    return overview;
  }

  public String getRating() {
    return rating;
  }

  public String getReleaseDate() {
    return releaseDate;
  }

  @Nullable
  public String getBackdropUrl(Context context) {
    return buildUrl(backdropUrl, context);
  }

  private String buildUrl(String url, Context context) {
    // Set the image width based on screen density
    String imageSize = detectImageSize(context);

    if (!TextUtils.isEmpty(url)) {
      return NetworkConstants.BASE_IMAGE_URL.concat(imageSize).concat(url);
    } else {
      return null;
    }
  }

  private String detectImageSize(Context context) {
    int densityDpi = context.getResources().getDisplayMetrics().densityDpi;
    switch (densityDpi) {
      case DisplayMetrics.DENSITY_LOW:
        return "w154";
      case DisplayMetrics.DENSITY_MEDIUM:
        return "w185";
      case DisplayMetrics.DENSITY_HIGH:
        return "w342";
      case DisplayMetrics.DENSITY_XHIGH:
        return "w500";
      case DisplayMetrics.DENSITY_XXHIGH:
        return "w780";
      case DisplayMetrics.DENSITY_XXXHIGH:
        return "original";
      default:
        return "w500";
    }
  }
}
