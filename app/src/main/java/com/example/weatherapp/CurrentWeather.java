package com.example.weatherapp;

import android.os.Parcel;
import android.os.Parcelable;

public class CurrentWeather implements Parcelable {
    String cityName;
    double temperature;
    String description;
    String icon;


    public CurrentWeather(String cityName, double temperature, String description, String icon) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.description = description;
        this.icon = icon;
    }

    protected CurrentWeather(Parcel in) {
        cityName=in.readString();
        temperature=in.readDouble();
        description=in.readString();
        icon=in.readString();
    }

    public static final Creator<CurrentWeather> CREATOR = new Creator<CurrentWeather>() {
        @Override
        public CurrentWeather createFromParcel(Parcel in) {
            return new CurrentWeather(in);
        }

        @Override
        public CurrentWeather[] newArray(int size) {
            return new CurrentWeather[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cityName);
        dest.writeDouble(temperature);
        dest.writeString(description);
        dest.writeString(icon);
    }

    public String getCityName() {
        return cityName;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return "CurrentWeather{" +
                "cityName='" + cityName + '\'' +
                ", temperature='" + temperature + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
