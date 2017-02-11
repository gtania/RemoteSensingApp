package com.example.tania_nikos.remotesensing.ActivitiesProbolis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tania_nikos.remotesensing.Helpers.OnSwipeTouchListener;
import com.example.tania_nikos.remotesensing.Http.HttpTaskHandler;
import com.example.tania_nikos.remotesensing.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GegonosProvoliSliderActivity extends AppCompatActivity {

    /**
     * Data variables
     */
    protected JSONArray eventImages;
    protected int position;

    /**
     * View Variables
     */
    protected ImageView imageView;
    protected TextView textView;

    /**
     * Image Loader variables
     */
    protected ImageLoader imageLoader;
    protected DisplayImageOptions options;

    /**
     * Constructor
     * Show Big Event Image
     * Set up slide listeners
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gegonos_provoli_slider);

        Intent intent = getIntent();
        String jsonArray = intent.getStringExtra("eventImages");
        position = intent.getIntExtra("position", 0);
        try {
            eventImages = new JSONArray(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        imageView = (ImageView) findViewById(R.id.image_view_gegonos_slider);
        textView = (TextView) findViewById(R.id.slider_image_gegonos_date);

        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP

        imageLoader = ImageLoader.getInstance();
        int fallback = 0 ;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .showImageForEmptyUri(android.R.color.white)
                .showImageOnFail(android.R.color.white)
                .showImageOnLoading(android.R.color.white)
                .build();

        try {
            JSONObject fieldImage = eventImages.getJSONObject(position);

            imageView.setOnTouchListener(new OnSwipeTouchListener(GegonosProvoliSliderActivity.this) {
                public void onSwipeRight() {
                    // -1 image
                    try {
                        if((position-1)>= 0) {
                            position--;
                            JSONObject fieldImage = eventImages.getJSONObject(position);
                            imageLoader.displayImage(HttpTaskHandler.baseUrl + "images/events/" + fieldImage.getInt("id"), imageView, options);
                            textView.setText(fieldImage.getString("created_at"));
                        } else {
                            Toast.makeText(GegonosProvoliSliderActivity.this, "Τέλος φωτογραφιών", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                public void onSwipeLeft() {
                    // +1 image
                    try {
                        if((position+1)<= eventImages.length() ) {
                            position++;
                            JSONObject fieldImage = eventImages.getJSONObject(position);
                            imageLoader.displayImage(HttpTaskHandler.baseUrl + "images/events/" + fieldImage.getInt("id"), imageView, options);
                            textView.setText(fieldImage.getString("created_at"));
                        } else {
                            Toast.makeText(GegonosProvoliSliderActivity.this, "Τέλος φωτογραφιών", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            imageLoader.displayImage(HttpTaskHandler.baseUrl + "images/events/"+ fieldImage.getInt("id"), imageView, options);
            textView.setText(fieldImage.getString("created_at"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
