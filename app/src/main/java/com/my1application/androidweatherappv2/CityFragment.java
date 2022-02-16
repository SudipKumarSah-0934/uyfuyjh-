package com.my1application.androidweatherappv2;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.label305.asynctask.SimpleAsyncTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.my1application.androidweatherappv2.Common.Common;
import com.my1application.androidweatherappv2.Model.WeatherResult;
import com.my1application.androidweatherappv2.R;
import com.my1application.androidweatherappv2.Retrofit.IOpenWeatherMap;
import com.my1application.androidweatherappv2.Retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

//import Common.Common;
//import Model.weatherResult;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class CityFragment extends Fragment {

    //  public  CityFragment RetrofitClient;
    private List<String> lstCities;
    private MaterialSearchBar searchBar;

    ImageView img_weather;
    TextView txt_city_name, txt_humidity, txt_sunrise, txt_sunset, txt_pressure, txt_temperature, txt_description, txt_date_time, txt_wind, txt_geo_coord;
    LinearLayout weather_panel;
    ProgressBar loading;


    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;


    static CityFragment instance;

    public static CityFragment getInstance() {
        if (instance== null)
            instance =new CityFragment();
        return instance;
    }


    public CityFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit= RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_city, container, false);

        img_weather = (ImageView) itemView.findViewById(R.id.img_weather);
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

        weather_panel = (LinearLayout)itemView.findViewById(R.id.weather_panel);
        loading = (ProgressBar)itemView.findViewById(R.id.loading);

        searchBar = (MaterialSearchBar) itemView.findViewById(R.id.searchBar);
        searchBar.setEnabled(false);

        new LoadCities().execute();
        return itemView;
    }

    private class LoadCities extends SimpleAsyncTask<List<String>> {

        @Override
        protected List<String> doInBackground() {
            lstCities =new ArrayList<>();
            try {
                StringBuilder builder=new StringBuilder();
                InputStream is = getResources().openRawResource(R.raw.city_list);
                GZIPInputStream gzipInputStream = new GZIPInputStream(is);

                InputStreamReader reader = new InputStreamReader(gzipInputStream);
                BufferedReader in =new BufferedReader(reader);

                String readed;
                while ((readed = in.readLine())!=null)
                    builder.append(readed);
                lstCities =new Gson().fromJson(builder.toString(),new TypeToken<List<String>>(){}.getType());

            } catch (IOException e) {
                e.printStackTrace();
            }

            return lstCities;
        }

        @Override
        protected void onSuccess(final List<String> listCity) {
            super.onSuccess(listCity);


            searchBar.setEnabled(true);
            searchBar.addTextChangeListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    List<String> suggest = new ArrayList<>();
                    for (String search : listCity) {
                        if (search.toLowerCase().contains(searchBar.getText().toLowerCase()))
                            suggest.add(search);
                    }
                    searchBar.setLastSuggestions(suggest);

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
                @Override
                public void onSearchStateChanged(boolean enabled) {
                }


                @Override
                public void onSearchConfirmed(CharSequence text) {
                    getWeatherInformation(text.toString());
                    searchBar.setLastSuggestions(listCity);

                }

                @Override
                public void onButtonClicked(int buttonCode) {

                }
            });
            searchBar.setLastSuggestions(listCity);
            loading.setVisibility(View.GONE);
        }

    }


    private void getWeatherInformation(String cityName) {

        compositeDisposable.add(mService.getWeatherByCityName(cityName,
                Common.APP_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {

                    public void accept(WeatherResult weatherResult) throws Exception {
                        Picasso.get().load(new StringBuilder()
                                .append(weatherResult.getWeather().get(0).getIcon())
                                .append(".png").toString()).into(img_weather);
                        //load information
                        txt_city_name.setText(weatherResult.getName());
                        txt_description.setText(new StringBuilder("Weather in")
                                .append(weatherResult.getName()).toString());
                        txt_temperature.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getTemp())).append("°C").toString());
                        txt_date_time.setText(Common.convertUnixToDate(weatherResult.getDt()));
                        txt_pressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure())).append("hpa").toString());
                        txt_humidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity())).append("%").toString());
                        txt_sunrise.setText(Common.convertexUnixToHour(weatherResult.getSys().getSunrise()));
                        txt_sunset.setText(Common.convertexUnixToHour(weatherResult.getSys().getSunset()));
                        txt_geo_coord.setText(new StringBuilder(weatherResult.getCoord().toString()).toString());

                        //displaypanel
                        weather_panel.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);



                    }
                }, new Consumer<Throwable>(){
                    public void accept(Throwable throwable) throws Exception{
                        Toast.makeText(getActivity(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
    }