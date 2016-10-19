package com.example.navanee.cityweather;

import java.util.Date;

/**
 * Created by navanee on 18-10-2016.
 */

public class FavouriteCity {
    private String city;
    private String country;
    private int temperature;
    private Date date;

    public FavouriteCity(Date date, String city, String country, int temperature) {
        this.date = date;
        this.city = city;
        this.country = country;
        this.temperature = temperature;
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

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
