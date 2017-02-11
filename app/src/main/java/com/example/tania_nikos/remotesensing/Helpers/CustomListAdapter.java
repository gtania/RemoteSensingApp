package com.example.tania_nikos.remotesensing.Helpers;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tania_nikos.remotesensing.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * Created by Tania-Nikos on 10/2/2017.
 */

public class CustomListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] itemname;
    private final String[] urls;

    /**
     * Constructor
     *
     * @param context
     * @param itemname
     * @param urls
     */
    public CustomListAdapter(Activity context, String[] itemname, String[] urls) {
        super(context, R.layout.xwrafia_proboli_list, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.urls=urls;

        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }

    /**
     * Get View with data
     *
     * @param position
     * @param view
     * @param parent
     * @return
     */
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.xwrafia_proboli_list, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        ImageLoader imageLoader = ImageLoader.getInstance();
        int fallback = 0 ;
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .showImageForEmptyUri(android.R.color.white)
                .showImageOnFail(android.R.color.white)
                .showImageOnLoading(android.R.color.white)
                .build();



        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.context).build();
        ImageLoader.getInstance().init(config);

        String datetime = itemname[position];
        String[] datetime_splited = datetime.split(" ");
        txtTitle.setText(datetime_splited[0]);
        //download and display image from url
        imageLoader.displayImage(urls[position], imageView, options);
        extratxt.setText(datetime_splited[1]);
        return rowView;

    };
}
