package com.my1application.androidweatherappv2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.my1application.androidweatherappv2.Common.Common;
import com.my1application.androidweatherappv2.Model.WeatherForecastResult;
import com.my1application.androidweatherappv2.R;
import com.squareup.picasso.Picasso;



public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.MyViewHolder>{
    Context context;
    WeatherForecastResult weatherForecastResult;




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_forecast,parent,false);

        return new MyViewHolder((itemView));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load("https://openweathermap.org/img/wn/" +
                weatherForecastResult.list.get(position).weather.get(0).getIcon() +
                "png").into(holder.img_weather);
        
        
        holder.txt_date_time.setText(new StringBuilder(Common.convertUnixToDate(weatherForecastResult
        .list.get(position).dt)));
        
        holder.txt_descrption.setText(new StringBuilder(weatherForecastResult.list.get(position)
        .weather.get(0).getDescription()));
        
        holder.txt_temperature.setText(new StringBuilder(String.valueOf(weatherForecastResult.list.get(position)
        .main.getTemp())).append("°C"));
        
        

    }

    @Override
    public int getItemCount() {
        return null!=weatherForecastResult?weatherForecastResult.list.size():0;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_date_time, txt_descrption, txt_temperature;
        ImageView img_weather;
        public MyViewHolder( View itemView) {
            super(itemView);
            
            img_weather = itemView.findViewById(R.id.img_weather);
            txt_date_time = itemView.findViewById(R.id.txt_date);
            txt_descrption = itemView.findViewById(R.id.txt_description);
            txt_temperature =itemView.findViewById(R.id.txt_temperature);
        }
    }
}