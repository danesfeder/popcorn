package com.danesfeder.popcorn.movies.favorite;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.danesfeder.popcorn.movies.list.Movie;

public class FavoriteUtils {

  public static void updateFavoriteMovie(Context context, Movie favoriteMovie) {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    SharedPreferences.Editor editor = preferences.edit();
    if (preferences.getBoolean(favoriteMovie.getTitle(), false)) {
      editor.putBoolean(favoriteMovie.getTitle(), false);
    } else {
      editor.putBoolean(favoriteMovie.getTitle(), true);
    }
    editor.apply();
  }
}
