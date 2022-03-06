package com.my1application.androidweatherappv2;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class SqliteDemo extends AppCompatActivity {
    EditText firstText;
    DBManager dbManager;
    static SqliteDemo instance;
    public SqliteDemo() {
    }

    public static SqliteDemo getInstance() {
        if (instance== null)
            instance =new SqliteDemo();
        return instance;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offlinefrag);
        firstText =(EditText) findViewById(R.id.offlineFrag);
        StringBuilder stringBuilder =  new StringBuilder();

        Cursor cu= dbManager.fetch();
        while(cu.moveToNext()){
            firstText.setText("se"+cu.getString(1));
            stringBuilder.append(cu.getString(1)+"######"+cu.getString(2));
            Log.d(cu.getString(1), "showDBDATA: ##############################################");
        }
    }
}
