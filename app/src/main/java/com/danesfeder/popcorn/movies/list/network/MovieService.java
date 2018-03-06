package com.danesfeder.popcorn.movies.list.network;

import com.danesfeder.popcorn.movies.list.network.model.Movies;
import com.danesfeder.popcorn.movies.list.network.model.Reviews;
import com.danesfeder.popcorn.movies.list.network.model.Videos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

  @GET("3/movie/popular")
  Call<Movies> fetchPopularMovies(@Query("api_key") String apiKey);

  @GET("3/movie/top_rated")
  Call<Movies> fetchTopRatedMovies(@Query("api_key") String apiKey);

  @GET("3/movie/{id}/videos")
  Call<Videos> fetchVideosForMovieId(@Path("id") long movieId, @Query("api_key") String apiKey);

  @GET("3/movie/{id}/reviews")
  Call<Reviews> fetchReviewsForMovieId(@Path("id") long movieId, @Query("api_key") String apiKey);
}
