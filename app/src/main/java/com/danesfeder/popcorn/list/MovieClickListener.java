package com.danesfeder.popcorn.list;

import com.danesfeder.popcorn.network.Movie;

interface MovieClickListener {
  void onMovieClick(Movie clickedMovie);
}
