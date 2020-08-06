package com.example.weatherpcellapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WeatherReport extends AppCompatActivity {
    String cityName;
    int cityWoeid ;
    List<WeatherAttributes> mylist = new ArrayList<>();
    ListView listView;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_report);

        //Now we retrived data of City Name and City Woeid which is passed from
        //MainActivity using Following Code be sure the fields inside getStringExtra is same as
        //field in MainActivities putExtra
        Intent intent = getIntent();
        cityName = intent.getStringExtra("cityName");
        cityWoeid = intent.getIntExtra("cityWoeid" , 2347567);

        //Displaying CityName and linking listview from xml to java
        txt = findViewById(R.id.cityName);
        txt.setText(cityName);
        listView = findViewById(R.id.weatherReportList);

        LoadData();

    }
    void LoadData(){

        //Now we will request using following code to get the weather report for particular city
        Request request = new Request.Builder()
                .url("https://www.metaweather.com/api/location/" + cityWoeid + "/")
                .get()
                .build();

        Log.d("RequestUrl", "https://www.metaweather.com/api/location/" + cityWoeid + "/");
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(WeatherReport.this, "Request Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    return;
                }
                //as reponse is sucessfull get the body can convert it into string
                String respFromApi = response.body().string();

                //Now we will Log Raw to console in order to undestand the response from server
                Log.d("WeatherReportRawData", respFromApi);
                try {
                    extractData(respFromApi);
                } catch (Exception e) {
                    Toast.makeText(WeatherReport.this, "Not in Json Format", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void extractData(String respFromApi) throws Exception{
        //Here reponse is quite different compared to which we got in MainActivity
        //open logcat in debug mode and enter WeatherReportRawData what we can observe is
        //here we get an Object not an array as before and we are intrested in consolidated_weather object
        //further when we retrieved consolidated_weather we get an array of object where each object consist of
        //weather_state,min_temp,max_temp and all other attributes further the following code is used to
        //extract same and add it to mylist

        JSONObject myjsonObject = new JSONObject(respFromApi);
        JSONArray jsonArray = myjsonObject.getJSONArray("consolidated_weather");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String date = jsonObject.getString("applicable_date");
            String min_temp = jsonObject.getString("min_temp");
            if (min_temp.length() > 5) {
                min_temp = String.valueOf(min_temp.toCharArray(), 0, 5);
            }
            String max_temp = jsonObject.getString("max_temp");
            if (max_temp.length() > 5) {
                max_temp = String.copyValueOf(max_temp.toCharArray(), 0, 5);
            }
            String humidity = jsonObject.getString("humidity");
            String air_pressure = jsonObject.getString("air_pressure");

            String weatherState = jsonObject.getString("weather_state_name");
            URL url = new URL("https://www.metaweather.com/static/img/weather/png/" + jsonObject.getString("weather_state_abbr") + ".png");

            //Loging WeatherImageUrl to console
            Log.d("WeatherImageUrl", url.toString());
            mylist.add(new WeatherAttributes(date, min_temp, max_temp, humidity, air_pressure, weatherState, url));
        }
        Asynchronous async = new Asynchronous();
        async.execute();
    }
        private class Asynchronous extends AsyncTask<String, Void, List<WeatherAttributes>> {
        @Override
        protected List<WeatherAttributes> doInBackground(String... strings) {
            return mylist;
        }

        @Override
        protected void onPostExecute(List<WeatherAttributes> my_data) {
            final WeatherAdapter adapter = new WeatherAdapter(WeatherReport.this, my_data);
            listView.setAdapter(adapter);

        }
    }

}