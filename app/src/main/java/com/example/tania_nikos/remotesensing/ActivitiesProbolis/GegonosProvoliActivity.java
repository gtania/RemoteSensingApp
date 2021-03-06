package com.example.tania_nikos.remotesensing.ActivitiesProbolis;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tania_nikos.remotesensing.Helpers.CustomListAdapter;
import com.example.tania_nikos.remotesensing.Helpers.Device;
import com.example.tania_nikos.remotesensing.Helpers.InternetHandler;
import com.example.tania_nikos.remotesensing.Helpers.ListDeleteHandler;
import com.example.tania_nikos.remotesensing.Helpers.Spiner;
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

public class GegonosProvoliActivity extends ActionBarActivity {

    public ListView listView;
    protected int event_id;
    protected JSONArray eventImages;

    /**
     * Constructor
     * Date ListView Selection
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gegonos_provoli);
        InternetHandler.checkInternet(this);
        Spiner.show(GegonosProvoliActivity.this);
        String deviceId = Device.getId(this);

        // get data from previous intent
        this.event_id = (int) getIntent().getIntExtra("id", 0);

        HttpGetTask get = new HttpGetTask(HttpTaskHandler.baseUrl + deviceId + "/events/" + this.event_id + "/images", new AsyncResponse() {
            public void processFinish(Response response) {
                // an exei lathi ta emfanizoume
                try {
                    //parse response
                    eventImages = new JSONArray(response.data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                System.out.println("In Get FIELDS " + eventImages.toString());
                if (response.status_code == 200) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            List<String> dates = new ArrayList<String>();
                            List<String> urls = new ArrayList<String>();
                            List<Integer> ids = new ArrayList<Integer>();
                            List<String> filepaths = new ArrayList<String>();

                            for (int i =0 ; i< eventImages.length(); i++)
                            {
                                try {
                                    JSONObject field = eventImages.getJSONObject(i);
                                    dates.add(field.getString("created_at"));
                                    urls.add(HttpTaskHandler.baseUrl + "images/events/"+ field.getInt("id"));
                                    ids.add(field.getInt("id"));
                                    filepaths.add(field.getString("filepath"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            final String[] itemname = dates.toArray(new String[dates.size()]);
                            String[] string_urls = urls.toArray(new String[urls.size()]);

                            ListDeleteHandler deleteHandler = new ListDeleteHandler(
                                    ListDeleteHandler.events,
                                    ids.toArray(new Integer[ids.size()]),
                                    filepaths.toArray(new String[filepaths.size()]),
                                    new AsyncResponse() {
                                        @Override
                                        public void processFinish(Response response) {
                                            if (response.status_code == 200) {
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        Toast.makeText(
                                                                getApplicationContext(),
                                                                "Διαγραφή επιτυχής",
                                                                Toast.LENGTH_LONG
                                                        ).show();
                                                    }
                                                });

                                                Intent intent = getIntent();
                                                finish();
                                                startActivity(intent);
                                            } else {
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        Toast.makeText(
                                                                getApplicationContext(),
                                                                "Διαγραφή μη επιτυχής",
                                                                Toast.LENGTH_SHORT
                                                        ).show();
                                                    }
                                                });
                                            }
                                        }
                                    });

                            CustomListAdapter adapter=new CustomListAdapter(
                                    GegonosProvoliActivity.this,
                                    dates.toArray(new String[dates.size()]),
                                    urls.toArray(new String[urls.size()]),
                                    deleteHandler);

                            listView = (ListView) findViewById(R.id.gegonos_date_list);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Intent intent = new Intent(GegonosProvoliActivity.this, TabsGegonosActivity.class);
                                    intent.putExtra("position", position);
                                    intent.putExtra("eventImages", eventImages.toString());
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
                Spiner.dismiss();
            }
        });
        new HttpTaskHandler().execute(get);
    }
}
