package com.example.tania_nikos.remotesensing;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tania_nikos.remotesensing.Helpers.Device;
import com.example.tania_nikos.remotesensing.Helpers.InternetHandler;
import com.example.tania_nikos.remotesensing.Http.AsyncResponse;
import com.example.tania_nikos.remotesensing.Http.HttpGetTask;
import com.example.tania_nikos.remotesensing.Http.HttpTaskHandler;
import com.example.tania_nikos.remotesensing.Http.Response;
import com.example.tania_nikos.remotesensing.Models.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GegonotaActivity extends ActionBarActivity {

    /**
     * List View
     */
    ListView listView ;

    /**
     *
     */
    Cursor eventsCursor;

    /**
     * Initialize view load data
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gegonota);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list_gegonota);

        String deviceId = Device.getId(this);

        Event event = new Event(this);
        final Cursor eventsCursor = event.getEvents();

        if (eventsCursor != null) {
            ArrayList<String> events_list = new ArrayList<String>();

            while (eventsCursor.moveToNext()) {
                events_list.add(eventsCursor.getString(eventsCursor.getColumnIndex("onoma_gegonotos")));
            }

            // Define a new Adapter
            // First parameter - Context
            // Second parameter - Layout for the row
            // Third parameter - ID of the TextView to which the data is written
            // Forth - the Array of data
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(GegonotaActivity.this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, events_list);

            // Assign adapter to ListView
            listView.setAdapter(adapter);


            // ListView Item Click Listener
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    // ListView Clicked item index
                    int itemPosition = position;

                    eventsCursor.moveToPosition(position);

                    Intent intent = new Intent(GegonotaActivity.this, EditGegonosActivity.class);
                    intent.putExtra("id", eventsCursor.getInt(eventsCursor.getColumnIndex("id")));
                    intent.putExtra("onoma_gegonotos", eventsCursor.getString(eventsCursor.getColumnIndex("onoma_gegonotos")));
                    intent.putExtra("perigrafi_gegonotos", eventsCursor.getString(eventsCursor.getColumnIndex("perigrafi_gegonotos")));
                    intent.putExtra("onoma_xorafiou", eventsCursor.getString(eventsCursor.getColumnIndex("onoma_xorafiou")));
                    intent.putExtra("perioxi_xorafiou", eventsCursor.getString(eventsCursor.getColumnIndex("perioxi_xorafiou")));
                    intent.putExtra("etos_kaliergias", eventsCursor.getString(eventsCursor.getColumnIndex("etos_kaliergias")));
                    intent.putExtra("onoma_kaliergiti", eventsCursor.getString(eventsCursor.getColumnIndex("onoma_kaliergiti")));
                    intent.putExtra("eidos", eventsCursor.getString(eventsCursor.getColumnIndex("eidos")));

                    System.out.println("just before change view");
                    startActivity(intent);
                }
            });

        }  else {
            runOnUiThread(new Runnable() {
                public void run()
                {
                    Toast.makeText(
                            getApplicationContext(),
                            "Φόρτωση μη επιτυχής",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        }
    }

    /**
     * Open EisagwgiGogonosActivity
     *
     * @param view
     */
    public void eisagegiGegwnotaActivity(View view)
    {
        Intent intent = new Intent(GegonotaActivity.this, EisagwgiGegonosActivity.class);
        startActivity(intent);
    }

    /**
     * Handle Back Button
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(GegonotaActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
