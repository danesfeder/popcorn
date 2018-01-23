package com.danesfeder.popcorn.network;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Movie implements Parcelable {

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

  public String getReleaseDate() {
    if (TextUtils.isEmpty(releaseDate)) {
      return "";
    }

    String inputPattern = "yyyy-MM-dd";
    SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.getDefault());
    Date date;
    try {
      date = inputFormat.parse(releaseDate);
      return DateFormat.getDateInstance().format(date);
    } catch (ParseException e) {
      e.printStackTrace();
      return releaseDate;
    }
  }

  public float getRating() {
    return (Float.valueOf(rating) / 10) * 5;
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
