package com.example.tania_nikos.remotesensing.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by Tania-Nikos on 19/2/2017.
 */

public class InternetHandler {

    public static void checkInternet(final Context context)
    {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);

        boolean isConnected = false ;
        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet

            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            isConnected = true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            isConnected = false;
        }

        if(!isConnected) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Δεν υπάρχει σύνδεση στο ίντερνετ")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //exit the app
                            ((Activity) context).finish();                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
