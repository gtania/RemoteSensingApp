package com.example.tania_nikos.remotesensing.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.tania_nikos.remotesensing.Database.DBHandler;

/**
 * Created by Tania-Nikos on 4/5/2017.
 */

public class Event extends Model{

    DBHandler handler;

    public Event(Context context) {
        this.handler = DBHandler.getInstance(context);
    }

    public Cursor getEvents()
    {
        return  this.handler
                .getReadableDatabase()
                .rawQuery("SELECT * FROM " + DBHandler.TABLE_EVENTS, null);
    }

    public long createEvent(ContentValues data)
    {
        String now = this.getDateNow();

        data.put("created_at", now);
        data.put("updated_at", now);
        return this.handler.getWritableDatabase().insert(DBHandler.TABLE_EVENTS, null, data);
    }

    public int updateEvent(int id, ContentValues data)
    {
        String now = this.getDateNow();
        data.put("updated_at", now);

        return this.handler.getReadableDatabase().update(DBHandler.TABLE_EVENTS, data, "id=" + id, null);
    }

    public long addEventImage(ContentValues data)
    {
        String now = this.getDateNow();

        data.put("created_at", now);
        data.put("updated_at", now);

        return this.handler.getWritableDatabase().insert(DBHandler.TABLE_EVENTS_IMAGES, null, data);
    }
    public Cursor getImagesFromEventWithId(int id)
    {
        return this.handler
                .getReadableDatabase()
                .rawQuery(
                        "SELECT * FROM " + DBHandler.TABLE_EVENTS_IMAGES +" WHERE " + DBHandler.EVENT_IMAGES_FIELD_ID + "=?",
                        new String[]{Integer.toString(id)}
                );

    }
}
