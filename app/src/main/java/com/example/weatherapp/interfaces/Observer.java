package com.example.weatherapp.interfaces;

import com.example.weatherapp.parcelableEntities.FavouriteCity;

public interface Observer {
    public void add (FavouriteCity favouriteCity);
    public void delete (FavouriteCity favouriteCity);
}
