package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class WeatherDescription extends AppCompatActivity {
    private static final String TAG = WeatherDescription.class.getSimpleName();
    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&APPID=%s&lang=ru&units=metric";
    private final String CITY = "city";
    private final String TEMPERATURE = "temp";
    private TextView textViewTemperature;
    private TextView textViewCity;
    private TextView textViewDescription;
    private MaterialButton favourites_button;
    private boolean flag = false;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_description);
        init();

        city = getIntent().getStringExtra(Keys.CITY);
        if (city != null) {
            Log.d(TAG, " получен бандл " + city);
            DownloadWeatherTask task = new DownloadWeatherTask();
            String url = String.format(WEATHER_URL, city, BuildConfig.WEATHER_API_KEY);
            task.execute(url);
        }
        initDataSource();
    }

    private void init() {
        textViewTemperature = findViewById(R.id.textViewTemperature);
        textViewCity = findViewById(R.id.textViewCity);
        textViewDescription = findViewById(R.id.textViewDescription);
        favourites_button = findViewById(R.id.favourites_button);
        favourites_button.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!flag) {
                Drawable drawable1 = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_star_24);
                favourites_button.setIcon(drawable1);
                flag = true;
                String message = getString(R.string.snackbar_message_add, city);
                Snackbar
                        .make(v, message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                String message = getString(R.string.snackbar_message_delete, city);
                Drawable drawable1 = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_star_border_24);
                favourites_button.setIcon(drawable1);
                flag = false;
                Snackbar
                        .make(v, message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    };

    private void initDataSource() {
        String[] data = getResources().getStringArray(R.array.week);
        initRecycleView(data);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CITY, textViewCity.getText().toString());
        outState.putString(TEMPERATURE, textViewTemperature.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        textViewCity.setText(savedInstanceState.getString(CITY));
        textViewTemperature.setText(savedInstanceState.getString(TEMPERATURE));
    }

    @Override
    public void onBackPressed() {
        if (flag) {
            Intent intentResult = new Intent(WeatherDescription.this, MainActivity.class);
            intentResult.putExtra(Keys.FAVOURITES, city);
            setResult(RESULT_OK, intentResult);
            Log.d(TAG, "передано " + city);
            finish();
        }
        super.onBackPressed();
    }

    private WeekTempAdapter initRecycleView(String[] data_source) {
        RecyclerView recyclerView = findViewById(R.id.recycleView_for_week_weather);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        WeekTempAdapter weekTempAdapter = new WeekTempAdapter(data_source);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getDrawable(R.drawable.separator));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(weekTempAdapter);
        return weekTempAdapter;
    }

    private void displayWeather(String temp, String city, String description, String pressure, String windSpeed,String moisture){
        textViewTemperature.setText(String.format("%s °", temp));
        textViewCity.setText(city);
        textViewDescription.setText(description);
        boolean isPressureTrue = getIntent().getBooleanExtra(Keys.PRESSURE, false);
        boolean isWindSpeedTrue = getIntent().getBooleanExtra(Keys.WIND_SPEED, false);
        boolean isMoistureTrue = getIntent().getBooleanExtra(Keys.MOISTURE, false);

        if (isPressureTrue || isWindSpeedTrue || isMoistureTrue) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            ExtraDataFragment fragment = new ExtraDataFragment();
            transaction.add(R.id.frame_for_extra_layout, fragment);
            Bundle bundle = new Bundle();
            if (isPressureTrue) {
                bundle.putString(Keys.PRESSURE, pressure);
            }
            if (isWindSpeedTrue) {
                bundle.putString(Keys.WIND_SPEED, windSpeed);

            }
            if (isMoistureTrue) {
                bundle.putString(Keys.MOISTURE, moisture);
            }
            fragment.setArguments(bundle);
            transaction.commit();
        }
    }

    private class DownloadWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            StringBuilder result = new StringBuilder();
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    result.append(line);
                    line = reader.readLine();
                }
                Log.d(TAG, result.toString());
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String city = jsonObject.getString("name");
                String temp = jsonObject.getJSONObject("main").getString("temp");
                String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                String windSpeed = jsonObject.getJSONObject("wind").getString("speed");
                String pressure = jsonObject.getJSONObject("main").getString("pressure");
                String moisture = jsonObject.getJSONObject("main").getString("humidity");
                displayWeather(temp, city, description,pressure,windSpeed,moisture);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}