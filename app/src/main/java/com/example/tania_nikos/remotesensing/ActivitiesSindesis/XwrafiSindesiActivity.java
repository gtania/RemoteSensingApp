package com.example.tania_nikos.remotesensing.ActivitiesSindesis;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tania_nikos.remotesensing.EditXorafiActivity;
import com.example.tania_nikos.remotesensing.EisagwgiXorafiActivity;
import com.example.tania_nikos.remotesensing.Helpers.Device;
import com.example.tania_nikos.remotesensing.Helpers.InternetHandler;
import com.example.tania_nikos.remotesensing.Helpers.Spiner;
import com.example.tania_nikos.remotesensing.Http.AsyncResponse;
import com.example.tania_nikos.remotesensing.Http.HttpGetTask;
import com.example.tania_nikos.remotesensing.Http.HttpPostTask;
import com.example.tania_nikos.remotesensing.Http.HttpTaskHandler;
import com.example.tania_nikos.remotesensing.Http.Response;
import com.example.tania_nikos.remotesensing.MainActivity;
import com.example.tania_nikos.remotesensing.Models.Field;
import com.example.tania_nikos.remotesensing.R;
import com.example.tania_nikos.remotesensing.XorafiaActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.extras.Base64;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class XwrafiSindesiActivity extends ActionBarActivity {

    /**
     * ListView
     */
    ListView listView ;

    /**
     * Fields array
     */
    JSONArray fields;

    /**
     * Constructor
     * Get Fields to connect the image with
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xwrafi_sindesi);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list_xorafia_sindesi);

        String deviceId = Device.getId(this);

        Field field = new Field(this);
        final Cursor fieldsCursor = field.getFields();

        if (fieldsCursor != null) {
            ArrayList<String> fields_list = new ArrayList<String>();

            while (fieldsCursor.moveToNext()) {
                Log.i(" ITEMS LOOP ", "IN" + fieldsCursor.getString(fieldsCursor.getColumnIndex("onoma_xorafiou")));
                fields_list.add(
                        fieldsCursor.getString(fieldsCursor.getColumnIndex("onoma_xorafiou")) + "\n" +
                        fieldsCursor.getString(fieldsCursor.getColumnIndex("perioxi_xorafiou")) + "\n" +
                        fieldsCursor.getString(fieldsCursor.getColumnIndex("etos_kaliergias")) + "\n" +
                        fieldsCursor.getString(fieldsCursor.getColumnIndex("onoma_kaliergiti")) + "\n"
                );
            }


            // Define a new Adapter
            // First parameter - Context
            // Second parameter - Layout for the row
            // Third parameter - ID of the TextView to which the data is written
            // Forth - the Array of data
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(XwrafiSindesiActivity.this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, fields_list);

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

                Spiner.show(XwrafiSindesiActivity.this);

                // ListView Clicked item index
                int itemPosition     = position;

                    // ListView Clicked item value

                    fieldsCursor.moveToPosition(position);
                    //JSONObject selectedField = fields.getJSONObject(position);
                    Integer field_id = fieldsCursor.getInt(fieldsCursor.getColumnIndex("id"));

                    String deviceId = Device.getId(XwrafiSindesiActivity.this);

                    // Image
//                    Bitmap bm = BitmapFactory.decodeFile(getIntent().getStringExtra("photo_path"));
//                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
//                    bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
//                    byte[] ba = bao.toByteArray();
//                    String base64_image = Base64.encodeToString(ba, Base64.DEFAULT);


                    // Set up data for post
                    ContentValues data = new ContentValues();
                    data.put("field_id", field_id);
                    data.put("filepath", getIntent().getStringExtra("photo_path"));

                    Log.i("IMAGE TAKE : ", getIntent().getStringExtra("photo_path"));
                    Field field = new Field(XwrafiSindesiActivity.this);
                    long created_id = field.addFieldImage(data);

                    if( created_id == -1){
                        Toast.makeText(
                                getApplicationContext(),
                                "Εγγραφή μη επιτυχής",
                                Toast.LENGTH_SHORT
                        ).show();
                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                "Εγγραφή επιτυχής",
                                Toast.LENGTH_LONG
                        ).show();
                        Spiner.dismiss();

                        Intent intent = new Intent(XwrafiSindesiActivity.this, MainActivity.class);
                        startActivity(intent);
                    }


            }
        });
    }
}
