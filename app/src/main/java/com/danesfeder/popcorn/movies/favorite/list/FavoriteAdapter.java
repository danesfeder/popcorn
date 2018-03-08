package com.danesfeder.popcorn.movies.favorite.list;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danesfeder.popcorn.R;
import com.danesfeder.popcorn.movies.favorite.FavoriteContract;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteViewHolder>
  implements FavoriteViewHolder.ViewHolderClickListener {

  private Cursor favoriteCursor;
  private FavoriteClickListener listener;

  public FavoriteAdapter(FavoriteClickListener listener) {
    this.listener = listener;
  }

  @Override
  public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.favorite_viewholder_layout, parent, false);
    return new FavoriteViewHolder(view, this);
  }

  @Override
  public void onBindViewHolder(FavoriteViewHolder holder, int position) {
    int posterUrlIndex = favoriteCursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_POSTER_URL);
    int titleIndex = favoriteCursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TITLE);
    int overviewIndex = favoriteCursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW);

    favoriteCursor.moveToPosition(position);

    String posterUrl = favoriteCursor.getString(posterUrlIndex);
    String title = favoriteCursor.getString(titleIndex);
    String overview = favoriteCursor.getString(overviewIndex);

    holder.setMovieImage(posterUrl);
    holder.setFavoriteTitleText(title);
    holder.setFavoriteOverviewText(overview);
  }

  @Override
  public int getItemCount() {
    if (favoriteCursor == null) {
      return 0;
    }
    return favoriteCursor.getCount();
  }

  @Override
  public void onViewHolderClick(int position) {
    listener.onFavoriteMovieClick(position);
  }

  public void updateFavorites(Cursor favoriteCursor) {
    if (this.favoriteCursor == favoriteCursor) {
      return;
    }
    this.favoriteCursor = favoriteCursor;
    if (favoriteCursor != null) {
      notifyDataSetChanged();
    }
  }
}
