package com.danesfeder.popcorn.movies.favorite;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteContract {

  private FavoriteContract() {
    // No instance
  }

  public static final String AUTHORITY = "com.danesfeder.popcorn.movies.favorite.data";
  static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

  public static final String PATH_FAVORITES = "favorites";

  public static final class FavoriteEntry implements BaseColumns {

    // FavoriteEntry content URI = base content URI + path
    public static final Uri CONTENT_URI =
      BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

    public static final String TABLE_NAME = "favorites";

    // Base columns adds ID automatically
    // Favorite columns include all fields from Movie.java
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_POSTER_URL = "poster_url";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_BACKDROP_URL = "backdrop_url";
    public static final String COLUMN_RELEASE_DATE = "release_date";
    public static final String COLUMN_RATING = "rating";
  }
}
