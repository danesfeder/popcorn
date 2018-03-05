package com.danesfeder.popcorn.movies.favorite.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.danesfeder.popcorn.movies.favorite.data.FavoriteContract;
import com.danesfeder.popcorn.movies.favorite.data.FavoriteDbHelper;

import static com.danesfeder.popcorn.movies.favorite.data.FavoriteContract.FavoriteEntry.COLUMN_ID;
import static com.danesfeder.popcorn.movies.favorite.data.FavoriteContract.FavoriteEntry.CONTENT_URI;
import static com.danesfeder.popcorn.movies.favorite.data.FavoriteContract.FavoriteEntry.TABLE_NAME;

public class FavoriteContentProvider extends ContentProvider {

  private static final String TAG = FavoriteContentProvider.class.getSimpleName();

  public static final int FAVORITES = 100;
  public static final int FAVORITE_WITH_ID = 101;

  private static final UriMatcher uriMatcher = buildUriMatcher();

  public static UriMatcher buildUriMatcher() {

    UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    String authority = FavoriteContract.AUTHORITY;
    String pathFavorites = FavoriteContract.PATH_FAVORITES;

    uriMatcher.addURI(authority, pathFavorites, FAVORITES);
    uriMatcher.addURI(authority, pathFavorites + "/#", FAVORITE_WITH_ID);

    return uriMatcher;
  }

  private FavoriteDbHelper favoriteDbHelper;

  @Override
  public boolean onCreate() {
    Context context = getContext();
    favoriteDbHelper = new FavoriteDbHelper(context);
    return true;
  }


  @Override
  public Uri insert(@NonNull Uri uri, ContentValues values) {
    final SQLiteDatabase db = favoriteDbHelper.getWritableDatabase();

    if (checkMoviePresent(values, db)) {
      Log.e(TAG, "Movie has already been added to the favorites database");
      return null;
    }

    int match = uriMatcher.match(uri);
    Uri returnUri;

    switch (match) {
      case FAVORITES:
        long id = db.insert(TABLE_NAME, null, values);
        // Check if valid id, otherwise throw exception
        if (id > 0) {
          returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
        } else {
          throw new SQLException("Failed to insert row into " + uri);
        }
        break;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    if (getContext() != null) {
      getContext().getContentResolver().notifyChange(uri, null);
      return returnUri;
    }
    return null;
  }

  @Override
  public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                      String[] selectionArgs, String sortOrder) {
    final SQLiteDatabase db = favoriteDbHelper.getReadableDatabase();

    int match = uriMatcher.match(uri);
    Cursor cursor;

    switch (match) {
      case FAVORITES:
        cursor = db.query(TABLE_NAME,
          projection,
          selection,
          selectionArgs,
          null,
          null,
          sortOrder);
        break;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    if (getContext() != null) {
      cursor.setNotificationUri(getContext().getContentResolver(), uri);
      return cursor;
    }
    return null;
  }

  @Override
  public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

    final SQLiteDatabase db = favoriteDbHelper.getWritableDatabase();

    int match = uriMatcher.match(uri);
    int favoriteDeleted;

    switch (match) {
      case FAVORITE_WITH_ID:
        String id = uri.getPathSegments().get(1);
        favoriteDeleted = db.delete(TABLE_NAME, "id=?", new String[] {id});
        break;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    if (getContext() != null && favoriteDeleted != 0) {
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return favoriteDeleted;
  }


  @Override
  public int update(@NonNull Uri uri, ContentValues values, String selection,
                    String[] selectionArgs) {
    throw new UnsupportedOperationException("Not yet implemented");
  }


  @Override
  public String getType(@NonNull Uri uri) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  private boolean checkMoviePresent(ContentValues values, SQLiteDatabase db) {
    Cursor cursor = db.query(FavoriteContract.FavoriteEntry.TABLE_NAME, null, null,
      null, null, null, null);
    while (cursor.moveToNext()) {
      long movieId = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
      if (movieId == values.getAsLong(COLUMN_ID)) {
        cursor.close();
        return true;
      }
    }
    cursor.close();
    return false;
  }
}
