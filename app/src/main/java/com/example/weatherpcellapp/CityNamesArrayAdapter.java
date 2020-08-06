package com.example.weatherpcellapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CityNamesArrayAdapter extends ArrayAdapter {
    public CityNamesArrayAdapter(Context context, List<CityClass> resource) {
        super(context,0, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView =convertView;
        //Now we inflate the view i.e we will load only that much data which can be
        //shown on screen it's also called recycler View
        if(listItemView==null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.cities_list_adapter, parent, false);
        }
        //we will get the data of current item using it's position
        CityClass currCity = (CityClass) getItem(position);

        //and populate current Item to listView of activity_main using the structure
        // defined on cities_list_adapter
        TextView textView = listItemView.findViewById(R.id.CityListAdapter);

        //We will also observe the change in LogCat open Log Cat go in debug sections and enter
        //CityNamesAdapter now what you can observe is not all the city names is printed in logcat
        //a small part of it is only there to verify, In logcat type ALLCities and count the number of Cities
        //and again go in CityNamesAdapter and count city there this is because we are not
        // loading the data which cannot be seen on
        //screen and now if you scroll on screen what can you observe is that there is new set of data
        // which can be observed which different from previous data hence we say previous data is recycled
        //I hope this is quite self explainatory
        Log.d("CityNamesAdapter" ,currCity.cities+" "+currCity.woeid);

        //Now we will set the current city name on textView inorder to display on screen
        textView.setText(currCity.cities);
        return listItemView;
    }
}
