package com.example.weatherpcellapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class WeatherAdapter extends ArrayAdapter {
    public WeatherAdapter(@NonNull Context context, List<WeatherAttributes> resource) {
        super(context,0, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView==null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.weather_report_adapter, parent, false);
        }
        //Similar to CityNamesArrayAdapter if you have confusion refer CityNamesArrayAdapter
        final WeatherAttributes current = (WeatherAttributes) getItem(position);

        //Populating current Item to listView of weather_report using the structure
        // defined on weather_report_adapter
        TextView weatherState = listItemView.findViewById(R.id.weatherState);
        TextView humidity = listItemView.findViewById(R.id.humidity);
        TextView MaxTemp = listItemView.findViewById(R.id.maxTemp);
        TextView MinTemp = listItemView.findViewById(R.id.minTemp);
        TextView AirPressure = listItemView.findViewById(R.id.airPressure);
        TextView date = listItemView.findViewById(R.id.date);
        ImageView img = listItemView.findViewById(R.id.weatherImage);

        weatherState.setText("Weather State:"+current.weather_state);
        humidity.setText("Humidity"+ current.humidity);
        MaxTemp.setText("Max Temp: "+current.max_temp);
        MinTemp.setText("Min Temp: "+current.min_temp);
        AirPressure.setText("Air Pressure:"+current.air_pressure);
        date.setText("Date:"+current.date);

        // here we use picasso in order to load image using url
        Picasso.get().load(String.valueOf(current.image)).into(img);

        //We will also Log this to console as we did before open log in debug mode and enter WeatherAdapter
        String str = current.weather_state + " "+current.humidity+" "+current.max_temp +" "+
                current.min_temp+" "+current.air_pressure +" "+current.date+" "+current.image;
        Log.d("WeatherAdapter" , str);

        return listItemView;
    }
}
