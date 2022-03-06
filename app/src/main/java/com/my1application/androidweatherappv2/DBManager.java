package com.my1application.androidweatherappv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import javax.security.auth.Subject;

public class DBManager {

    private MyDatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new MyDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, String desc,String temp,String dateTime,String wind,String pressure,String humidity,String sunrise,String sunset,String geo_cord) {

        ContentValues contentValue = new ContentValues();
        contentValue.put(MyDatabaseHelper.PLACE, name);
        contentValue.put(MyDatabaseHelper.DESC, desc);
        contentValue.put(MyDatabaseHelper.TEMP, temp);
        contentValue.put(MyDatabaseHelper.DATETIME, dateTime);
        contentValue.put(MyDatabaseHelper.WIND, wind);
        contentValue.put(MyDatabaseHelper.PRESSURE, pressure);
        contentValue.put(MyDatabaseHelper.HUMIDITY, humidity);
        contentValue.put(MyDatabaseHelper.SUNRISE, sunrise);
        contentValue.put(MyDatabaseHelper.SUNSET, sunset);
        contentValue.put(MyDatabaseHelper.GEO_CORD, geo_cord);
        int imp=database.update(MyDatabaseHelper.TABLE_NAME,contentValue,MyDatabaseHelper.PLACE+"="+ name,null);
        if (imp!=0){
            database.insert(MyDatabaseHelper.TABLE_NAME, null, contentValue);
        }else {
            Toast.makeText(context, "Duplicate Found", Toast.LENGTH_SHORT).show();
        }

    }

    public Cursor fetch() {
        String[] columns = new String[] { MyDatabaseHelper._ID, MyDatabaseHelper.PLACE, MyDatabaseHelper.DESC,MyDatabaseHelper.TEMP,MyDatabaseHelper.DATETIME,MyDatabaseHelper.HUMIDITY,MyDatabaseHelper.PRESSURE,MyDatabaseHelper.SUNRISE,MyDatabaseHelper.SUNSET,MyDatabaseHelper.GEO_CORD };
        Cursor cursor = database.query(MyDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public int update(long _id, String name, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDatabaseHelper.PLACE, name);
        contentValues.put(MyDatabaseHelper.DESC, desc);
        int i = database.update(MyDatabaseHelper.TABLE_NAME, contentValues, MyDatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(String id) {
        int test1 =database.delete(MyDatabaseHelper.TABLE_NAME, MyDatabaseHelper._ID + " = ?", new String[]{id});
        if (test1!=0){
            Log.d(id, "delete:success ################################");
        }else {
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
        }
    }

}