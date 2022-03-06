package com.my1application.androidweatherappv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.my1application.androidweatherappv2.Model.WeatherResult;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "Info";

    // Table columns
    public static final String _ID = "_id";
    public static final String PLACE = "place";
    public static final String DESC = "description";
    public static final String TEMP = "temperature";
    public static final String DATETIME = "datetime";
    public static final String WIND = "WIND";
    public static final String PRESSURE = "pressure";
    public static final String HUMIDITY = "humidity";
    public static final String SUNRISE = "sunrise";
    public static final String SUNSET = "sunset";
    public static final String GEO_CORD = "geo_coord";

    // Database Information
    static final String DB_NAME = "Weather.DB";

    // database version
    static final int DB_VERSION = 14;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PLACE + " TEXT NOT NULL, " + DESC + " TEXT, " + TEMP + " TEXT, " + DATETIME + " TEXT, " + WIND + " TEXT, " + PRESSURE + " TEXT, " + HUMIDITY + " TEXT, " + SUNRISE + " TEXT, " + SUNSET + " TEXT, " +
            GEO_CORD + " TEXT);";

    public MyDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
