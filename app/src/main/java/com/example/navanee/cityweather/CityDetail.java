package com.example.navanee.cityweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class CityDetail extends AppCompatActivity implements GetData.DataRetriever {

    String city, country, api_url = "http://api.openweathermap.org/data/2.5/forecast?q=location&mode=xml&appid=6f967d9101a9d96d594b80e083f5fa42";
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);
        getSupportActionBar().setTitle(R.string.app_name_city_detail);
        fetchData();
    }

    public void fetchData() {
        city = getIntent().getExtras().get("city").toString();
        country = getIntent().getExtras().get("country").toString();
        ((TextView)findViewById(R.id.det_title)).setText("Daily Forecast for " + city + "," + country);
        String temp_url = api_url.replace("location", city + "," + country);
        new GetData(this,city,country).execute(temp_url);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.addCity) {
         /*if(weatherArrList.size() > 0) {
            int index = isFavCityAlreadyExists(city, state);
            if(index == -1) {
               favList.add(new FavouriteCity(weatherArrList.get(0).getTimeStamp(), city, state, weatherArrList.get(0).getTemperature()));
               Toast.makeText(this, R.string.fav_added, Toast.LENGTH_LONG).show();
            } else {
               FavouriteCity fav = favList.get(index);
               fav.setTemperature(weatherArrList.get(0).getTemperature());
               fav.setDate(weatherArrList.get(0).getTimeStamp());
               Toast.makeText(this, R.string.fav_updated, Toast.LENGTH_LONG).show();
            }
            favCitiesStr = gson.toJson(favList,MainActivity.type);
            editor.clear();
            editor.putString(MainActivity.PREF_KEY_NAME,favCitiesStr);
            editor.commit();
         }*/
        } else if(item.getItemId() == R.id.settings){
            Intent intent = new Intent(this,PreferenceActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void setData(ArrayList<Weather> weatherList, ArrayList<Weather> summaryList) {
        Log.d("demo", String.valueOf(weatherList.size()));
        Log.d("demo", String.valueOf(summaryList.size()));
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ForecastAdapter(this,summaryList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
