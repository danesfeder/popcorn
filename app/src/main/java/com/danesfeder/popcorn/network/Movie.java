package com.danesfeder.popcorn.network;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {

  public static final String LOG_TAG = Movie.class.getSimpleName();
  public static final Creator<Movie> CREATOR = new Creator<Movie>() {
    @Override
    public Movie createFromParcel(Parcel in) {
      return new Movie(in);
    }

    @Override
    public Movie[] newArray(int size) {
      return new Movie[size];
    }
  };
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

  // Parcelable implementation
  private Movie(Parcel in) {
    id = in.readLong();
    title = in.readString();
    posterUrl = in.readString();
    overview = in.readString();
    rating = in.readString();
    releaseDate = in.readString();
    backdropUrl = in.readString();
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

  public float getRating() {
    return (Float.valueOf(rating) / 10) * 5;
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

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(id);
    dest.writeString(title);
    dest.writeString(posterUrl);
    dest.writeString(overview);
    dest.writeString(rating);
    dest.writeString(releaseDate);
    dest.writeString(backdropUrl);
  }

  @Override
  public int describeContents() {
    return 0;
  }
}
