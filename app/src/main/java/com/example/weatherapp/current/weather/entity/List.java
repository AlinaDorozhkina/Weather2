package com.example.weatherapp.current.weather.entity;

public class List {
    private Main main;
    private Weather[] weather;
    private Wind wind;
    private String dt_txt;

    public Main getMain() {
        return main;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public Wind getWind() {
        return wind;
    }

    public String getDt_txt() {
        return dt_txt;
    }
}
