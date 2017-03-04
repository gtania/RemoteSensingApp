package com.example.tania_nikos.remotesensing.ActivitiesProbolis;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tania_nikos.remotesensing.Helpers.CommentsDeleteHandler;
import com.example.tania_nikos.remotesensing.Helpers.CustomCommentsListAdapter;
import com.example.tania_nikos.remotesensing.Helpers.InternetHandler;
import com.example.tania_nikos.remotesensing.Helpers.OnSwipeTouchListener;
import com.example.tania_nikos.remotesensing.Http.AsyncResponse;
import com.example.tania_nikos.remotesensing.Http.HttpGetTask;
import com.example.tania_nikos.remotesensing.Http.HttpTaskHandler;
import com.example.tania_nikos.remotesensing.Http.Response;
import com.example.tania_nikos.remotesensing.MainActivity;
import com.example.tania_nikos.remotesensing.R;
import com.example.tania_nikos.remotesensing.XorafiaActivity;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class XwrafiProboliSliderActivity extends Activity {

    /**
     * Data variables
     */
    protected JSONArray fieldImages;
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
    protected String commentText;
    protected TextView commentTextView;

    /**
     * Constructor
     * Load Big Single image
     * Set up slide events
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xwrafi_proboli_slider);
        InternetHandler.checkInternet(this);

        Intent intent = getIntent();
        String jsonArray = intent.getStringExtra("fieldImages");
        position = intent.getIntExtra("position", 0);

        try {
             fieldImages = new JSONArray(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        imageView = (ImageView) findViewById(R.id.image_view_slider);
        textView = (TextView) findViewById(R.id.slider_image_date);

        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(false).cacheInMemory(false)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP

        imageLoader = ImageLoader.getInstance();
        int fallback = 0 ;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .showImageForEmptyUri(android.R.color.white)
                .showImageOnFail(android.R.color.white)
                .showImageOnLoading(android.R.color.white)
                .build();



        try {
            JSONObject fieldImage = fieldImages.getJSONObject(position);
            String url = HttpTaskHandler.baseUrl + "images/fields/"+ fieldImage.getInt("id");
            MemoryCacheUtils.removeFromCache( url, ImageLoader.getInstance().getMemoryCache());
            DiskCacheUtils.removeFromCache(url, ImageLoader.getInstance().getDiskCache());
            imageLoader.clearMemoryCache();

            Log.d("APP", "image with id " + Integer.toString(fieldImage.getInt("id")));
            imageLoader.displayImage(HttpTaskHandler.baseUrl + "images/fields/"+ fieldImage.getInt("id"), imageView, options);
            textView.setText(fieldImage.getString("created_at"));
            this.setComment(fieldImage.getInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void home(View view)
    {
        Intent intent = new Intent(XwrafiProboliSliderActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void setComment(int id)
    {
        HttpGetTask get = new HttpGetTask(HttpTaskHandler.baseUrl + "images/fields/"+ id +"/comments", new AsyncResponse() {
            public void processFinish(Response response) {

                try {
                    //parse response
                    commentsArray = new JSONArray(response.data);
                    commentTextView = (TextView) findViewById(R.id.latest_comment);
                    if(commentsArray.length()>0) {
                        JSONObject comment = commentsArray.getJSONObject(commentsArray.length() -1 );
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
