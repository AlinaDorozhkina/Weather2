package com.example.weatherapp.utils;

import com.example.weatherapp.current.weather.entity.WeatherRequest;
import com.example.weatherapp.parcelableEntities.CurrentWeather;
import com.example.weatherapp.parcelableEntities.WeekWeather;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class JSONUtils {

    public static CurrentWeather initCurrentWeather(WeatherRequest weatherRequest){
        String name = weatherRequest.getCity().getName();
        int temp = (int) weatherRequest.getList()[0].getMain().getTemp();
        String description = weatherRequest.getList()[0].getWeather()[0].getDescription();
        String icon = weatherRequest.getList()[0].getWeather()[0].getIcon();
        int pressure = weatherRequest.getList()[0].getMain().getPressure();
        int wind = (int) weatherRequest.getList()[0].getWind().getSpeed();
        return new CurrentWeather(name, temp, description, icon, wind, pressure);
    }
    public static ArrayList<WeekWeather> initWeekWeatherList (WeatherRequest weatherRequest){
        String data1 = "";
        ArrayList<WeekWeather> weekWeathersList = new ArrayList<>();
        for (int i = 0; i < weatherRequest.getList().length; i++) {
            WeekWeather current = new WeekWeather();
            String text = weatherRequest.getList()[i].getDt_txt();
            String data = editDay(text);
            if (data.equals(data1)) {
            } else {
                current.setDay(data);
                data1 = data;
                current.setTemp(weatherRequest.getList()[i].getMain().getTemp());
                current.setIcon(weatherRequest.getList()[i].getWeather()[0].getIcon());
                weekWeathersList.add(current);
            }
        }
        return weekWeathersList;
    }

    private static String editDay(String data) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat fmt2 = new SimpleDateFormat("E, d MMM", Locale.getDefault());
        try {
            Date date = fmt.parse(data);
            return fmt2.format(date);
        } catch (ParseException pe) {
            return "Date";
        }
    }
}
