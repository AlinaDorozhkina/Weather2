package com.example.weatherapp;

import android.os.Parcel;
import android.os.Parcelable;

public class City implements Parcelable {
    //еще не реализован, в проекте
    private String name;
    private int temperature;

    public City(String name, int temperature) {
        this.name = name;
        this.temperature = temperature;

    }

    protected City(Parcel in) {
        name = in.readString();
        temperature = in.readInt();
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(temperature);
    }
}
