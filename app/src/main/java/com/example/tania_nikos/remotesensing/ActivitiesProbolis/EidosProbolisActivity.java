package com.example.tania_nikos.remotesensing.ActivitiesProbolis;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import com.example.tania_nikos.remotesensing.ActivitiesSindesis.EidosSindesisActivity;
import com.example.tania_nikos.remotesensing.ActivitiesSindesis.XwrafiSindesiActivity;
import com.example.tania_nikos.remotesensing.GegonotaActivity;
import com.example.tania_nikos.remotesensing.Helpers.InternetHandler;
import com.example.tania_nikos.remotesensing.R;

public class EidosProbolisActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eidos_probolis);
    }

    /**
     * Open XwrafiaProboli
     *
     * @param view
     */
    public void proboliXwrafia(View view)
    {
        Intent intent = new Intent(EidosProbolisActivity.this, XwrafiaProboliActivity.class);
        startActivity(intent);
    }

    /**
     * Open GegonotaProvoli
     *
     * @param view
     */
    public void proboliGegonota(View view)
    {
        Intent intent = new Intent(EidosProbolisActivity.this, GegonotaProboliActivity.class);
        startActivity(intent);
    }
}
