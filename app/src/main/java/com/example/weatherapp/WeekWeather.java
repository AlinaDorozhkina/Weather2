package com.example.weatherapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WeekWeather {

    private String day;
    private double temp;
    private String icon;

    public WeekWeather(){

    }

    public WeekWeather(String day, double temp, String iconUrl) {
        this.day = day;
        this.temp = temp;
        this.icon = iconUrl;
    }

    public String getDay() {
        return day;
    }

    public double getTemp() {
        return temp;
    }

    public String getIconUrl() {
        return icon;
    }

    public void setDay(String day) {

        this.day = day;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


}
