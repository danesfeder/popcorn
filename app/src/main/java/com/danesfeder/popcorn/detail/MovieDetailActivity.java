package com.danesfeder.popcorn.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.danesfeder.popcorn.R;
import com.danesfeder.popcorn.network.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

  private ImageView moviePosterImageView;
  private ImageView movieBackdropImageView;
  private TextView movieTitleTextView;
  private TextView movieOverviewTextView;
  private TextView movieReleaseDate;
  private RatingBar movieRatingBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_detail);
    bind();

    Movie movie = getIntent().getParcelableExtra(getString(R.string.movie_detail_extra));
    loadMovieImages(movie);
    setMovieDetails(movie);
  }

  private void setMovieDetails(Movie movie) {
    movieTitleTextView.setText(movie.getTitle());
    movieOverviewTextView.setText(movie.getOverview());
    movieReleaseDate.setText(movie.getReleaseDate());
    movieRatingBar.setRating(movie.getRating());
  }

  private void bind() {
    moviePosterImageView = findViewById(R.id.iv_movie_poster);
    movieBackdropImageView = findViewById(R.id.iv_movie_backdrop);
    movieTitleTextView = findViewById(R.id.tv_movie_title);
    movieOverviewTextView = findViewById(R.id.tv_movie_overview);
    movieReleaseDate = findViewById(R.id.tv_movie_release_date);
    movieRatingBar = findViewById(R.id.rb_movie_rating);
  }

  private void loadMovieImages(Movie movie) {
    Picasso.with(this).load(movie.getPosterUrl(this))
      .fit()
      .centerCrop()
      .error(R.drawable.ic_error)
      .into(moviePosterImageView);

    Picasso.with(this).load(movie.getBackdropUrl(this))
      .fit()
      .centerCrop()
      .error(R.drawable.ic_error)
      .into(movieBackdropImageView);
  }
}
