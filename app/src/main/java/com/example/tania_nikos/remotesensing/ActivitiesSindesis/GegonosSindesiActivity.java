package com.example.tania_nikos.remotesensing.ActivitiesSindesis;

import android.content.Context;
import android.content.Intent;
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

import com.example.tania_nikos.remotesensing.Helpers.Device;
import com.example.tania_nikos.remotesensing.Helpers.InternetHandler;
import com.example.tania_nikos.remotesensing.Helpers.Spiner;
import com.example.tania_nikos.remotesensing.Http.AsyncResponse;
import com.example.tania_nikos.remotesensing.Http.HttpGetTask;
import com.example.tania_nikos.remotesensing.Http.HttpPostTask;
import com.example.tania_nikos.remotesensing.Http.HttpTaskHandler;
import com.example.tania_nikos.remotesensing.Http.Response;
import com.example.tania_nikos.remotesensing.MainActivity;
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

public class GegonosSindesiActivity extends ActionBarActivity {

    ListView listView ;

    /**
     * Events array
     */
    JSONArray events;

    /**
     * Constructor
     * Get Events to connect the image with
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gegonos_sindesi);
        InternetHandler.checkInternet(this);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list_gegonota_sindesi);

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


                System.out.println("In Get FIELDS " + events.toString());
                if (response.status_code == 200) {
                    final JSONArray finalFields = events;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ArrayList<String> fields_list = new ArrayList<String>();
                            for (int i = 0; i < finalFields.length(); i++) {
                                try {
                                    JSONObject field = finalFields.getJSONObject(i);

                                    fields_list.add(
                                            field.getString("onoma_gegonotos") + "\n" +
                                            field.getString("onoma_xorafiou") + "\n" +
                                            field.getString("perioxi_xorafiou") + "\n" +
                                            field.getString("etos_kaliergias") + "\n" +
                                            field.getString("onoma_kaliergiti") + "\n"
                                    );


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            // Define a new Adapter
                            // First parameter - Context
                            // Second parameter - Layout for the row
                            // Third parameter - ID of the TextView to which the data is written
                            // Forth - the Array of data
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(GegonosSindesiActivity.this,
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
                Spiner.show(GegonosSindesiActivity.this);


                // ListView Clicked item index
                int itemPosition     = position;

                try {
                    // ListView Clicked item value

                    JSONObject event = events.getJSONObject(position);
                    Integer event_id = event.getInt("id");

                    String deviceId = Device.getId(GegonosSindesiActivity.this);

                    // Image
                    Bitmap bm = BitmapFactory.decodeFile(getIntent().getStringExtra("photo_path"));
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
                    byte[] ba = bao.toByteArray();
                    String base64_image = Base64.encodeToString(ba, Base64.DEFAULT);


                    // Set up data for post
                    List<NameValuePair> data = new ArrayList<NameValuePair>();
                    data.add(new BasicNameValuePair("base64_image", base64_image));
                    data.add(new BasicNameValuePair("filepath", getIntent().getStringExtra("photo_path")));

                    HttpPostTask post = new HttpPostTask( HttpTaskHandler.baseUrl + deviceId + "/events/"+ event_id + "/image", data, new AsyncResponse() {
                        public void processFinish(Response response) {
                            // an exei lathi ta emfanizoume
                            JSONObject jObject = null;
                            try {
                                jObject = new JSONObject(response.data);
                            } catch (JSONException e) {
                                e.printStackTrace();
                             //   System.out.println("Resposeeeeee : " + response.data);

                            }
                          //  System.out.println("In call TRERERERERRE" + jObject.toString());

                            if (response.status_code == 200) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "Εγγραφή επιτυχής",
                                                Toast.LENGTH_LONG
                                        ).show();
                                    }
                                });

                                Intent intent = new Intent(GegonosSindesiActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                runOnUiThread(new Runnable() {
                                    public void run()
                                    {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "Εγγραφή μη επιτυχής",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }
                                });
                            }
                            Spiner.dismiss();

                        }
                    });

                    new HttpTaskHandler().execute(post);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
