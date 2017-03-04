package com.example.tania_nikos.remotesensing.Helpers;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.tania_nikos.remotesensing.EditXorafiActivity;
import com.example.tania_nikos.remotesensing.Http.AsyncResponse;
import com.example.tania_nikos.remotesensing.Http.HttpDeleteTask;
import com.example.tania_nikos.remotesensing.Http.HttpGetTask;
import com.example.tania_nikos.remotesensing.Http.HttpTaskHandler;
import com.example.tania_nikos.remotesensing.Http.Response;
import com.example.tania_nikos.remotesensing.XorafiaActivity;

import java.io.File;

/**
 * Created by Tania-Nikos on 12/2/2017.
 */


public class ListDeleteHandler {

    /**
     * Types of delete handling
     */
    public static final String fields = "fields";
    public static final String events = "events";

    /**
     * Data
     */
    protected String type;
    protected Integer[] ids;
    protected String[] filepaths;
    protected AsyncResponse asyncResponse;

    /**
     * Constructor
     *
     * @param type
     * @param ids
     * @param filepaths
     */
    public ListDeleteHandler(String type, Integer[] ids, String[] filepaths, AsyncResponse asyncResponse) {
        this.type = type;
        this.ids = ids;
        this.filepaths = filepaths;
        this.asyncResponse = asyncResponse;
    }

    /**
     * Delete item in position
     *
     * @param position
     */
    public void deleteEntry(int position)
    {

        System.out.println("in deleteEntry at : "+ HttpTaskHandler.baseUrl + "images/" + this.type + "/" + this.ids[position]);
        HttpGetTask delete = new HttpGetTask(HttpTaskHandler.baseUrl + "images/" + this.type + "/" + this.ids[position] + "/delete", asyncResponse);
        new HttpTaskHandler().execute(delete);
        //kai file delete
        File file = new File(this.filepaths[position]);
        if (file.exists()) {
            if (file.delete()) {
                Log.d("DELETE : ","file Deleted :" + file.getPath());
            } else {
                Log.d("DELETE : ","file not Deleted :" + file.getPath());
            }
        }
    }
}
