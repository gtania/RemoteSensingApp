package com.example.tania_nikos.remotesensing.ActivitiesProbolis;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tania_nikos.remotesensing.EditXorafiActivity;
import com.example.tania_nikos.remotesensing.Helpers.CustomListAdapter;
import com.example.tania_nikos.remotesensing.Helpers.Device;
import com.example.tania_nikos.remotesensing.Helpers.InternetHandler;
import com.example.tania_nikos.remotesensing.Helpers.ListDeleteHandler;
import com.example.tania_nikos.remotesensing.Helpers.Spiner;
import com.example.tania_nikos.remotesensing.Http.AsyncResponse;
import com.example.tania_nikos.remotesensing.Http.HttpDeleteTask;
import com.example.tania_nikos.remotesensing.Http.HttpGetTask;
import com.example.tania_nikos.remotesensing.Http.HttpTaskHandler;
import com.example.tania_nikos.remotesensing.Http.Response;
import com.example.tania_nikos.remotesensing.MainActivity;
import com.example.tania_nikos.remotesensing.Models.Field;
import com.example.tania_nikos.remotesensing.R;
import com.example.tania_nikos.remotesensing.XorafiaActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class XwrafiProboliActivity extends ActionBarActivity {

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
        Spiner.show(XwrafiProboliActivity.this);
        String deviceId = Device.getId(this);

        // get data from previous intent
        this.field_id = (int) getIntent().getIntExtra("id", 0);
        Field field = new Field(this);
        final Cursor fieldImagesCursor = field.getImagesFromFieldWithId(this.field_id);
        Log.i(" Images Loaded ", "here "+ fieldImagesCursor.getCount());

        if (fieldImagesCursor != null) {
            ArrayList<String> fields_list = new ArrayList<String>();
            List<String> dates = new ArrayList<String>();
            List<String> urls = new ArrayList<String>();
            List<Integer> ids = new ArrayList<Integer>();
            List<String> filepaths = new ArrayList<String>();

            while (fieldImagesCursor.moveToNext()) {
                urls.add(fieldImagesCursor.getString(fieldImagesCursor.getColumnIndex("filepath")));

                filepaths.add(fieldImagesCursor.getString(fieldImagesCursor.getColumnIndex("filepath")));
                ids.add(fieldImagesCursor.getInt(fieldImagesCursor.getColumnIndex("id")));
                dates.add(fieldImagesCursor.getString(fieldImagesCursor.getColumnIndex("created_at")));
            }

            ListDeleteHandler deleteHandler = new ListDeleteHandler(
                    ListDeleteHandler.fields,
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

            CustomListAdapter adapter=new CustomListAdapter(XwrafiProboliActivity.this,
                    dates.toArray(new String[dates.size()]),
                    urls.toArray(new String[urls.size()]),
                    deleteHandler);

            listView = (ListView) findViewById(R.id.xwrafi_date_list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(XwrafiProboliActivity.this, TabsXwrafiActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("fieldImages", fieldImages.toString());
                    startActivity(intent);

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

    public void deleteItem(String type, int id)
    {
        HttpGetTask delete = new HttpGetTask(HttpTaskHandler.baseUrl + "images/" + type + "/" + id + "/delete", new AsyncResponse() {
            @Override
            public void processFinish(Response response) {
                if (response.status_code == 200) {
                   System.out.println(" GOT 200"+ response.data);
                } else {
                    System.out.println(" GOT Something else" + response.data);

                }

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

                    Intent intent = new Intent(XwrafiProboliActivity.this, XwrafiProboliActivity.class);
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
        new HttpTaskHandler().execute(delete);
    }
}
