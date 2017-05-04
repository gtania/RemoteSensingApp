package com.example.tania_nikos.remotesensing.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.tania_nikos.remotesensing.Database.DBHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;

/**
 * Created by Tania-Nikos on 3/5/2017.
 */

public class Field {

    DBHandler handler;

    public Field(Context context) {
         this.handler = DBHandler.getInstance(context);
    }

    /**
     * Create field entry
     * @param data
     * @return
     */
    public long createField(ContentValues data)
    {
        String now = this.getDateNow();

        data.put("created_at", now);
        data.put("updated_at", now);
        return this.handler.getWritableDatabase().insert("fields", null, data);
    }

    public Cursor getFields()
    {
        Log.i("INSODE DAO  ",  "Table Name=> ");

        return this.handler.getReadableDatabase().rawQuery("SELECT * FROM fields", null);
    }

    public int updateField(int id, ContentValues data)
    {
        String now = this.getDateNow();
        data.put("updated_at", now);

        return this.handler.getReadableDatabase().update("fields", data, "id=" + id, null);
    }

    /**
     * Get date now
     *
     * @return
     */
    protected String getDateNow()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

}
