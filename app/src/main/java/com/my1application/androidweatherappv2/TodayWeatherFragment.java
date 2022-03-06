package com.my1application.androidweatherappv2;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.my1application.androidweatherappv2.Common.Common;
import com.my1application.androidweatherappv2.Model.WeatherResult;
import com.my1application.androidweatherappv2.Retrofit.IOpenWeatherMap;
import com.my1application.androidweatherappv2.Retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class TodayWeatherFragment extends Fragment  {


    ImageView img_weather,img_wind;
    TextView txt_city_name, txt_humidity, txt_sunrise, txt_sunset, txt_pressure, txt_temperature, txt_description, txt_date_time, txt_wind, txt_geo_coord;
    LinearLayout weather_panel;
    ProgressBar loading;
    private DBManager dbManager;
    FloatingActionButton btnDel,btnUp;
    EditText delInput,upId,upPlace,upDesc;



    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;


    static TodayWeatherFragment instance;


    public static TodayWeatherFragment getInstance() {
        if (instance== null)
            instance =new TodayWeatherFragment();
        return instance;
    }


    public TodayWeatherFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_today_weather, container, false);
        img_weather = (ImageView) itemView.findViewById(R.id.img_weather);
        img_wind=(ImageView) itemView.findViewById((R.id.img_wind));
        txt_city_name=(TextView)itemView.findViewById((R.id.txt_city_name));
        txt_humidity = (TextView) itemView.findViewById(R.id.txt_humidity);
        txt_sunrise = (TextView) itemView.findViewById(R.id.txt_sunrise);
        txt_sunset = (TextView) itemView.findViewById(R.id.txt_sunset);
        txt_pressure = (TextView) itemView.findViewById(R.id.txt_pressure);
        txt_temperature = (TextView) itemView.findViewById(R.id.txt_temperature);
        txt_description = (TextView)itemView.findViewById(R.id.txt_description);
        txt_date_time = (TextView)itemView.findViewById(R.id.txt_date_time);
        txt_wind = (TextView)itemView.findViewById(R.id.txt_wind);
        txt_geo_coord = (TextView)itemView.findViewById(R.id.txt_geo_coord);
        btnDel= (FloatingActionButton) itemView.findViewById(R.id.faDelete);
        btnUp= (FloatingActionButton)itemView.findViewById(R.id.faUpdate);
        delInput = new EditText(getContext());
        upId = new EditText(getContext());
        upPlace = new EditText(getContext());
        upDesc = new EditText(getContext());
        weather_panel = (LinearLayout)itemView.findViewById(R.id.weather_panel);
        loading = (ProgressBar)itemView.findViewById(R.id.loading);


        getWeatherInformation();
        dbManager  = new DBManager(getContext());
        dbManager.open();
        showDBDATA();
        del();
        update();
        return itemView;
    }

    public void getWeatherInformation() {
        compositeDisposable.add(mService.getweatherByLatlng(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.APP_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                               @Override
                               public void accept(WeatherResult weatherResult) throws Exception {
                                   Glide.with(TodayWeatherFragment.this).load("https://icon-library.com/images/weather-app-icon/weather-app-icon-4.jpg").into(img_weather);
                                   Glide.with(TodayWeatherFragment.this).load("https://icon-library.com/images/wind-blowing-icon/wind-blowing-icon-13.jpg").into(img_wind);
                                   txt_description.setText(new StringBuilder("Weather in ")
                                           .append(weatherResult.getName()).toString());
                                   txt_temperature.setText(new StringBuilder(
                                           String.valueOf(weatherResult.getMain().getTemp())).append("°C").toString());
                                   txt_date_time.setText(Common.convertUnixToDate(weatherResult.getDt()));
                                   txt_wind.setText(new StringBuilder(String.valueOf(weatherResult.getWind().getSpeed())).append("m/s").toString());
                                   txt_pressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure())).append( "hpa").toString());
                                   txt_humidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity())).append( "%").toString());
                                   txt_sunrise.setText(Common.convertexUnixToHour(weatherResult.getSys().getSunrise()));
                                   txt_sunset.setText(Common.convertexUnixToHour(weatherResult.getSys().getSunset()));
                                   txt_geo_coord.setText(new StringBuilder("[").append(weatherResult.getCoord().toString()).append("]").toString());
                                   weather_panel.setVisibility(View.VISIBLE);
                                   loading.setVisibility(View.GONE);
                                   dbManager.insert(weatherResult.getName(),weatherResult.getBase(),String.valueOf(weatherResult.getMain().getTemp()),String.valueOf(weatherResult.getDt()),String.valueOf(weatherResult.getWind().getSpeed()),String.valueOf((weatherResult.getMain().getPressure())),String.valueOf(weatherResult.getMain().getHumidity()),String.valueOf(weatherResult.getSys().getSunrise()),String.valueOf(weatherResult.getSys().getSunset()),String.valueOf(weatherResult.getCoord()));
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(getActivity(), ""+throwable.getMessage(),Toast.LENGTH_SHORT).show();
                               }
                           }
                )
        );
    }
    public void showDBDATA(){
        Glide.with(TodayWeatherFragment.this).load("https://icon-library.com/images/weather-app-icon/weather-app-icon-4.jpg").into(img_weather);
        Glide.with(TodayWeatherFragment.this).load("https://icon-library.com/images/wind-blowing-icon/wind-blowing-icon-13.jpg").into(img_wind);
        if (dbManager.fetch()!=null){
            Cursor cu= dbManager.fetch();
            weather_panel.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
            txt_description.setText(new StringBuilder("Weather in ")
                    .append(cu. getString(1)));
            txt_temperature.setText((cu.getString(3)));
            txt_date_time.setText(Common.convertUnixToDate(cu.getLong(4)));
            txt_wind.setText(cu.getString(5));
            txt_pressure.setText(cu.getString(6));
            txt_humidity.setText(cu.getString(5));
            txt_sunrise.setText(Common.convertUnixToDate(cu.getLong(7)));
            txt_sunset.setText(Common.convertUnixToDate(cu.getLong(8)));
            txt_geo_coord.setText(cu.getString(9));

            Log.d(cu.getString(1), "fetch: ############################################");
        }else {
            Log.d(getTag(), "No Data in Database");
        }
    }
    public void del(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete");
        builder.setIcon(R.drawable.common_google_signin_btn_icon_dark);
        builder.setMessage("Enter ID to be deleted");

        builder.setView(delInput);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String txt = delInput.getText().toString();
                dbManager.delete(txt);
                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog ad = builder.create();
        btnDel.setOnClickListener(new View.OnClickListener() {
            /* String place = delInput.getText().toString();*/
            @Override
            public void onClick(View v) {
                ad.show();
                // calling a method to delete our course.

            }
        });
    }
    public void update(){
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update");
        builder.setIcon(R.drawable.ic_menu_black_24dp);
        builder.setMessage("Enter Details to be Updated");

        upId.setHint("ID");
        layout.addView(upId);
        upPlace.setHint("Place");
        layout.addView(upPlace);
        upDesc.setHint("Description");
        layout.addView(upDesc);
        builder.setView(layout);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                long id = Integer.parseInt(upId.getText().toString());
                String uPlace = upPlace.getText().toString();
                String uDesc = upDesc.getText().toString();
                dbManager.update(id,uPlace,uDesc);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog ad = builder.create();
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.show();
                // calling a method to delete our course.

            }
        });
    }

// adding on click listener for delete button to delete our course.

    public void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }

    public void onStop(){
        compositeDisposable.clear();
        super.onStop();
    }


}
