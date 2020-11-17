package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class WeatherDescription extends AppCompatActivity {
    private static final String TAG = WeatherDescription.class.getSimpleName();
    private TextView textViewTemperature;
    private TextView textViewCity;
    private ImageButton favourites_button;
    private boolean flag = false;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_description);
        textViewTemperature=findViewById(R.id.textViewTemperature);
        textViewCity = findViewById(R.id.textViewCity);
        favourites_button = findViewById(R.id.favourites_button);

        city = getIntent().getStringExtra(Keys.CITY);
        if (city!=null){
            Log.d(TAG, " получен бандл "+ city);
            textViewCity.setText(city);
            textViewTemperature.setText(String.format("%s °", showRandomValue()));
        }
    }

    private  String showRandomValue() {
        int random = (int) (Math.random() * 30);
        return String.valueOf(random);
    }

    public void addToFavourites(View view) {
        if (!flag) {
            favourites_button.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.btn_star_big_on));
            flag = true;
        } else {
            favourites_button.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.btn_star_big_off));
            flag = false;
        }
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
}