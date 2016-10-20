package com.example.navanee.cityweather;

import android.os.AsyncTask;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by navanee on 18-10-2016.
 */

public class GetData extends AsyncTask<String, Void, ArrayList<Weather>> {
    DataRetriever dataRetriever;
    String city, country, errorDescription = "";
    ArrayList<Weather> weatherList = new ArrayList<Weather>();
    ArrayList<Weather> summaryList = new ArrayList<Weather>();
    JSONObject root;
    boolean isValidEntry = true;

    public GetData(DataRetriever dataRetriever, String city, String country) {
        this.dataRetriever = dataRetriever;
        this.city = city;
        this.country = country;
    }

    @Override
    protected ArrayList<Weather> doInBackground(String... params) {

        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statusCode = HttpURLConnection.HTTP_OK;
            if(con.getResponseCode() == statusCode) {
                InputStream inputStream= con.getInputStream();
                return WeatherParser.ParseWeather(inputStream,city,country);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return weatherList;
    }

    @Override
    protected void onPostExecute(ArrayList<Weather> weathers) {
        super.onPostExecute(weathers);
        if(weathers.size() > 0) {
            Double avgTemp = 0.0, temp = 0.0;
            Date tempDt = null;
            int j = 0;
            Weather tempWeather;
            for (int i = 0; i < weathers.size(); i++) {
                Weather curWeather = weathers.get(i);
                Date curDate = curWeather.getTimeStamp();
                if(tempDt == null || (tempDt.getDate() == curDate.getDate() && tempDt.getMonth() == curDate.getMonth() && tempDt.getYear() == curDate.getYear())) {
                    tempDt = curDate;
                    tempWeather = curWeather;
                    temp = temp + curWeather.getTemperature();
                    j++;
                } else {
                    avgTemp = temp / j;
                    Weather summaryWeatherObj = new Weather();
                    summaryWeatherObj.setCity(city);
                    summaryWeatherObj.setCountry(country);
                    summaryWeatherObj.setTimeStamp(tempDt);
                    summaryWeatherObj.setTemperature(avgTemp);
                    summaryList.add(summaryWeatherObj);
                    avgTemp = 0.0;
                    tempDt = curDate;
                    tempWeather = curWeather;
                    temp = curWeather.getTemperature();
                    j = 1;
                }
            }
            avgTemp = temp / j;
            Weather summaryWeatherObj = new Weather();
            summaryWeatherObj.setCity(city);
            summaryWeatherObj.setCountry(country);
            summaryWeatherObj.setTimeStamp(tempDt);
            summaryWeatherObj.setTemperature(avgTemp);
            summaryList.add(summaryWeatherObj);
        }
        dataRetriever.setData(weathers,summaryList);
    }

    interface DataRetriever {
        void setData(ArrayList<Weather> weatherList, ArrayList<Weather> summaryList);
    }
}
