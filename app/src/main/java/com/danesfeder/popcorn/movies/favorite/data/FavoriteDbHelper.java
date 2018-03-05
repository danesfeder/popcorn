package com.danesfeder.popcorn.movies.favorite.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteDbHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "favoritesDb.db";

  private static final int VERSION = 2;

  public FavoriteDbHelper(Context context) {
    super(context, DATABASE_NAME, null, VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    final String CREATE_TABLE = "CREATE TABLE "          + FavoriteContract.FavoriteEntry.TABLE_NAME + " (" +
      FavoriteContract.FavoriteEntry._ID                 + " INTEGER PRIMARY KEY, " +
      FavoriteContract.FavoriteEntry.COLUMN_ID           + " LONG NOT NULL, " +
      FavoriteContract.FavoriteEntry.COLUMN_TITLE        + " TEXT NOT NULL, " +
      FavoriteContract.FavoriteEntry.COLUMN_POSTER_URL   + " TEXT NOT NULL, " +
      FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW     + " TEXT NOT NULL, " +
      FavoriteContract.FavoriteEntry.COLUMN_BACKDROP_URL + " TEXT NOT NULL, " +
      FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
      FavoriteContract.FavoriteEntry.COLUMN_RATING       + " FLOAT NOT NULL);";
    db.execSQL(CREATE_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.FavoriteEntry.TABLE_NAME);
    onCreate(db);
  }
}
