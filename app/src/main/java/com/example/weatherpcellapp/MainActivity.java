package com.example.weatherpcellapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    List<CityClass> cityNames = new ArrayList<CityClass>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Linking ListView in activity_main.xml to MainActivity.java
        listView = findViewById(R.id.CitiesList);

        //Calling loadData function which will take care of Requesting as well
        // as Loading Data in out activity_main.xml
        loadData();
    }

    void loadData() {
        //Don't Forget to add OkHttp dependency in build.gradle(Module: app) and
        // add Internet Permission in AndroidManiFest
        //Import the following libraries
        // import com.squareup.okhttp.Callback;
        //import com.squareup.okhttp.OkHttpClient;
        //import com.squareup.okhttp.Request;
        //import com.squareup.okhttp.Response;

        //----------------------------For India------------------------------------

        //create a request object by specifying url the following get Request will give
        //us woeid number and name of cities in India you can copy this url and paste in google
        //chrome and see the response
         Request request = new Request.Builder()
                .url("https://www.metaweather.com/api/location/search/?lattlong=20.5937,78.9629")
                .get().build();
        //Create OkHttpClient Object
        OkHttpClient client = new OkHttpClient();

        // Call the request using client we just created
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //Use this code to Handle Failed Request mostly due to internet issue
                // we will just Create a Tost Message for user
                Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                //Here we will check is reponse is Sucessfull or is their any
                // request error i.e url error
                if (!response.isSuccessful()) {
                    //Here our reponse is UnSucessfull so we inform the user
                    // about the same as before
                    Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                //If Response is sucessfull we move forward and convert the
                // reponse which is in JSON format to String
                String respFromApi = response.body().string();

                //We will Log this LogCat in order to view the Raw Format recieved
                //you can open the log cat go in debug section and type RawData you
                // will be able to observe data there
                Log.d("RawData", respFromApi);

                //Now We will call Extract Data Function which will retrieve the
                // woied and name of each city from the response
                try {
                    extractData(respFromApi);
                } catch (Exception e) {
                    //Informing Data is Not in JSON Format
                    Toast.makeText(MainActivity.this, "Response Not in JSON Format", Toast.LENGTH_SHORT).show();
                }
                ;
            }
        });


        //---------------------------------FOR United States----------------------------------------

        //Following codes has similar output as before but the difference is the city Names here
        //is of United States
        request = new Request.Builder()
                .url("https://www.metaweather.com/api/location/search/?lattlong=38.899101,-77.028999")
                .get().build();
         client = new OkHttpClient();
         client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                String respFromApi = response.body().string();
                Log.d("RawData", respFromApi);
                try {
                    extractData(respFromApi);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Response Not in JSON Format", Toast.LENGTH_SHORT).show();
                }
            }
         });



    }

    void extractData(String respFromApi) throws Exception {
        //Now we will convert String to JSONArray remember the response we got is in
        // JSON format
        //jsonArray is Basically an Array of JSON Object, open Log cat and type
        // RawData and you can observe that it starts with [ and ends with ] indicatiog it's
        //an array in that you can find multiple object which starts with { and ends with }
        // that contains multiple properties here it is distance,title,woeid etc

        JSONArray jsonArray = new JSONArray(respFromApi);
        //Now we will extract the jsonObject and get title and woeid, title is used to list names
        // and woeid used when a user clicks on particulat city we will again send a http request
        // for that city only using woeid
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            //Now we will add this Names and woeid using CityClass Objects to out list of cities which we are
            // going to populate in activity_main.xml

            //getting title refer RawData for better understanding
            String cityName = jsonObject.getString("title");

            //getting woeid
            int woeid = jsonObject.getInt("woeid");

            //We will also Log every city that we extract from respFromApi i.e jsonObject
            //So open Open LogCat go to debug section and type AllCities
            Log.d("AllCities", cityName + " " + woeid);


            //creating object of current city
            CityClass singleCity = new CityClass(cityName, woeid);

            //adding object to list of cityNames
            cityNames.add(singleCity);
        }
        //We will now create an Asynchronous Function which runs in background and don't
        //affects the Main thread
        Asynchronous asynk  = new Asynchronous();
        asynk.execute();
    }

    private class Asynchronous extends AsyncTask<String ,Void,List<CityClass>>{
        @Override
        protected List<CityClass> doInBackground(String... strings) {
            //Whenever we load or scroll on screen this function is automatically
            //called and we return the list of citynames that we loaded in extractData
            //function after this onPostExecute is called
            return cityNames;
        }

        @Override
        protected void onPostExecute(List<CityClass> cityClasses) {
                //Now we create a object cityAdapter and intialize with CityNamesArrayAdapter class
                //Check out CityNamesAdapter in order to understand more
                final CityNamesArrayAdapter cityAdapter = new CityNamesArrayAdapter(MainActivity.this , cityClasses);

                //after that set the adapter on listView which refers to listView in activity_main
                listView.setAdapter(cityAdapter);

                //Now we will setOnItemClickListener in order to get CityName on
                //which user clicks in order to get information of weather of that city
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //here i represents index position of the element in adapter
                        CityClass city = (CityClass) cityAdapter.getItem(i);

                        //We will log this to LogCat in order to observe which element is clicked
                        Log.d("SelectedCity" , city.cities +" "+city.woeid+" "+i);

                        //Now Following Code will send data from this activity to WeatherReport Activity
                        //which is reponsible to display all weather information for this
                        //city

                        //Format new Intent(current  , destination );
                        Intent intent = new Intent(MainActivity.this , WeatherReport.class);

                        //sending information of city name as well as city woeid
                        intent.putExtra("cityName" , city.cities);
                        intent.putExtra("cityWoeid" , city.woeid);

                        //starting new activity
                        startActivity(intent);

                        //you can uncomment following code if you want this activity to be released
                        //i.e if some one presses back in WeatherReport the app should be closed
                        //as of now if the user pressed back in WeatherReport he will again come to this activity
                        //and can select any other city which is logically correct that's why we don't use it here
                        //but you are can try it.

                        //finish();

                    }
                });

        }
    }
}