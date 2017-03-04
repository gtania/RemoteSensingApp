package com.example.tania_nikos.remotesensing.Helpers;

import android.util.Log;

import com.example.tania_nikos.remotesensing.Http.AsyncResponse;
import com.example.tania_nikos.remotesensing.Http.HttpDeleteTask;
import com.example.tania_nikos.remotesensing.Http.HttpGetTask;
import com.example.tania_nikos.remotesensing.Http.HttpTaskHandler;

import java.io.File;

/**
 * Created by Tania-Nikos on 19/2/2017.
 */

public class CommentsDeleteHandler {

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
    protected String[] comments;
    protected AsyncResponse asyncResponse;

    public CommentsDeleteHandler(String type, Integer[] ids, String[] comments, AsyncResponse asyncResponse) {
        this.type = type;
        this.ids = ids;
        this.comments = comments;
        this.asyncResponse = asyncResponse;
    }
    /**
     * Delete item in position
     *
     * @param position
     */
    public void deleteEntry(int position) {

        Log.d("APP DELETE ", "in deleteEntry at : "+ HttpTaskHandler.baseUrl + "images/" + this.type + "/" + this.ids[position]);
        HttpDeleteTask delete = new HttpDeleteTask(HttpTaskHandler.baseUrl + "images/" + this.type + "/comments/"+ this.ids[position], asyncResponse);
        new HttpTaskHandler().execute(delete);
    }

}
