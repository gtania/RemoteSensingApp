package com.example.tania_nikos.remotesensing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
import com.example.tania_nikos.remotesensing.Models.Field;

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
     *
     */
    Cursor fieldsCursor;

    /**
     * Create View Load Initial Data
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("before Device ID");
        String deviceId = Device.getId(this);

        System.out.println("in view Xwrafia");
        setContentView(R.layout.activity_xorafia);
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list_xorafia);



        System.out.println("before HTTP CALL");

        Field field = new Field(this);
        final Cursor fieldsCursor = field.getFields();

        if (fieldsCursor != null) {
            ArrayList<String> fields_list = new ArrayList<String>();

            while (fieldsCursor.moveToNext()) {
                Log.i(" ITEMS LOOP ", "IN" + fieldsCursor.getString(fieldsCursor.getColumnIndex("onoma_xorafiou")));
                fields_list.add(fieldsCursor.getString(fieldsCursor.getColumnIndex("onoma_xorafiou")));
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

            // ListView Item Click Listener
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        // ListView Clicked item index
                        int itemPosition     = position;

                        // ListView Clicked item value
                        fieldsCursor.moveToPosition(position);
                        //JSONObject field = fieldsCursor.getJSONObject(position);

                        Intent intent = new Intent(XorafiaActivity.this, EditXorafiActivity.class);
                        intent.putExtra("id", fieldsCursor.getInt(fieldsCursor.getColumnIndex(("id"))));
                        intent.putExtra("onoma_xorafiou", fieldsCursor.getString(fieldsCursor.getColumnIndex("onoma_xorafiou")));
                        intent.putExtra("perioxi_xorafiou", fieldsCursor.getString(fieldsCursor.getColumnIndex("perioxi_xorafiou")));
                        intent.putExtra("etos_kaliergias", fieldsCursor.getString(fieldsCursor.getColumnIndex("etos_kaliergias")));
                        intent.putExtra("onoma_kaliergiti", fieldsCursor.getString(fieldsCursor.getColumnIndex("onoma_kaliergiti")));
                        intent.putExtra("eidos", fieldsCursor.getString(fieldsCursor.getColumnIndex("eidos")));

                        System.out.println("just before change view");
                        startActivity(intent);
                }
            });

        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Φόρτωση μη επιτυχής",
                    Toast.LENGTH_SHORT
            ).show();
        }

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
