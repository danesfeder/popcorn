package com.danesfeder.popcorn.movies.favorite;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.danesfeder.popcorn.R;
import com.danesfeder.popcorn.movies.favorite.list.FavoriteAdapter;
import com.danesfeder.popcorn.movies.favorite.list.FavoriteClickListener;

public class MovieFavoritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
  FavoriteClickListener {

  public static final int FAVORITES_LOADER_ID = 0;
  private FavoriteAdapter favoriteAdapter;
  private TextView noFavoritesTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_favorites);

    noFavoritesTextView = findViewById(R.id.tv_no_favorites);
    RecyclerView rvFavorites = findViewById(R.id.rv_favorites);
    initFavoritesRecyclerView(rvFavorites);

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
    if (data.getCount() > 0) {
      noFavoritesTextView.setVisibility(View.INVISIBLE);
    }
    favoriteAdapter.updateFavorites(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    // Remove old data being reset
  }

  @Override
  public void onFavoriteMovieClick(int favoriteMoviePosition) {

  }

  private void initFavoritesRecyclerView(RecyclerView rvFavorites) {
    rvFavorites.setLayoutManager(new LinearLayoutManager(this));
    favoriteAdapter = new FavoriteAdapter(this);
    rvFavorites.setAdapter(favoriteAdapter);
  }
}
