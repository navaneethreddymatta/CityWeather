package com.example.navanee.cityweather;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        Date timeStamp;
        float temperature;
        int humidity, pressure, windSpeed;
        String condition, windDir, windAngle;
        Weather weatherObj;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statusCode = HttpURLConnection.HTTP_OK;
            if(con.getResponseCode() == statusCode) {
                BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = "";
                StringBuilder sb = new StringBuilder();
                while ((line = bf.readLine()) != null) {
                    sb.append(line + "\n");
                }
                root = new JSONObject(sb.toString());
                isValidEntry = true;
                JSONArray hourlyForecast = root.getJSONObject("root").getJSONObject("array").getJSONArray("list");
                for (int i = 0; i < hourlyForecast.length(); i++) {
                    JSONObject jsonObj = (JSONObject) hourlyForecast.get(i);
                    timeStamp = dateFormat.parse(jsonObj.getString("dt_txt"));
                    JSONObject mainObj = (JSONObject) jsonObj.getJSONObject("main");
                    temperature = Float.parseFloat(mainObj.getString("temp"));
                    pressure = Integer.parseInt(mainObj.getString("pressure"));
                    humidity = Integer.parseInt(mainObj.getString("humidity"));
                    windSpeed = Integer.parseInt((jsonObj.getJSONObject("wind")).getString("speed"));
                    windAngle = jsonObj.getJSONObject("wind").getString("deg");
                    windDir = "";
                    condition = ((JSONObject)((JSONArray) jsonObj.getJSONArray("weather")).get(0)).getString("description");
                    weatherObj = new Weather(city, country, timeStamp, temperature, humidity, pressure, condition, windSpeed, windDir, windAngle);
                    weatherList.add(weatherObj);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            isValidEntry = false;
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return weatherList;
    }

    @Override
    protected void onPostExecute(ArrayList<Weather> weathers) {
        super.onPostExecute(weathers);
        if(weatherList.size() > 0) {
            float avgTemp = 0, temp = 0;
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
                    Weather summaryWeatherObj = new Weather(city,country,tempDt,avgTemp,0,0,"",0,"","");
                    summaryList.add(summaryWeatherObj);
                    avgTemp = 0;
                    tempDt = curDate;
                    tempWeather = curWeather;
                    temp = curWeather.getTemperature();
                    j = 1;
                }
            }
            avgTemp = temp / j;
            Weather summaryWeatherObj = new Weather(city,country,tempDt,avgTemp,0,0,"",0,"","");
            summaryList.add(summaryWeatherObj);
        }
        dataRetriever.setData(weathers,summaryList);
    }

    interface DataRetriever {
        void setData(ArrayList<Weather> weatherList, ArrayList<Weather> summaryList);
    }
}
