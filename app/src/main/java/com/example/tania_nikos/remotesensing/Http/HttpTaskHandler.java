package com.example.tania_nikos.remotesensing.Http;

import android.os.AsyncTask;

import java.io.IOException;

/**
 * Created by Tania-Nikos on 2/2/2017.
 */

public class HttpTaskHandler extends AsyncTask< HttpObject, Integer, Integer>
{
    public static final String baseUrl = "http://192.168.1.9:8000/api/";

    @Override
    protected Integer doInBackground(HttpObject... httpObjects) {
        for (HttpObject httpObject:httpObjects) {
            try {
                httpObject.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
