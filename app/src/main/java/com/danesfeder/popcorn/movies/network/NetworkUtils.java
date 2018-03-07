package com.danesfeder.popcorn.movies.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import com.danesfeder.popcorn.R;

public class NetworkUtils {

  private NetworkUtils() {
  }

  public static boolean checkInternetConnection(Context context) {
    ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    // Make sure a manager was found
    if (manager == null) {
      return false;
    }

    NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
    boolean isConnected = activeNetwork != null &&
      activeNetwork.isConnectedOrConnecting();

    if (!isConnected) {
      showConnectivityDialog(context);
    }
    return isConnected;
  }

  private static void showConnectivityDialog(Context context) {
    new AlertDialog.Builder(context)
      .setTitle(R.string.internet_connection_title)
      .setMessage(R.string.internet_connection_message)
      .setNegativeButton(R.string.button_text_dismiss, (dialog, which) -> dialog.dismiss())
      .setIcon(android.R.drawable.ic_dialog_alert)
      .show();
  }
}
