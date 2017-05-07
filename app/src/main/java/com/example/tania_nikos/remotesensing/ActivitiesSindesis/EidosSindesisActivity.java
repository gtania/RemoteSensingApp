package com.example.tania_nikos.remotesensing.ActivitiesSindesis;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import com.example.tania_nikos.remotesensing.Helpers.InternetHandler;
import com.example.tania_nikos.remotesensing.PictureActivity;
import com.example.tania_nikos.remotesensing.R;

public class EidosSindesisActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eidos_sindesis);
        String onoma_gegonotos = getIntent().getStringExtra("onoma_gegonotos");
    }

    /**
     * Connect photo to Xwrafi
     *
     * @param view
     */
    public void connectXwrafi(View view)
    {
        Intent intent = new Intent(EidosSindesisActivity.this, XwrafiSindesiActivity.class);
        intent.putExtra("photo_path", getIntent().getStringExtra("photo_path"));
        startActivity(intent);
    }

    /**
     * Connect photo to Gegonos
     *
     * @param view
     */
    public void connectGegonos(View view)
    {
        Intent intent = new Intent(EidosSindesisActivity.this, GegonosSindesiActivity.class);
        intent.putExtra("photo_path", getIntent().getStringExtra("photo_path"));
        startActivity(intent);
    }
}
