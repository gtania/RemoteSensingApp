package com.example.tania_nikos.remotesensing.ActivitiesProbolis;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tania_nikos.remotesensing.Helpers.InternetHandler;
import com.example.tania_nikos.remotesensing.Helpers.OnSwipeTouchListener;
import com.example.tania_nikos.remotesensing.Http.AsyncResponse;
import com.example.tania_nikos.remotesensing.Http.HttpGetTask;
import com.example.tania_nikos.remotesensing.Http.HttpTaskHandler;
import com.example.tania_nikos.remotesensing.Http.Response;
import com.example.tania_nikos.remotesensing.MainActivity;
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

public class GegonosProvoliSliderActivity extends Activity {

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

    protected JSONArray commentsArray;
    protected TextView commentTextView;
    protected String commentText;

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
        InternetHandler.checkInternet(this);

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
            //latest_comment_gegonos
            this.setComment(fieldImage.getInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public void home(View view)
    {
        Intent intent = new Intent(GegonosProvoliSliderActivity.this, MainActivity.class);
        startActivity(intent);
    }


    public void setComment(int id)
    {
        HttpGetTask get = new HttpGetTask(HttpTaskHandler.baseUrl + "images/events/"+ id +"/getcomments", new AsyncResponse() {
            public void processFinish(Response response) {

                try {
                    //parse response
                    commentsArray = new JSONArray(response.data);
                    commentTextView = (TextView) findViewById(R.id.latest_comment_gegonos);
                    if(commentsArray.length()>0) {
                        JSONObject comment = commentsArray.getJSONObject(commentsArray.length() - 1);
                        commentText = comment.getString("text");
                        runOnUiThread(new Runnable() {
                            public void run() {
                                commentTextView.setText(commentText);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                commentTextView.setText("");
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        new HttpTaskHandler().execute(get);
    }
}
