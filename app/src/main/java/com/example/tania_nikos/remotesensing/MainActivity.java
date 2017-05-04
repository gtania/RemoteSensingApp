package com.example.tania_nikos.remotesensing;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.tania_nikos.remotesensing.ActivitiesProbolis.EidosProbolisActivity;
import com.example.tania_nikos.remotesensing.Database.DBHandler;
import com.example.tania_nikos.remotesensing.Helpers.InternetHandler;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        DBHandler dbHandler = new DBHandler(this, null, null, 1);
//        Cursor c = dbHandler.getTables();
//        if (c.moveToFirst()) {
//            while ( !c.isAfterLast() ) {
//                Toast.makeText(MainActivity.this, "Table Name=> "+c.getString(0), Toast.LENGTH_LONG).show();
//                Log.i("SQL LOG ",  "Table Name=> "+c.getString(0));
//                c.moveToNext();
//            }
//        }

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
