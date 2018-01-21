package com.danesfeder.popcorn.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.danesfeder.popcorn.R;

public class MovieListActivity extends AppCompatActivity {

  private RecyclerView rvMovies;
  private SwipeRefreshLayout refreshLayout;
  private BottomNavigationView navigation;
  private MovieAdapter movieAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_display_movies);
    init();
  }

  private void init() {
    bind();
    initRecyclerView();
    initClickListeners();
  }

  /**
   * Binds all views needed and sets them to appropriate fields.
   */
  private void bind() {
    rvMovies = findViewById(R.id.rv_movies);
    refreshLayout = findViewById(R.id.refresh_layout);
    navigation = findViewById(R.id.navigation);
  }

  /**
   * Sets up the RecyclerView that will be used to
   * display the movies.
   */
  private void initRecyclerView() {
    movieAdapter = new MovieAdapter();
    rvMovies.setAdapter(movieAdapter);
    rvMovies.setLayoutManager(new GridLayoutManager(this, 2));
    rvMovies.setHasFixedSize(true);
  }

  private void initClickListeners() {
    refreshLayout.setOnRefreshListener(refreshListener);
    navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
  }

  private final SwipeRefreshLayout.OnRefreshListener refreshListener
    = new SwipeRefreshLayout.OnRefreshListener() {

    @Override
    public void onRefresh() {
      // TODO load movies again
    }
  };

  private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener
    = new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()) {
        case R.id.navigation_popular:
          return true;
        case R.id.navigation_rating:
          return true;
      }
      return false;
    }
  };
}
