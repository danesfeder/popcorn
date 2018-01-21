package com.danesfeder.popcorn.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {

  @GET("3/movie/popular")
  Call<Movies> fetchPopularMovies(@Query("api_key") String apiKey);

  @GET("3/movie/top_rated")
  Call<Movies> fetchTopRatedMovies(@Query("api_key") String apiKey);
}
