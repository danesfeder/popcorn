package com.danesfeder.popcorn.movies.favorite;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.lang.ref.WeakReference;

import static android.content.ContentValues.TAG;

class FavoritesAsyncLoader extends AsyncTaskLoader<Cursor> {

  private Cursor taskData = null;
  private WeakReference<Context> contextWeakReference;

  FavoritesAsyncLoader(Context context) {
    super(context);
    contextWeakReference = new WeakReference<>(context);
  }

  @Override
  protected void onStartLoading() {
    if (taskData != null) {
      deliverResult(taskData);
    } else {
      forceLoad();
    }
  }

  @Override
  public Cursor loadInBackground() {
    try {
      ContentResolver contentResolver = contextWeakReference.get().getContentResolver();
      return contentResolver.query(FavoriteContract.FavoriteEntry.CONTENT_URI,
        null,
        null,
        null,
        FavoriteContract.FavoriteEntry._ID);
    } catch (Exception exception) {
      Log.e(TAG, "Failed to asynchronously load data.");
      exception.printStackTrace();
      return null;
    }
  }

  public void deliverResult(Cursor data) {
    taskData = data;
    super.deliverResult(data);
  }
}
