package com.example.weatherapp.interfaces;


public interface Subject {
    public void register (Observer observer);
    public void unregister (Observer observer);
    public void notifyObservers();
}
