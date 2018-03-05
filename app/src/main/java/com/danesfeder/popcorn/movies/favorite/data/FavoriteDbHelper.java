package com.danesfeder.popcorn.movies.favorite.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.widget.Toast;

import com.danesfeder.popcorn.movies.list.network.model.Movie;

public class FavoriteDbHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "favoritesDb.db";

  private static final int VERSION = 2;

  public FavoriteDbHelper(Context context) {
    super(context, DATABASE_NAME, null, VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    final String CREATE_TABLE = "CREATE TABLE " + FavoriteContract.FavoriteEntry.TABLE_NAME + " (" +
      FavoriteContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY, " +
      FavoriteContract.FavoriteEntry.COLUMN_ID + " LONG NOT NULL, " +
      FavoriteContract.FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
      FavoriteContract.FavoriteEntry.COLUMN_POSTER_URL + " TEXT NOT NULL, " +
      FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
      FavoriteContract.FavoriteEntry.COLUMN_BACKDROP_URL + " TEXT NOT NULL, " +
      FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
      FavoriteContract.FavoriteEntry.COLUMN_RATING + " FLOAT NOT NULL);";
    db.execSQL(CREATE_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.FavoriteEntry.TABLE_NAME);
    onCreate(db);
  }

  public static void insertFavoriteMovie(Context context, Movie favoriteMovie) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_ID, favoriteMovie.getId());
    contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_TITLE, favoriteMovie.getTitle());
    contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW, favoriteMovie.getOverview());
    contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_BACKDROP_URL, favoriteMovie.getBackdropUrl(context));
    contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_POSTER_URL, favoriteMovie.getPosterUrl(context));
    contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_RATING, favoriteMovie.getRating());
    contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE, favoriteMovie.getReleaseDate());

    Uri uri = context.getContentResolver().insert(FavoriteContract.FavoriteEntry.CONTENT_URI, contentValues);
    if (uri != null) {
      Toast.makeText(context, uri.toString(), Toast.LENGTH_LONG).show();
    }
  }

  public static void deleteFavoriteMovie(Context context, Movie favoriteMovieRemoved) {
    String movieId = String.valueOf(favoriteMovieRemoved.getId());
    Uri uriDeleteMovie = FavoriteContract.FavoriteEntry.CONTENT_URI.buildUpon().appendPath(movieId).build();
    int result = context.getContentResolver().delete(uriDeleteMovie, null, null);
    if (result > 0) {
      Toast.makeText(context, "Successfully deleted: " + favoriteMovieRemoved.getTitle(), Toast.LENGTH_LONG).show();
    }
  }
}
