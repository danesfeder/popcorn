package com.danesfeder.popcorn.list;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.danesfeder.popcorn.R;
import com.danesfeder.popcorn.detail.MovieDetailActivity;
import com.danesfeder.popcorn.network.FetchMoviesTask;
import com.danesfeder.popcorn.network.Movie;

import java.util.List;

public class MovieListActivity extends AppCompatActivity implements FetchMoviesTask.MoviesLoadedListener,
  MovieClickListener {

  private RecyclerView rvMovies;
  private SwipeRefreshLayout refreshLayout;
  private ProgressBar progressBar;
  private BottomNavigationView navigation;
  private MovieAdapter movieAdapter;

  private int taskType = FetchMoviesTask.POPULAR;
  private final SwipeRefreshLayout.OnRefreshListener refreshListener
    = new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
      fetchMovies(taskType);
    }
  };
  private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener
    = new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()) {
        case R.id.navigation_popular:
          fetchMovies(FetchMoviesTask.POPULAR);
          return true;
        case R.id.navigation_rating:
          fetchMovies(FetchMoviesTask.TOP_RATED);
          return true;
      }
      return false;
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_display_movies);
    init();
  }

  @Override
  protected void onPause() {
    super.onPause();
    saveSharedPreferences();
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

  private void launchDetailActivity(Movie clickedMovie) {
    Intent movieDetails = new Intent(this, MovieDetailActivity.class);
    movieDetails.putExtra(getString(R.string.movie_detail_extra), clickedMovie);
    startActivity(movieDetails);

  }

  private void init() {
    bind();
    initRecyclerView();
    initListeners();
    extractSharedPreferences();
    fetchMovies(taskType);
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
    rvMovies.setLayoutManager(new GridLayoutManager(this, 2));
    rvMovies.setHasFixedSize(true);
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

  /**
   * Fetches a current list of movies based on the task type provided.
   * <p>
   * Returns a successful list to the {@link FetchMoviesTask.MoviesLoadedListener} implemented
   * by this class.
   */
  private void fetchMovies(int taskType) {
    // Show loading
    progressBar.setVisibility(View.VISIBLE);
    // Scroll rv to the top of the list
    rvMovies.smoothScrollToPosition(0);
    // Fetch popular movies
    this.taskType = taskType;
    new FetchMoviesTask(taskType, this).execute();
  }
}
