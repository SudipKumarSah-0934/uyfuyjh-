package com.my1application.androidweatherappv2.Retrofit;

import com.my1application.androidweatherappv2.Model.WeatherForecastResult;
import com.my1application.androidweatherappv2.Model.WeatherResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMap {
    @GET("weather")
    Observable<WeatherResult> getweatherByLatlng(@Query("lat") String lat,
                                                 @Query("lon") String lng,
                                                 @Query("appid") String appid,
                                                 @Query("units") String unit);

    @GET("weather")
    Observable<WeatherResult> getWeatherByCityName(@Query("q") String CityName,
                                                   @Query("appid") String appid,
                                                   @Query("units") String unit);


    @GET("forecast")
    Observable<WeatherForecastResult> getForecastWeatherByLatlng(@Query("lat") String lat,
                                                                 @Query("lon") String lng,
                                                                 @Query("appid") String appid,
                                                                 @Query("units") String unit);


}