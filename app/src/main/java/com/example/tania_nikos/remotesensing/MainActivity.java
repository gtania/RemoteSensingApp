package com.example.tania_nikos.remotesensing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
