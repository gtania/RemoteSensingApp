package com.example.tania_nikos.remotesensing.ActivitiesProbolis;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tania_nikos.remotesensing.ActivitiesSindesis.GegonosSindesiActivity;
import com.example.tania_nikos.remotesensing.Helpers.Device;
import com.example.tania_nikos.remotesensing.Helpers.InternetHandler;
import com.example.tania_nikos.remotesensing.Http.AsyncResponse;
import com.example.tania_nikos.remotesensing.Http.HttpGetTask;
import com.example.tania_nikos.remotesensing.Http.HttpPostTask;
import com.example.tania_nikos.remotesensing.Http.HttpTaskHandler;
import com.example.tania_nikos.remotesensing.Http.Response;
import com.example.tania_nikos.remotesensing.MainActivity;
import com.example.tania_nikos.remotesensing.Models.Event;
import com.example.tania_nikos.remotesensing.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.extras.Base64;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class GegonotaProboliActivity extends ActionBarActivity {

    ListView listView ;
    JSONArray events;

    /**
     * Constructor
     * Set Event list view selection
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gegonota_proboli);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list_gegonota_proboli);

        String deviceId = Device.getId(this);
        Event event = new Event(this);
        final Cursor eventsCursor = event.getEvents();
        if ( eventsCursor != null ) {

            ArrayList<String> events_list = new ArrayList<String>();

            while (eventsCursor.moveToNext()) {
                events_list.add(
                        eventsCursor.getString(eventsCursor.getColumnIndex("onoma_gegonotos")) + "\n"
                                + eventsCursor.getString(eventsCursor.getColumnIndex("onoma_xorafiou")) + "\n"
                                + eventsCursor.getString(eventsCursor.getColumnIndex("perioxi_xorafiou")) + "\n"
                                + eventsCursor.getString(eventsCursor.getColumnIndex("etos_kaliergias")) + "\n"
                                + eventsCursor.getString(eventsCursor.getColumnIndex("onoma_kaliergiti")) + "\n"
                                + eventsCursor.getString(eventsCursor.getColumnIndex("eidos")) + "\n"

                );
            }

            // Define a new Adapter
            // First parameter - Context
            // Second parameter - Layout for the row
            // Third parameter - ID of the TextView to which the data is written
            // Forth - the Array of data
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(GegonotaProboliActivity.this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, events_list);

            // Assign adapter to ListView
            listView.setAdapter(adapter);

        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Φόρτωση μη επιτυχής",
                    Toast.LENGTH_SHORT
            ).show();
        }

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;
                eventsCursor.moveToPosition(position);
                Integer event_id = eventsCursor.getInt(eventsCursor.getColumnIndex("id"));

                Intent intent = new Intent(GegonotaProboliActivity.this, GegonosProvoliActivity.class);
                intent.putExtra("id", event_id);
                startActivity(intent);
            }
        });

    }
}
