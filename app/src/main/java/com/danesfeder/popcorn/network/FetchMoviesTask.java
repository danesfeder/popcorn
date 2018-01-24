package com.danesfeder.popcorn.network;

import android.os.AsyncTask;
import android.support.annotation.IntDef;
import android.util.Log;

import com.danesfeder.popcorn.BuildConfig;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FetchMoviesTask extends AsyncTask<Void, Void, List<Movie>> {

  private static String LOG_TAG = FetchMoviesTask.class.getSimpleName();

  public final static int POPULAR = 0;
  public final static int TOP_RATED = 1;

  @Retention(RetentionPolicy.SOURCE)
  @IntDef({POPULAR, TOP_RATED})
  @interface TASK_TYPE {
  }

  private int taskType = POPULAR;
  private MoviesLoadedListener listener;

  public FetchMoviesTask(@TASK_TYPE int taskType, MoviesLoadedListener listener) {
    this.taskType = taskType;
    this.listener = listener;
  }

  @Override
  protected List<Movie> doInBackground(Void... voids) {

    Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(NetworkConstants.MOVIE_DB_BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build();

    MovieService service = retrofit.create(MovieService.class);
    Call<Movies> call = null;
    String movieDbApiKey = BuildConfig.MOVIE_DB_API_KEY;

    // Create call based on task type
    if (taskType == POPULAR) {
      call = service.fetchPopularMovies(movieDbApiKey);
    } else if (taskType == TOP_RATED) {
      call = service.fetchTopRatedMovies(movieDbApiKey);
    }

    // Check for null call
    if (call == null) {
      return null;
    }

    // Execute the call and return the movies if successful
    try {
      Response<Movies> response = call.execute();
      Movies movies = response.body();
      return movies.getMovies();
    } catch (IOException exception) {
      Log.e(LOG_TAG, "Unable to retrieve movies from MovieDB", exception);
    }
    return null;
  }

  @Override
  protected void onPostExecute(List<Movie> movies) {
    if (movies == null || movies.isEmpty()) {
      Log.e(LOG_TAG, "An error occurred retrieving the movies - movie list is null or empty");
      return;
    }
    // Valid list, pass to listener
    if (listener != null) {
      listener.onMoviesLoaded(movies);
    }
  }

  public interface MoviesLoadedListener {
    void onMoviesLoaded(List<Movie> movies);
  }
}
