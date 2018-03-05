package com.danesfeder.popcorn.movies.list;

import com.danesfeder.popcorn.movies.list.network.Movie;

interface MovieClickListener {

  void onMovieClick(Movie clickedMovie);

  void onMovieFavorite(Movie favoriteMovie);

  void onMovieFavoriteRemoved(Movie favoriteMovieRemoved);
}
