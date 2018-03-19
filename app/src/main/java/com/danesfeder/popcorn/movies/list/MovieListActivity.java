package com.danesfeder.popcorn.movies.list;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.danesfeder.popcorn.R;
import com.danesfeder.popcorn.movies.detail.MovieDetailActivity;
import com.danesfeder.popcorn.movies.favorite.FavoriteDbHelper;
import com.danesfeder.popcorn.movies.favorite.FavoriteUtils;
import com.danesfeder.popcorn.movies.favorite.MovieFavoritesActivity;
import com.danesfeder.popcorn.movies.network.NetworkUtils;

import java.util.List;

public class MovieListActivity extends AppCompatActivity implements FetchMoviesTask.MoviesLoadedListener,
  MovieClickListener {

  private RecyclerView rvMovies;
  private SwipeRefreshLayout refreshLayout;
  private ProgressBar progressBar;
  private BottomNavigationView navigation;
  private MovieAdapter movieAdapter;

  private int taskType = FetchMoviesTask.POPULAR;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_display_movies);
    init(savedInstanceState != null);
  }

  @Override
  protected void onResume() {
    super.onResume();
    refreshNavigationView();
  }

  @Override
  protected void onPause() {
    super.onPause();
    saveSharedPreferences();
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    movieAdapter.updateMovieList(savedInstanceState.getParcelableArrayList(getString(R.string.current_movie_list)));
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelableArrayList(getString(R.string.current_movie_list), movieAdapter.getMovieList());
  }

  @Override
  public void onMoviesLoaded(List<Movie> movies) {
    // Hide main progress bar
    if (progressBar.getVisibility() == View.VISIBLE) {
      progressBar.setVisibility(View.INVISIBLE);
    }
    // Hide refresh layout if refreshing
    if (refreshLayout.isRefreshing()) {
      refreshLayout.setRefreshing(false);
    }
    // Update the adapter with the new movies
    movieAdapter.updateMovieList(movies);
  }

  @Override
  public void onMovieClick(Movie clickedMovie) {
    launchDetailActivity(clickedMovie);
  }

  @Override
  public void onMovieFavorite(Movie favoriteMovie) {
    FavoriteDbHelper.insertFavoriteMovie(this, favoriteMovie);
    FavoriteUtils.updateFavoriteMovie(this, favoriteMovie);
  }

  @Override
  public void onMovieFavoriteRemoved(Movie favoriteMovieRemoved) {
    FavoriteDbHelper.deleteFavoriteMovie(this, favoriteMovieRemoved);
  }

  private void init(boolean isConfigurationChange) {
    bind();
    initRecyclerView();
    initListeners();
    extractSharedPreferences();

    if (!isConfigurationChange) {
      fetchMovies(taskType);
    }
  }

  /**
   * Binds all views needed and sets them to appropriate fields.
   */
  private void bind() {
    rvMovies = findViewById(R.id.rv_movies);
    refreshLayout = findViewById(R.id.refresh_layout);
    progressBar = findViewById(R.id.pb_content_loading);
    navigation = findViewById(R.id.navigation);
  }

  /**
   * Sets up the RecyclerView that will be used to
   * display the movies.
   */
  private void initRecyclerView() {
    movieAdapter = new MovieAdapter(this);
    rvMovies.setAdapter(movieAdapter);
    rvMovies.setHasFixedSize(true);
    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
      rvMovies.setLayoutManager(new GridLayoutManager(this, 2));
    } else {
      rvMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }
  }

  /**
   * Sets up all listeners needed for the activity UI.
   */
  private void initListeners() {
    refreshLayout.setOnRefreshListener(refreshListener);
    navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
  }

  /**
   * Looks at {@link SharedPreferences} and extracts any configuration data.
   * If none is found, provides defaults for the first app start.
   */
  private void extractSharedPreferences() {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    taskType = preferences.getInt(getString(R.string.preference_task_type), FetchMoviesTask.POPULAR);
    navigation.setSelectedItemId(preferences.getInt(getString(R.string.preference_navigation_position), 0));
  }

  private void refreshNavigationView() {
    switch (taskType) {
      case FetchMoviesTask.POPULAR:
        navigation.setSelectedItemId(R.id.navigation_popular);
        break;
      case FetchMoviesTask.TOP_RATED:
        navigation.setSelectedItemId(R.id.navigation_rating);
        break;
    }
  }

  /**
   * Saves the current task type and selected navigation item to
   * restore next time the application is started.
   */
  private void saveSharedPreferences() {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    SharedPreferences.Editor editor = preferences.edit();
    editor.putInt(getString(R.string.preference_task_type), taskType);
    editor.putInt(getString(R.string.preference_navigation_position), navigation.getSelectedItemId());
    editor.apply();
  }

  private void launchDetailActivity(Movie clickedMovie) {
    Intent movieDetails = new Intent(this, MovieDetailActivity.class);
    movieDetails.putExtra(getString(R.string.movie_detail_extra), clickedMovie);
    startActivity(movieDetails);
  }

  private void launchFavoritesActivity() {
    Intent movieFavorites = new Intent(this, MovieFavoritesActivity.class);
    startActivity(movieFavorites);
  }

  /**
   * Fetches a current list of movies based on the task type provided.
   * <p>
   * Returns a successful list to the {@link FetchMoviesTask.MoviesLoadedListener} implemented
   * by this class.
   */
  private void fetchMovies(int taskType) {
    // Check for an internet connection
    if (NetworkUtils.checkInternetConnection(this)) {
      // Show loading (don't show if from refresh)
      if (!refreshLayout.isRefreshing()) {
        progressBar.setVisibility(View.VISIBLE);
      }
      // Scroll rv to the top of the list
      rvMovies.smoothScrollToPosition(0);
      // Fetch popular movies
      this.taskType = taskType;
      new FetchMoviesTask(taskType, this).execute();
    }
  }

  private final SwipeRefreshLayout.OnRefreshListener refreshListener
    = () -> fetchMovies(taskType);

  private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener
    = item -> {
    switch (item.getItemId()) {
      case R.id.navigation_popular:
        fetchMovies(FetchMoviesTask.POPULAR);
        return true;
      case R.id.navigation_rating:
        fetchMovies(FetchMoviesTask.TOP_RATED);
        return true;
      case R.id.navigation_favorites:
        launchFavoritesActivity();
        return true;
    }
    return false;
  };
}
