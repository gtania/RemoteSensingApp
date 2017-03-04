package com.example.tania_nikos.remotesensing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

public class XorafiaActivity extends ActionBarActivity {

    /**
     * ListView
     */
    ListView listView ;

    /**
     * Fields Array
     */
    JSONArray fields;

    /**
     * Create View Load Initial Data
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InternetHandler.checkInternet(this);
        System.out.println("before Device ID");
        String deviceId = Device.getId(this);

        System.out.println("in view Xwrafia");
        setContentView(R.layout.activity_xorafia);
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list_xorafia);



        System.out.println("before HTTP CALL");

        HttpGetTask get = new HttpGetTask(HttpTaskHandler.baseUrl + deviceId + "/fields", new AsyncResponse() {
            public void processFinish(Response response) {
                // an exei lathi ta emfanizoume
                try {
                    //parse response
                    fields = new JSONArray(response.data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                System.out.println("In Get FIELDS " + fields.toString());
                if (response.status_code == 200) {
                    final JSONArray finalFields = fields;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ArrayList<String> fields_list = new ArrayList<String>();
                            for (int i = 0; i < finalFields.length(); i++) {
                                try {
                                    JSONObject field = finalFields.getJSONObject(i);

                                    fields_list.add(field.getString("onoma_xorafiou"));


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            // Define a new Adapter
                            // First parameter - Context
                            // Second parameter - Layout for the row
                            // Third parameter - ID of the TextView to which the data is written
                            // Forth - the Array of data
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(XorafiaActivity.this,
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
                int itemPosition     = position;

                try {
                    // ListView Clicked item value

                    JSONObject field = fields.getJSONObject(position);

                    Intent intent = new Intent(XorafiaActivity.this, EditXorafiActivity.class);
                    intent.putExtra("id", field.getInt("id"));
                    intent.putExtra("onoma_xorafiou", field.getString("onoma_xorafiou"));
                    intent.putExtra("perioxi_xorafiou", field.getString("perioxi_xorafiou"));
                    intent.putExtra("etos_kaliergias", field.getString("etos_kaliergias"));
                    intent.putExtra("onoma_kaliergiti", field.getString("onoma_kaliergiti"));
                    intent.putExtra("eidos", field.getString("eidos"));

                    System.out.println("just before change view");
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }

        });

    }

    /**
     * Open EisagwgiXorafiActivity
     *
     * @param view
     */
    public void eisagegiXorafiaActivity(View view)
    {
        Intent intent = new Intent(XorafiaActivity.this, EisagwgiXorafiActivity.class);
        startActivity(intent);
    }

    /**
     * Handle back button
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(XorafiaActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
