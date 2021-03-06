package com.danesfeder.popcorn.movies.detail.reviews;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.danesfeder.popcorn.BuildConfig;
import com.danesfeder.popcorn.movies.network.MovieService;
import com.danesfeder.popcorn.movies.network.NetworkConstants;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FetchMovieReviewsTask extends AsyncTask<Void, Void, ArrayList<Review>> {

  private static String LOG_TAG = FetchMovieReviewsTask.class.getSimpleName();

  private long movieId = -1;
  private MovieReviewsLoadedListener listener;

  public FetchMovieReviewsTask(@Nullable Long movieId, @NonNull MovieReviewsLoadedListener listener) {
    this.listener = listener;
    if (movieId != null) {
      this.movieId = movieId;
    }
  }

  @Override
  protected ArrayList<Review> doInBackground(Void... voids) {

    Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(NetworkConstants.MOVIE_DB_BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build();

    MovieService service = retrofit.create(MovieService.class);
    Call<Reviews> reviewsCall;
    String movieDbApiKey = BuildConfig.MOVIE_DB_API_KEY;

    if (movieId < 0) {
      return null;
    }

    reviewsCall = service.fetchReviewsForMovieId(movieId, movieDbApiKey);

    // Execute the call and return the reviews if successful
    try {
      Response<Reviews> response = reviewsCall.execute();
      Reviews reviews = response.body();
      return reviews.getReviews();
    } catch (IOException exception) {
      Log.e(LOG_TAG, "Unable to retrieve reviews from MovieDB", exception);
    }
    return null;
  }

  @Override
  protected void onPostExecute(ArrayList<Review> reviews) {
    if (reviews == null || reviews.isEmpty()) {
      Log.e(LOG_TAG, "Review list is null or empty");
      listener.onReviewsEmpty();
      return;
    }
    // Valid list, pass to listener
    listener.onReviewsLoaded(reviews);
  }

  public interface MovieReviewsLoadedListener {
    void onReviewsLoaded(ArrayList<Review> reviews);

    void onReviewsEmpty();
  }
}
