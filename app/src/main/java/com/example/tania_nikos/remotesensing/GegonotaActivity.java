package com.example.tania_nikos.remotesensing;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
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
     * Events array
     */
    JSONArray events;

    /**
     * Initialize view load data
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gegonota);
        InternetHandler.checkInternet(this);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list_gegonota);

        String deviceId = Device.getId(this);

        HttpGetTask get = new HttpGetTask(HttpTaskHandler.baseUrl + deviceId + "/events", new AsyncResponse() {
            public void processFinish(Response response) {
                // an exei lathi ta emfanizoume
                try {
                    //parse response
                    events = new JSONArray(response.data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                System.out.println("In Get GEGONOTA " + events.toString());
                if (response.status_code == 200) {
                    final JSONArray finalEvents = events;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ArrayList<String> fields_list = new ArrayList<String>();
                            for (int i = 0; i < finalEvents.length(); i++) {
                                try {
                                    JSONObject field = finalEvents.getJSONObject(i);

                                    fields_list.add(field.getString("onoma_gegonotos"));


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            // Define a new Adapter
                            // First parameter - Context
                            // Second parameter - Layout for the row
                            // Third parameter - ID of the TextView to which the data is written
                            // Forth - the Array of data
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(GegonotaActivity.this,
                                    android.R.layout.simple_list_item_1, android.R.id.text1, fields_list);

                            // Assign adapter to ListView
                            listView.setAdapter(adapter);
                        }
                    });
                } else {
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
        });
        new HttpTaskHandler().execute(get);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                try {
                    JSONObject event = events.getJSONObject(position);

                    Intent intent = new Intent(GegonotaActivity.this, EditGegonosActivity.class);
                    intent.putExtra("id", event.getInt("id"));
                    intent.putExtra("onoma_gegonotos", event.getString("onoma_gegonotos"));
                    intent.putExtra("perigrafi_gegonotos", event.getString("perigrafi_gegonotos"));
                    intent.putExtra("onoma_xorafiou", event.getString("onoma_xorafiou"));
                    intent.putExtra("perioxi_xorafiou", event.getString("perioxi_xorafiou"));
                    intent.putExtra("etos_kaliergias", event.getString("etos_kaliergias"));
                    intent.putExtra("onoma_kaliergiti", event.getString("onoma_kaliergiti"));
                    intent.putExtra("eidos", event.getString("eidos"));

                    System.out.println("just before change view");
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        });

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
