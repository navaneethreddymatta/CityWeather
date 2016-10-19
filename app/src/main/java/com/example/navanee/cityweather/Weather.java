package com.example.navanee.cityweather;

import java.util.Date;

/**
 * Created by navanee on 18-10-2016.
 */

public class Weather {
    private String city;
    private String country;
    private Date timeStamp;
    private float temperature;
    private int humidity;
    private int pressure;
    private String condition;
    private int windSpeed;
    private String windDir;
    private String windAngle;

    public Weather(String city, String country, Date timeStamp, float temperature, int humidity, int pressure, String condition, int windSpeed, String windDir, String windAngle) {
        this.city = city;
        this.country = country;
        this.timeStamp = timeStamp;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.condition = condition;
        this.windSpeed = windSpeed;
        this.windDir = windDir;
        this.windAngle = windAngle;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindAngle() {
        return windAngle;
    }

    public void setWindAngle(String windAngle) {
        this.windAngle = windAngle;
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }
}
