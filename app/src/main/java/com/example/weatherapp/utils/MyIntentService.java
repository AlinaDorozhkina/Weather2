package com.example.weatherapp.utils;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.example.weatherapp.WeatherDescription;
import com.example.weatherapp.parcelableEntities.WeekWeather;
import com.example.weatherapp.current.weather.entity.WeatherRequest;
import com.example.weatherapp.helper.Keys;
import com.example.weatherapp.parcelableEntities.CurrentWeather;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        HttpURLConnection urlConnection = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(intent.getStringExtra(Keys.URL));
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
            Log.v("MyintentService",sb.toString() );
            initJsonModel(sb.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private void initJsonModel(String result) {
        if (result == null) {
            Log.v("MyintentService"," "  + result );
            Intent broadcastIntent = new Intent(WeatherDescription.BROADCAST_ACTION_FINISHED);
            broadcastIntent.putExtra(Keys.NULL, result);
            sendBroadcast(broadcastIntent);
        } else {
            Log.v("MyintentService","зашли в элс" );
            Gson gson = new Gson();
            WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
            CurrentWeather currentWeather = JSONUtils.initCurrentWeather(weatherRequest);
            ArrayList<WeekWeather> weekWeathersList = JSONUtils.initWeekWeatherList(weatherRequest);
            sendBrodcast(currentWeather, weekWeathersList);
        }
    }

    private void sendBrodcast(CurrentWeather currentWeather,ArrayList<WeekWeather> weekWeathersList) {
        Intent broadcastIntent = new Intent(WeatherDescription.BROADCAST_ACTION_FINISHED);
        broadcastIntent.putExtra(Keys.CURRENT_WEATHER, currentWeather);
        broadcastIntent.putParcelableArrayListExtra(Keys.JSON_RESULT, weekWeathersList);
        sendBroadcast(broadcastIntent);
    }
}