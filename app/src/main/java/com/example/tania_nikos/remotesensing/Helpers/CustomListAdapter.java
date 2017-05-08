package com.example.tania_nikos.remotesensing.Helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tania_nikos.remotesensing.ActivitiesProbolis.XwrafiProboliActivity;
import com.example.tania_nikos.remotesensing.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.io.File;

import static com.example.tania_nikos.remotesensing.R.id.url;

/**
 * Created by Tania-Nikos on 10/2/2017.
 */

public class CustomListAdapter extends ArrayAdapter<String> {
    private final Activity context;

    private final String[] itemname;
    private final String[] urls;
    private final ListDeleteHandler deleteHandler;
    int positionToRemove;
    /**
     * Constructor
     *
     * @param context
     * @param itemname
     * @param urls
     */
    public CustomListAdapter(Activity context, String[] itemname, String[] urls, ListDeleteHandler deleteHandler) {
        super(context, R.layout.xwrafia_proboli_list, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.urls=urls;
        this.deleteHandler = deleteHandler;

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
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.xwrafia_proboli_list, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);
        Button deleteBtn = (Button) rowView.findViewById(R.id.provoli_list_delete_button);
        deleteBtn.setTag(position);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                positionToRemove = (int)v.getTag(); //get the position of the view to delete stored in the tag

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                removeItem(positionToRemove);
                                notifyDataSetChanged(); //remove the item
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Είστε σίγουροι για την διαγραφή?").setPositiveButton("Ναι", dialogClickListener)
                        .setNegativeButton("Οχι", dialogClickListener).show();
            }
        });

        ImageLoader imageLoader = ImageLoader.getInstance();
        int fallback = 0 ;
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(false) // Remove CACHE STUFF
                .resetViewBeforeLoading(true)
                .considerExifParams(true)
                .showImageForEmptyUri(android.R.color.white)
                .showImageOnFail(android.R.color.white)
                .showImageOnLoading(android.R.color.white)
                .build();


        MemoryCacheUtils.removeFromCache( urls[position], ImageLoader.getInstance().getMemoryCache());
        DiskCacheUtils.removeFromCache(urls[position], ImageLoader.getInstance().getDiskCache());
        imageLoader.clearMemoryCache();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.context).build();
        ImageLoader.getInstance().init(config);

        String datetime = itemname[position];
        String[] datetime_splited = datetime.split(" ");
        txtTitle.setText(datetime_splited[0]);
        //download and display image from url
//        imageLoader.displayImage(urls[position], imageView, options);
        File imgFile = new File(urls[position]);
        Bitmap myBitmap = BitmapFactory.decodeFile(urls[position]);
        imageView.setImageBitmap(myBitmap);

        Log.d("LOADING URLS", urls[position]);
        extratxt.setText(datetime_splited[1]);
        return rowView;

    };

    public void removeItem(int position){
        deleteHandler.deleteEntry(position);

        //convert array to ArrayList, delete item and convert back to array
//        ArrayList<String> a = new ArrayList<>(Arrays.asList(s1));
//        a.remove(position);
//        String[] s = new String[a.size()];
//        s=a.toArray(s);
//        s1 = s;
//        System.out.println("In removeItemFunc");
//        XwrafiProboliActivity.deleteItem(ListDeleteHandler.fields, position);
//        notifyDataSetChanged(); //refresh your listview based on new data

    }
}
