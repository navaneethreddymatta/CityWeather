package com.example.navanee.cityweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CityDetail extends AppCompatActivity implements GetData.DataRetriever, ForecastAdapter.ConnectAdapter {

    String city, country, api_url = "http://api.openweathermap.org/data/2.5/forecast?q=location&mode=xml&appid=6f967d9101a9d96d594b80e083f5fa42";
    private RecyclerView mRecyclerView, dRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager,dLayoutManager;
    private RecyclerView.Adapter mAdapter,dAdapter;
    ArrayList<Weather> weatherDetailList = new ArrayList<Weather>();
    ArrayList<Weather> weatherSummaryList = new ArrayList<Weather>();
    DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
    DatabaseDataManager dm;
    DecimalFormat df = new DecimalFormat("#.##");
    List<FavouriteCity> fCities = new ArrayList<FavouriteCity>();

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
        String temp_city = city.replace("_"," ");
        String temp_url = api_url.replace("location", temp_city + "," + country);
        new GetData(this,city,country).execute(temp_url);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.addCity) {
            int id = item.getItemId();
            dm = new DatabaseDataManager(CityDetail.this);
            FavouriteCity fCity = new FavouriteCity();
            fCities = dm.getAllCities();
            boolean isAlreadyExists = false;
            int index=0;
            for (int i = 0; i < fCities.size(); i++) {
                if (fCities.get(i).getCityName().equals(city)){
                    isAlreadyExists = true;
                    index = i;
                    break;
                }
            }
            if (isAlreadyExists) {
                fCity.set_id(fCities.get(index).get_id());
                fCity.setCityName(city);
                fCity.setCountry(country);
                fCity.setTemperature(df.format(weatherDetailList.get(0).getTemperature()));
                fCity.setDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
                fCity.setFavorite(0);
                dm.updateCity(fCity);
                Toast.makeText(this,"City Updated",Toast.LENGTH_SHORT).show();

            } else{
                fCity.setCityName(city);
                fCity.setDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
                fCity.setCountry(country);
                fCity.setTemperature(df.format(weatherDetailList.get(0).getTemperature()));
                dm.saveCity(fCity);
                Toast.makeText(this,"City Saved",Toast.LENGTH_SHORT).show();
            }
        } else if(item.getItemId() == R.id.settings){
            Intent intent = new Intent(this,UserPreferenceActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void setData(ArrayList<Weather> weatherList, ArrayList<Weather> summaryList) {
        weatherDetailList = weatherList;
        weatherSummaryList = summaryList;
        if(weatherDetailList.size() == 0) {
            Toast.makeText(this,R.string.error_message,Toast.LENGTH_LONG).show();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(CityDetail.this,MainActivity.class);
                    startActivity(intent);
                }
            },5 * 1000);
        } else {
            findViewById(R.id.loadingLayout).setVisibility(View.GONE);
            findViewById(R.id.detailLayout).setVisibility(View.VISIBLE);
            mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);
            mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new ForecastAdapter(this, summaryList, this);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void setSelectedCell(int index) {
        dRecyclerView = (RecyclerView) findViewById(R.id.detailRecycleView);
        dLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        dRecyclerView.setLayoutManager(dLayoutManager);
        Weather selWeather = weatherSummaryList.get(index);
        Date curDate = selWeather.getTimeStamp();
        ArrayList<Weather> detailList = new ArrayList<Weather>();
        String tDate = dateFormat.format(selWeather.getTemperature());
        for(int i = 0; i < weatherDetailList.size(); i++) {
            Weather thisItem = weatherDetailList.get(i);
            Date tempDt = thisItem.getTimeStamp();
            if (tempDt != null && curDate != null && (tempDt.getDate() == curDate.getDate() && tempDt.getMonth() == curDate.getMonth() && tempDt.getYear() == curDate.getYear())) {
                detailList.add(thisItem);
                tDate = dateFormat.format(thisItem.getTimeStamp());
                Log.d("demo",tDate);
            }
        }
        ((TextView)findViewById(R.id.det_sub_title)).setText("Three Hourly Forecast on " + tDate);
        dAdapter = new DetailAdapter(this,detailList);
        dRecyclerView.setAdapter(dAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2,menu);
        return true;
    }

    private class PreferencesChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener{
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Toast.makeText(CityDetail.this, "Preferences changed", Toast.LENGTH_LONG);
        }
    }
}
