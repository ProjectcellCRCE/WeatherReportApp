package com.example.weatherpcellapp;

import java.net.URL;

public class WeatherAttributes {
    String weather_state ,date;
    URL image ;
    String min_temp , max_temp , humidity , air_pressure;
    public WeatherAttributes(String date , String min_temp , String max_temp , String humidity , String air_pressure ,String weather_state,URL image)
    {
        this.date = date;
        this.min_temp = min_temp ;
        this.max_temp = max_temp ;
        this.humidity = humidity  ;
        this.air_pressure = air_pressure;
        this.image =image ;
        this.weather_state = weather_state ;
    }

}
