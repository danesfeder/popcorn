package com.danesfeder.popcorn.movies.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.danesfeder.popcorn.R;
import com.danesfeder.popcorn.movies.detail.reviews.FetchMovieReviewsTask;
import com.danesfeder.popcorn.movies.detail.reviews.ReviewAdapter;
import com.danesfeder.popcorn.movies.detail.videos.FetchMovieVideosTask;
import com.danesfeder.popcorn.movies.list.Movie;
import com.danesfeder.popcorn.movies.detail.reviews.Review;
import com.danesfeder.popcorn.movies.detail.videos.Video;
import com.danesfeder.popcorn.movies.network.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieDetailActivity extends AppCompatActivity
  implements FetchMovieReviewsTask.MovieReviewsLoadedListener,
  FetchMovieVideosTask.MovieVideosLoadedListener {

  private ImageView moviePosterImageView;
  private ImageView movieBackdropImageView;
  private TextView movieTitleTextView;
  private TextView movieOverviewTextView;
  private TextView movieReleaseDate;
  private RatingBar movieRatingBar;
  private RecyclerView reviewRecyclerView;
  private ReviewAdapter reviewAdapter;
  private ProgressBar reviewProgressBar;
  private FloatingActionButton videoPlayBtn;
  private ProgressBar videoProgressBar;
  private List<Video> videos;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_detail);
    bind();
    initReviewRecyclerView();
    initVideoClickListener();

    Movie movie = getIntent().getParcelableExtra(getString(R.string.movie_detail_extra));
    setMovieDetails(movie);

    if (NetworkUtils.checkInternetConnection(this)) {
      loadMovieImages(movie);
      loadMovieReviews(movie);
      loadMovieVideos(movie);
    }
  }

  @Override
  public void onReviewsLoaded(List<Review> reviews) {
    reviewProgressBar.setVisibility(View.GONE);
    reviewAdapter.updateReviewList(reviews);
  }

  @Override
  public void onReviewsEmpty() {
    reviewProgressBar.setVisibility(View.GONE);
  }

  @Override
  public void onVideosLoaded(List<Video> videos) {
    this.videos = videos;
    videoProgressBar.setVisibility(View.GONE);
    videoPlayBtn.show();
  }

  @Override
  public void onVideosEmpty() {
    videoProgressBar.setVisibility(View.GONE);
  }

  private void bind() {
    moviePosterImageView = findViewById(R.id.iv_movie_poster);
    movieBackdropImageView = findViewById(R.id.iv_movie_backdrop);
    movieTitleTextView = findViewById(R.id.tv_movie_date);
    movieOverviewTextView = findViewById(R.id.tv_movie_overview);
    movieReleaseDate = findViewById(R.id.tv_movie_release_date);
    movieRatingBar = findViewById(R.id.rb_movie_rating);
    reviewRecyclerView = findViewById(R.id.rv_reviews);
    reviewProgressBar = findViewById(R.id.pb_review_loading);
    videoPlayBtn = findViewById(R.id.iv_play_arrow);
    videoProgressBar = findViewById(R.id.pb_video_loading);
  }

  private void initReviewRecyclerView() {
    reviewAdapter = new ReviewAdapter();
    reviewRecyclerView.setAdapter(reviewAdapter);
    reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    reviewRecyclerView.setHasFixedSize(true);
  }

  private void initVideoClickListener() {
    videoPlayBtn.setOnClickListener(view -> {
      playVideo(videos);
    });
  }

  private void setMovieDetails(Movie movie) {
    movieTitleTextView.setText(movie.getTitle());
    movieOverviewTextView.setText(movie.getOverview().trim());
    movieReleaseDate.setText(movie.getReleaseDate());
    movieRatingBar.setRating(movie.getRating());
  }

  private void loadMovieReviews(Movie movie) {
    new FetchMovieReviewsTask(movie.getId(), this).execute();
  }

  private void loadMovieVideos(Movie movie) {
    new FetchMovieVideosTask(movie.getId(), this).execute();
  }

  private void loadMovieImages(Movie movie) {
    Picasso.with(this).load(movie.getPosterUrl(this))
      .fit()
      .error(R.drawable.ic_error)
      .into(moviePosterImageView);

    Picasso.with(this).load(movie.getBackdropUrl(this))
      .fit()
      .centerCrop()
      .error(R.drawable.ic_error)
      .into(movieBackdropImageView);
  }

  public void playVideo(List<Video> videos) {
    for (Video video : videos) {
      if (video.getSite().equals(Video.SITE_YOUTUBE)) {
        startActivity(new Intent(Intent.ACTION_VIEW,
          Uri.parse("http://www.youtube.com/watch?v=" + video.getKey())));
        break;
      }
    }
  }
}
