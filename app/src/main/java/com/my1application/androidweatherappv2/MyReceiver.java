package com.my1application.androidweatherappv2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        try {
            if (isOnline(context)) {
                Toast.makeText(context, "Network Connected ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "No network", Toast.LENGTH_SHORT).show();
                TodayWeatherFragment.getInstance().showDBDATA();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    public boolean isOnline(Context context){
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return (networkInfo!=null && networkInfo.isConnected());
        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }


}