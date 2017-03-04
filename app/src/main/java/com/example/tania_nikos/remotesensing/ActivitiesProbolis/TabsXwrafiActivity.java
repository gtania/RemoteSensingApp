package com.example.tania_nikos.remotesensing.ActivitiesProbolis;

import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.tania_nikos.remotesensing.GegonotaActivity;
import com.example.tania_nikos.remotesensing.Helpers.Device;
import com.example.tania_nikos.remotesensing.Helpers.InternetHandler;
import com.example.tania_nikos.remotesensing.Helpers.OnSwipeTouchListener;
import com.example.tania_nikos.remotesensing.Http.AsyncResponse;
import com.example.tania_nikos.remotesensing.Http.HttpGetTask;
import com.example.tania_nikos.remotesensing.Http.HttpTaskHandler;
import com.example.tania_nikos.remotesensing.Http.Response;
import com.example.tania_nikos.remotesensing.MainActivity;
import com.example.tania_nikos.remotesensing.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class TabsXwrafiActivity extends TabActivity {

    protected JSONArray fieldImages;
    protected JSONArray fieldcomments;
    protected int position;

    protected TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_xwrafi);
        InternetHandler.checkInternet(this);

        position = getIntent().getIntExtra("position", 0);
        String fieldImagesString = getIntent().getStringExtra("fieldImages");
        try {
            fieldImages = new JSONArray(fieldImagesString);
            JSONObject fieldImage = fieldImages.getJSONObject(position);

                    tabHost = (TabHost) findViewById(android.R.id.tabhost);

                    TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab");
                    TabHost.TabSpec tab2 = tabHost.newTabSpec("Second Tab");

                    tab1.setIndicator("Φωτογραφία");
                    Intent intent1 = new Intent(TabsXwrafiActivity.this, XwrafiProboliSliderActivity.class);
                    intent1.putExtra("position", position);
                    intent1.putExtra("fieldImages", fieldImages.toString());
                    tab1.setContent(intent1);


                    tab2.setIndicator("Σχόλια");
                    Intent intent2 = new Intent(TabsXwrafiActivity.this, CommentsActivity.class);
                    intent2.putExtra("position", position);
                    intent2.putExtra("fieldImages", fieldImages.toString());
                    tab2.setContent(intent2);

                    tabHost.addTab(tab1);
                    tabHost.addTab(tab2);
                    Log.d("APP ", "Before Listener");
                    tabHost.setOnTouchListener(new OnSwipeTouchListener(TabsXwrafiActivity.this) {
                        public void onSwipeRight() {
                            // -1 image
                            if ((position - 1) >= 0) {
                                position--;
                                try {
                                    JSONObject fieldImage = fieldImages.getJSONObject(position);

                                            tabHost.clearAllTabs();

                                            Log.d("APP", "-1 item: pos : " + Integer.toString(position));
                                            Random r = new Random();

                                            TabHost.TabSpec tab1 = tabHost.newTabSpec("tab" + r.nextInt());
                                            TabHost.TabSpec tab2 = tabHost.newTabSpec("tab" + r.nextInt());

                                            tab1.setIndicator("Φωτογραφία");
                                            Intent intent1 = new Intent(TabsXwrafiActivity.this, XwrafiProboliSliderActivity.class);
                                            intent1.putExtra("position", position);
                                            intent1.putExtra("fieldImages", fieldImages.toString());
                                            tab1.setContent(intent1);

                                            tab2.setIndicator("Σχόλια");
                                            Intent intent2 = new Intent(TabsXwrafiActivity.this, CommentsActivity.class);
                                            intent2.putExtra("fieldImages", fieldImages.toString());
                                            tab2.setContent(intent2);

                                            tabHost.addTab(tab1);
                                            tabHost.addTab(tab2);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Toast.makeText(TabsXwrafiActivity.this, "Τέλος φωτογραφιών", Toast.LENGTH_SHORT).show();
                            }
                        }

                        public void onSwipeLeft() {
                            // +1 image
                            if ((position + 1) < fieldImages.length()) {
                                position++;

                                try {
                                    JSONObject fieldImage = fieldImages.getJSONObject(position);

                                            Log.d("APP", "+1 item : pos : " + Integer.toString(position));

                                            tabHost.clearAllTabs();
                                            Random r = new Random();
                                            TabHost.TabSpec tab1 = tabHost.newTabSpec("tab" + r.nextInt());
                                            TabHost.TabSpec tab2 = tabHost.newTabSpec("tab" + r.nextInt());

                                            tab1.setIndicator("Φωτογραφία");
                                            Intent intent1 = new Intent(TabsXwrafiActivity.this, XwrafiProboliSliderActivity.class);
                                            intent1.putExtra("position", position);
                                            intent1.putExtra("fieldImages", fieldImages.toString());
                                            tab1.setContent(intent1);

                                            tab2.setIndicator("Σχόλια");
                                            Intent intent2 = new Intent(TabsXwrafiActivity.this, CommentsActivity.class);
                                            intent2.putExtra("position",  position);
                                            intent2.putExtra("fieldImages", fieldImages.toString());
                                            tab2.setContent(intent2);

                                            tabHost.addTab(tab1);
                                            tabHost.addTab(tab2);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            } else {
                                Toast.makeText(TabsXwrafiActivity.this, "Τέλος φωτογραφιών", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            Intent intent = new Intent(TabsXwrafiActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
