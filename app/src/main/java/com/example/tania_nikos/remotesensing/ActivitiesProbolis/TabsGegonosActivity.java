package com.example.tania_nikos.remotesensing.ActivitiesProbolis;

import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.tania_nikos.remotesensing.Helpers.InternetHandler;
import com.example.tania_nikos.remotesensing.Helpers.OnSwipeTouchListener;
import com.example.tania_nikos.remotesensing.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class TabsGegonosActivity extends TabActivity {

    protected JSONArray eventImages;
    protected JSONArray eventcomments;
    protected int position;

    protected TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_gegonos);
        InternetHandler.checkInternet(this);

        position = getIntent().getIntExtra("position", 0);
        String eventImagesString = getIntent().getStringExtra("eventImages");

        try {
            eventImages = new JSONArray(eventImagesString);
            JSONObject eventImage = eventImages.getJSONObject(position);

            tabHost = (TabHost) findViewById(android.R.id.tabhost);

            TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab22");
            TabHost.TabSpec tab2 = tabHost.newTabSpec("Second Tab");

            tab1.setIndicator("Φωτογραφία");
            Intent intent1 = new Intent(TabsGegonosActivity.this, GegonosProvoliSliderActivity.class);
            intent1.putExtra("position", position);
            intent1.putExtra("eventImages", eventImages.toString());
            tab1.setContent(intent1);


            tab2.setIndicator("Σχόλια");                            // Isos Alagi
            Intent intent2 = new Intent(TabsGegonosActivity.this, CommentsGegonosActivity.class);
            intent2.putExtra("position", position);
            intent2.putExtra("eventImages", eventImages.toString());
            tab2.setContent(intent2);

            tabHost.addTab(tab1);
            tabHost.addTab(tab2);
            Log.d("APP ", "Before Listener");
            tabHost.setOnTouchListener(new OnSwipeTouchListener(TabsGegonosActivity.this) {
                public void onSwipeRight() {
                    // -1 image
                    if ((position - 1) >= 0) {
                        position--;
                        try {
                            JSONObject eventImage = eventImages.getJSONObject(position);

                            tabHost.clearAllTabs();

                            Log.d("APP", "-1 item: pos : " + Integer.toString(position));
                            Random r = new Random();

                            TabHost.TabSpec tab1 = tabHost.newTabSpec("tab" + r.nextInt());
                            TabHost.TabSpec tab2 = tabHost.newTabSpec("tab" + r.nextInt());

                            tab1.setIndicator("Φωτογραφία");
                            Intent intent1 = new Intent(TabsGegonosActivity.this, GegonosProvoliSliderActivity.class);
                            intent1.putExtra("position", position);
                            intent1.putExtra("eventImages", eventImages.toString());
                            tab1.setContent(intent1);

                            tab2.setIndicator("Σχόλια");
                            Intent intent2 = new Intent(TabsGegonosActivity.this, CommentsActivity.class);
                            intent2.putExtra("eventImages", eventImages.toString());
                            tab2.setContent(intent2);

                            tabHost.addTab(tab1);
                            tabHost.addTab(tab2);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(TabsGegonosActivity.this, "Τέλος φωτογραφιών", Toast.LENGTH_SHORT).show();
                    }
                }

                public void onSwipeLeft() {
                    // +1 image
                    if ((position + 1) < eventImages.length()) {
                        position++;

                        try {
                            JSONObject eventImage = eventImages.getJSONObject(position);

                            Log.d("APP", "+1 item : pos : " + Integer.toString(position));

                            tabHost.clearAllTabs();
                            Random r = new Random();
                            TabHost.TabSpec tab1 = tabHost.newTabSpec("tab" + r.nextInt());
                            TabHost.TabSpec tab2 = tabHost.newTabSpec("tab" + r.nextInt());

                            tab1.setIndicator("Φωτογραφία");
                            Intent intent1 = new Intent(TabsGegonosActivity.this, GegonosProvoliSliderActivity.class);
                            intent1.putExtra("position", position);
                            intent1.putExtra("eventImages", eventImages.toString());
                            tab1.setContent(intent1);

                            tab2.setIndicator("Σχόλια");
                            Intent intent2 = new Intent(TabsGegonosActivity.this, CommentsActivity.class);
                            intent2.putExtra("position",  position);
                            intent2.putExtra("eventImages", eventImages.toString());
                            tab2.setContent(intent2);

                            tabHost.addTab(tab1);
                            tabHost.addTab(tab2);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(TabsGegonosActivity.this, "Τέλος φωτογραφιών", Toast.LENGTH_SHORT).show();
                    }
                }

            });

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
}
