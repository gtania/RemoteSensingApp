package com.example.tania_nikos.remotesensing.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

/**
 * Created by Tania-Nikos on 3/4/2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "remotesensing.db";

    // TABLE NAMES
    public static final String TABLE_FIELDS = "fields";
    public static final String TABLE_FIELD_COMMENTS = "field_comments";
    public static final String TABLE_FIELD_IMAGES = "field_images";
    public static final String TABLE_EVENTS = "events";
    public static final String TABLE_EVENTS_COMMENTS = "event_comments";
    public static final String TABLE_EVENTS_IMAGES = "event_images";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";

    // Fiels TABLE
    // id
    public static final String FIELDS_DEVICE_ID = "device_id"; // TODO: MAYBE NOT NEEDED ANY MORE
    public static final String FIELDS_ONOMA_XORAFIOU = "onoma_xorafiou";
    public static final String FIELDS_PERIOXI_XORAFIOU = "perioxi_xorafiou";
    public static final String FIELDS_ETOS_KALIERGIAS = "etos_kaliergias";
    public static final String FIELDS_ONOMA_KALIERGITI = "onoma_kaliergiti";
    public static final String FIELDS_EIDOS = "eidos";
    // created at
    // updated at

    // Field Comments TABLE
    // id
    public static final String FIELD_COMMENTS_FIELD_IMAGE_ID = "field_image_id";
    public static final String FIELD_COMMENTS_TEXT = "text";
    // created at
    // updated at

    // Field images TABLE
    // id
    public static final String FIELD_IMAGES_FIELD_ID = "field_id";
    //public static final String FIELD_IMAGES_FILENAME = "filename";
    public static final String FIELD_IMAGES_FILEPATH = "filepath";
    // created at
    // updated at

    // Events TABLE
    public static final String EVENTS_DEVICE_ID = "device_id"; // TODO: MAYBE NOT NEEDED ANY MORE
    public static final String EVENTS_ONONA_GEGONOTOS = "onoma_gegonotos";
    public static final String EVENTS_PERIGRAFI_GEGONOTOS = "perigrafi_gegonotos";
    public static final String EVENTS_ONOMA_XORAFIOU = "onoma_xorafiou";
    public static final String EVENTS_PERIOXI_XORAFIOU = "perioxi_xorafiou";
    public static final String EVENTS_ETOS_KALIERGIAS = "etos_kaliergias";
    public static final String EVENTS_ONOMA_KALIERGITI = "onoma_kaliergiti";
    public static final String EVENTS_EIDOS = "eidos";
    // created at
    // updated at

    // Event comments TABLE
    //id
    public static final String EVENT_COMMENTS_EVENT_IMAGE_ID = "event_image_id";
    public static final String EVENT_COMMENTS_TEXT = "text";
    // created at
    // updated at

    // Event images TABLE
    // id
    public static final String EVENT_IMAGES_FIELD_ID = "event_id";
    public static final String EVENT_IMAGES_FILENAME = "filename";
    public static final String EVENT_IMAGES_FILEPATH = "filepath";
    // created at
    // updated at


    // Database creation sql statement
    // CREATE FIELDS TABLE
    private static final String CREATE_FIELDS  = "create table " + TABLE_FIELDS + "( "
            + KEY_ID                + " integer primary key autoincrement, "
            + FIELDS_DEVICE_ID      + " text not null,"
            + FIELDS_ONOMA_XORAFIOU + " text not null,"
            + FIELDS_PERIOXI_XORAFIOU + " text not null,"
            + FIELDS_ETOS_KALIERGIAS + " text not null,"
            + FIELDS_ONOMA_KALIERGITI + " text not null,"
            + FIELDS_EIDOS + " text not null,"
            + KEY_CREATED_AT + " datetime,"
            + KEY_UPDATED_AT + " datetime"
            + ");";

    // CREATE FIELD COMMENTS TABLE
    private static final String CREATE_FIELD_COMMENTS  = "create table " + TABLE_FIELD_COMMENTS + "( "
            + KEY_ID                + " integer primary key autoincrement, "
            + FIELD_COMMENTS_FIELD_IMAGE_ID      + " integer not null,"
            + FIELD_COMMENTS_TEXT + " text not null,"
            + KEY_CREATED_AT + " datetime,"
            + KEY_UPDATED_AT + " datetime,"
            + "FOREIGN KEY ("+FIELD_COMMENTS_FIELD_IMAGE_ID+") REFERENCES "+TABLE_FIELD_IMAGES+"("+KEY_ID+") ON DELETE CASCADE"
            + ");";

    // CREATE FIELD IMAGES TABLE
    private static final String CREATE_FIELD_IMAGES  = "create table " + TABLE_FIELD_IMAGES + "( "
            + KEY_ID                + " integer primary key autoincrement, "
            + FIELD_IMAGES_FIELD_ID + " integer not null,"
          //  + FIELD_IMAGES_FILENAME + " text not null,"
            + FIELD_IMAGES_FILEPATH + " text not null,"
            + KEY_CREATED_AT + " datetime,"
            + KEY_UPDATED_AT + " datetime,"
            + "FOREIGN KEY ("+FIELD_IMAGES_FIELD_ID+") REFERENCES "+ TABLE_FIELDS+"("+KEY_ID+") ON DELETE CASCADE"
            + ");";

    // CREATE EVENTS TABLE
    private static final String CREATE_EVENTS  = "create table " + TABLE_EVENTS + "( "
            + KEY_ID                + " integer primary key autoincrement, "
            + EVENTS_DEVICE_ID      + " text not null,"
            + EVENTS_ONONA_GEGONOTOS + " text not null,"
            + EVENTS_PERIGRAFI_GEGONOTOS + " text not null,"
            + EVENTS_ONOMA_XORAFIOU + " text not null,"
            + EVENTS_PERIOXI_XORAFIOU + " text not null,"
            + EVENTS_ETOS_KALIERGIAS + " text not null,"
            + EVENTS_ONOMA_KALIERGITI + " text not null,"
            + EVENTS_EIDOS + " text not null,"
            + KEY_CREATED_AT + " datetime,"
            + KEY_UPDATED_AT + " datetime"
            + ");";

    // CREATE EVENTS COMMENTS TABLE
    private static final String CREATE_EVENT_COMMENTS  = "create table " + TABLE_EVENTS_COMMENTS + "( "
            + KEY_ID                + " integer primary key autoincrement, "
            + EVENT_COMMENTS_EVENT_IMAGE_ID      + " integer not null,"
            + EVENT_COMMENTS_TEXT + "text not null,"
            + KEY_CREATED_AT + " datetime,"
            + KEY_UPDATED_AT + " datetime,"
            + "FOREIGN KEY ("+EVENT_COMMENTS_EVENT_IMAGE_ID+") REFERENCES "+ TABLE_EVENTS_IMAGES+"("+KEY_ID+") ON DELETE CASCADE"
            + ");";

    // CREATE EVENT IMAGES TABLE
    private static final String CREATE_EVENT_IMAGES  = "create table " + TABLE_EVENTS_IMAGES + "( "
            + KEY_ID                + " integer primary key autoincrement, "
            + EVENT_IMAGES_FIELD_ID + " integer not null,"
            + EVENT_IMAGES_FILENAME + " text not null,"
            + EVENT_IMAGES_FILEPATH + "text not null,"
            + KEY_CREATED_AT + " datetime,"
            + KEY_UPDATED_AT + " datetime,"
            + "FOREIGN KEY ("+EVENT_IMAGES_FIELD_ID+") REFERENCES "+ TABLE_EVENTS+"("+KEY_ID+") ON DELETE CASCADE"
            + ");";

    // CONSTRAINS
    private static final  String CONSTRAIN_FIELD_COMMENTS = "ALTER TABLE " + TABLE_FIELD_COMMENTS +" ADD CONSTRAINT field_comments_field_image_id_foreign FOREIGN KEY ("+FIELD_COMMENTS_FIELD_IMAGE_ID+") REFERENCES "+TABLE_FIELD_IMAGES+"("+KEY_ID+") ON DELETE CASCADE;";
    private static final  String CONSTRAIN_FIELD_IMAGES = "ALTER TABLE " + TABLE_FIELD_IMAGES +" ADD CONSTRAINT field_images_field_id_foreign FOREIGN KEY ("+FIELD_IMAGES_FIELD_ID+") REFERENCES "+ TABLE_FIELDS+"("+KEY_ID+") ON DELETE CASCADE;";
    private static final  String CONSTRAIN_EVENT_COMMENTS = "ALTER TABLE " + TABLE_EVENTS_COMMENTS +" ADD CONSTRAINT event_comments_event_image_id_foreign FOREIGN KEY ("+EVENT_COMMENTS_EVENT_IMAGE_ID+") REFERENCES "+ TABLE_EVENTS_IMAGES+"("+KEY_ID+") ON DELETE CASCADE;";
    private static final  String CONSTRAIN_EVENT_IMAGES = "ALTER TABLE " + TABLE_EVENTS_IMAGES +" ADD CONSTRAINT event_images_event_id_foreign FOREIGN KEY ("+EVENT_IMAGES_FIELD_ID+") REFERENCES "+ TABLE_EVENTS+"("+KEY_ID+") ON DELETE CASCADE;";


    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Check if the database exist and can be read.
     *
     * @return true if it exists and can be read, false if it doesn't
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DATABASE_NAME, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }
        return checkDB != null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        File dbtest =new File(DATABASE_NAME);

        if (!dbtest.exists()) {
            Log.i(" CREATE DATABASE ", "create new");
            db.execSQL(CREATE_FIELDS);
            db.execSQL(CREATE_FIELD_COMMENTS);
            db.execSQL(CREATE_FIELD_IMAGES);
            db.execSQL(CREATE_EVENTS);
            db.execSQL(CREATE_EVENT_COMMENTS);
            db.execSQL(CREATE_EVENT_IMAGES);
        } else {
            Log.i(" CREATE DATABASE ", "exists");

        }

    }
    private static DBHandler sInstance;

    public static synchronized DBHandler getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBHandler(context, DBHandler.DATABASE_NAME, null, DBHandler.DATABASE_VERSION);
        }
        return sInstance;
    }
    public Cursor getTables()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHandler.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELD_COMMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELD_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS_COMMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS_IMAGES);

        onCreate(db);
    }
}
