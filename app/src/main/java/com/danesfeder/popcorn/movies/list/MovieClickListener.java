package com.danesfeder.popcorn.movies.list;

interface MovieClickListener {

  void onMovieClick(Movie clickedMovie);

  void onMovieFavorite(Movie favoriteMovie);

  void onMovieFavoriteRemoved(Movie favoriteMovieRemoved);
}
