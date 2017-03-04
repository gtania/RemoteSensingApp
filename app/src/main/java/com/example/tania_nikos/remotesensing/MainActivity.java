package com.example.tania_nikos.remotesensing;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.tania_nikos.remotesensing.ActivitiesProbolis.EidosProbolisActivity;
import com.example.tania_nikos.remotesensing.Helpers.InternetHandler;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InternetHandler.checkInternet(this);

    }

    /**
     * Open XorafiaActivity
     * @param view
     */
    public void xorafiaActivity(View view)
    {
        Intent intent = new Intent(MainActivity.this, XorafiaActivity.class);
        startActivity(intent);
    }

    /**
     * Open GegonotaActivity
     * @param view
     */
    public void gegonotaActivity(View view)
    {
        Intent intent = new Intent(MainActivity.this, GegonotaActivity.class);
        startActivity(intent);
    }

    /**
     * Open pictureActivity
     *
     * @param view
     */
    public void pictureActivity(View view)
    {
        Intent intent = new Intent(MainActivity.this, PictureActivity.class);
        startActivity(intent);
    }

    /**
     * Open ProvoliActivity
     *
     * @param view
     */
    public void proboliActivity(View view)
    {
        Intent intent = new Intent(MainActivity.this, EidosProbolisActivity.class);
        startActivity(intent);
    }

    /**
     * Check if device is connected to the internet
     *
     * @return
     */
    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet

            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            Toast.makeText(this, "Δεν βρέθηκε σύνδεση στο internet", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    /**
     * Close application on back button
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
