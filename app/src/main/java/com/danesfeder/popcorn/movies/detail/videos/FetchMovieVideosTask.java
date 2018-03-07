package com.danesfeder.popcorn.movies.detail.videos;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.danesfeder.popcorn.BuildConfig;
import com.danesfeder.popcorn.movies.network.MovieService;
import com.danesfeder.popcorn.movies.network.NetworkConstants;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FetchMovieVideosTask extends AsyncTask<Void, Void, List<Video>> {

  private static String LOG_TAG = FetchMovieVideosTask.class.getSimpleName();

  private long movieId = -1;
  private MovieVideosLoadedListener listener;

  public FetchMovieVideosTask(@Nullable Long movieId, @NonNull MovieVideosLoadedListener listener) {
    this.listener = listener;
    if (movieId != null) {
      this.movieId = movieId;
    }
  }

  @Override
  protected List<Video> doInBackground(Void... voids) {

    Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(NetworkConstants.MOVIE_DB_BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build();

    MovieService service = retrofit.create(MovieService.class);
    Call<Videos> videosCall;
    String movieDbApiKey = BuildConfig.MOVIE_DB_API_KEY;

    if (movieId < 0) {
      return null;
    }

    videosCall = service.fetchVideosForMovieId(movieId, movieDbApiKey);

    // Execute the call and return the videos if successful
    try {
      Response<Videos> response = videosCall.execute();
      Videos videos = response.body();
      return videos.getVideos();
    } catch (IOException exception) {
      Log.e(LOG_TAG, "Unable to retrieve videos from MovieDB", exception);
    }
    return null;
  }

  @Override
  protected void onPostExecute(List<Video> videos) {
    if (videos == null || videos.isEmpty()) {
      Log.e(LOG_TAG, "Video list is null or empty");
      listener.onVideosEmpty();
      return;
    }
    // Valid list, pass to listener
    listener.onVideosLoaded(videos);
  }

  public interface MovieVideosLoadedListener {
    void onVideosLoaded(List<Video> videos);

    void onVideosEmpty();
  }
}
