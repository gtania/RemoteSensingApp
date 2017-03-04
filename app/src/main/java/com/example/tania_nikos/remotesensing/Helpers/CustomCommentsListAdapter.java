package com.example.tania_nikos.remotesensing.Helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tania_nikos.remotesensing.ActivitiesProbolis.CommentsActivity;
import com.example.tania_nikos.remotesensing.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

/**
 * Created by Tania-Nikos on 19/2/2017.
 */

public class CustomCommentsListAdapter extends ArrayAdapter<String> {

        private final Activity context;

        private final String[] comments;
        private final CommentsDeleteHandler deleteHandler;
        int positionToRemove;
        /**
         * Constructor
         *
         * @param context
         * @param itemname
         */
        public CustomCommentsListAdapter(Activity context, String[] itemname, CommentsDeleteHandler deleteHandler) {
            super(context, R.layout.comments_provoli_list, itemname);
            // TODO Auto-generated constructor stub

            this.context=context;
            this.comments=itemname;
            this.deleteHandler = deleteHandler;
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
            Log.d("APP", "After setting row View");
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.comments_provoli_list ,null,true);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.comment_text);
            txtTitle.setText(comments[position]);
            Button deleteBtn = (Button) rowView.findViewById(R.id.comments_delete_btn);
            Log.d("APP", "After setting row View");
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

            return rowView;

        };

        public void removeItem(int position){
            deleteHandler.deleteEntry(position);

        }
    }


