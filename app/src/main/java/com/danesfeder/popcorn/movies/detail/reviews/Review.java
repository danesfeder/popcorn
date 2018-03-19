package com.danesfeder.popcorn.movies.detail.reviews;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public final class Review implements Parcelable {

  @Expose
  private String id;
  @Expose
  private String author;
  @Expose
  private String content;
  @Expose
  private String url;
  private boolean isExpanded;

  public Review() {
  }

  public String getId() {
    return id;
  }

  public Review setId(String id) {
    this.id = id;
    return this;
  }

  public String getAuthor() {
    return author;
  }

  public Review setAuthor(String author) {
    this.author = author;
    return this;
  }

  public String getContent() {
    return content;
  }

  public Review setContent(String content) {
    this.content = content;
    return this;
  }

  public String getUrl() {
    return url;
  }

  public Review setUrl(String url) {
    this.url = url;
    return this;
  }

  public boolean isExpanded() {
    return isExpanded;
  }

  public void setExpanded(boolean expanded) {
    isExpanded = expanded;
  }

  protected Review(Parcel in) {
    id = in.readString();
    author = in.readString();
    content = in.readString();
    url = in.readString();
    isExpanded = in.readByte() != 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(author);
    dest.writeString(content);
    dest.writeString(url);
    dest.writeByte((byte) (isExpanded ? 1 : 0));
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<Review> CREATOR = new Creator<Review>() {
    @Override
    public Review createFromParcel(Parcel in) {
      return new Review(in);
    }

    @Override
    public Review[] newArray(int size) {
      return new Review[size];
    }
  };
}