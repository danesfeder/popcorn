package com.danesfeder.popcorn.movies.favorite;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.danesfeder.popcorn.R;

public class MovieFavoritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

  public static final int FAVORITES_LOADER_ID = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_favorites);

    getSupportLoaderManager().initLoader(FAVORITES_LOADER_ID, null, this);
  }

  @Override
  protected void onResume() {
    super.onResume();

    getSupportLoaderManager().restartLoader(FAVORITES_LOADER_ID, null, this);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new FavoritesAsyncLoader(this);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    Log.d(MovieFavoritesActivity.class.getSimpleName(), "onLoadFinished!");
    logResults(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    // Remove old data being reset
  }

  private void logResults(Cursor data) {
    for (int i = 0; i < data.getCount(); i++) {
      int idIndex = data.getColumnIndex(FavoriteContract.FavoriteEntry._ID);
      int titleIndex = data.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TITLE);
      data.moveToPosition(i);
      final int id = data.getInt(idIndex);
      String title = data.getString(titleIndex);
      Log.d(MovieFavoritesActivity.class.getSimpleName(), "Movie Loaded: " + title + " ID: " + id);
    }
  }
}
