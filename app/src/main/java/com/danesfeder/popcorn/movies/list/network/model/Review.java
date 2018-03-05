package com.danesfeder.popcorn.movies.list.network.model;

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

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.author);
    dest.writeString(this.content);
    dest.writeString(this.url);
  }

  protected Review(Parcel in) {
    this.id = in.readString();
    this.author = in.readString();
    this.content = in.readString();
    this.url = in.readString();
  }

  public static final Creator<Review> CREATOR = new Creator<Review>() {
    public Review createFromParcel(Parcel source) {
      return new Review(source);
    }

    public Review[] newArray(int size) {
      return new Review[size];
    }
  };
}