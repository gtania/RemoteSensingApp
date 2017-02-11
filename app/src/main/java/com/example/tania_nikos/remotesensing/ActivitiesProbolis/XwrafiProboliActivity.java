package com.example.tania_nikos.remotesensing.ActivitiesProbolis;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tania_nikos.remotesensing.Helpers.CustomListAdapter;
import com.example.tania_nikos.remotesensing.Http.AsyncResponse;
import com.example.tania_nikos.remotesensing.Http.HttpGetTask;
import com.example.tania_nikos.remotesensing.Http.HttpTaskHandler;
import com.example.tania_nikos.remotesensing.Http.Response;
import com.example.tania_nikos.remotesensing.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class XwrafiProboliActivity extends AppCompatActivity {

    public ListView listView;
    protected int field_id;
    protected JSONArray fieldImages;

    /**
     * Constructor
     * Create Date field list view
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xwrafi_proboli);

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();

        // get data from previous intent
        this.field_id = (int) getIntent().getIntExtra("id", 0);

        HttpGetTask get = new HttpGetTask(HttpTaskHandler.baseUrl + deviceId + "/fields/" + this.field_id + "/images", new AsyncResponse() {
            public void processFinish(Response response) {
                // an exei lathi ta emfanizoume
                try {
                    //parse response
                    fieldImages = new JSONArray(response.data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                System.out.println("In Get FIELDS " + fieldImages.toString());
                if (response.status_code == 200) {
                    final JSONArray finalFields = fieldImages;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            List<String> dates = new ArrayList<String>();
                            List<String> urls = new ArrayList<String>();

                            for (int i =0 ; i< fieldImages.length(); i++)
                            {
                                try {
                                    JSONObject field = fieldImages.getJSONObject(i);
                                    dates.add(field.getString("created_at"));
                                    urls.add(HttpTaskHandler.baseUrl + "images/fields/"+ field.getInt("id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            final String[] itemname = dates.toArray(new String[dates.size()]);
                            String[] string_urls = urls.toArray(new String[urls.size()]);

                            CustomListAdapter adapter=new CustomListAdapter(XwrafiProboliActivity.this, itemname, string_urls);

                            listView = (ListView) findViewById(R.id.xwrafi_date_list);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Intent intent = new Intent(XwrafiProboliActivity.this, XwrafiProboliSliderActivity.class);
                                    intent.putExtra("position", position);
                                    intent.putExtra("fieldImages", fieldImages.toString());
                                    startActivity(intent);

                                }
                            });
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


    }
}
