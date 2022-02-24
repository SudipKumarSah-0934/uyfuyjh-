package com.my1application.androidweatherappv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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

    public void insert(String name, String desc) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(MyDatabaseHelper.SUBJECT, name);
        contentValue.put(MyDatabaseHelper.DESC, desc);
        database.insert(MyDatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { MyDatabaseHelper._ID, MyDatabaseHelper.SUBJECT, MyDatabaseHelper.DESC };
        Cursor cursor = database.query(MyDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String name, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDatabaseHelper.SUBJECT, name);
        contentValues.put(MyDatabaseHelper.DESC, desc);
        int i = database.update(MyDatabaseHelper.TABLE_NAME, contentValues, MyDatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(MyDatabaseHelper.TABLE_NAME, MyDatabaseHelper._ID + "=" + _id, null);
    }

}