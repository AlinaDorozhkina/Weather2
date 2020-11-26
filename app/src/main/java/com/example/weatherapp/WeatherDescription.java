package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import static com.example.weatherapp.R.drawable.ic_baseline_star_border_24;

public class WeatherDescription extends AppCompatActivity {
    private static final String TAG = WeatherDescription.class.getSimpleName();
    private final String CITY="city";
    private final String TEMPERATURE = "temp";
    private TextView textViewTemperature;
    private TextView textViewCity;
    private MaterialButton favourites_button;
    private boolean flag = false;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);
        // хотела установить автоматическую смену темы дня и ночи (по  сути интересна смена бекграунда),
        // но MODE_NIGHT_AUTO деприкейтид, и не знаю как реализовать такую схему
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_description);
        textViewTemperature = findViewById(R.id.textViewTemperature);
        textViewCity = findViewById(R.id.textViewCity);
        favourites_button = findViewById(R.id.favourites_button);

        favourites_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resources res = getResources();
                if (!flag){
                    Drawable drawable1 = res.getDrawable(R.drawable.ic_baseline_star_24);
                    // метод getDrawable деприкейтид, а на что его надо заменить в данном случае?
                    // на эмуляторе моя звезда белая при нажатии, хотя должна быть желтой
                    favourites_button.setIcon(drawable1);
                    flag=true;
                    String message = getString(R.string.snackbar_message_add, city);
                    Snackbar
                            .make(v,message, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else{
                    String message = getString(R.string.snackbar_message_delete, city);
                    Drawable drawable1 = res.getDrawable(ic_baseline_star_border_24);
                    favourites_button.setIcon(drawable1);
                    flag=false;
                    Snackbar
                            .make(v, message, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        city = getIntent().getStringExtra(Keys.CITY);
        if (city != null) {
            Log.d(TAG, " получен бандл " + city);
            textViewCity.setText(city);
            textViewTemperature.setText(String.format("%s °", showRandomValue()));
        }
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
                bundle.putBoolean(Keys.PRESSURE, true);
            }
            if (isWindSpeedTrue) {
                bundle.putBoolean(Keys.WIND_SPEED, true);
            }
            if (isMoistureTrue) {
                bundle.putBoolean(Keys.MOISTURE, true);
            }
            fragment.setArguments(bundle);
            transaction.commit();
        }
        initDataSource();
    }

    private String showRandomValue() {
        return String.valueOf((int) (Math.random() * 30));
    }

    private void initDataSource(){
        String [] data = getResources().getStringArray(R.array.week);
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

    private WeekTempAdapter initRecycleView(String [] data_source){
        RecyclerView recyclerView=findViewById(R.id.recycleView_for_week_weather);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        WeekTempAdapter weekTempAdapter =new WeekTempAdapter(data_source);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,  LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getDrawable(R.drawable.separator));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(weekTempAdapter);
        return weekTempAdapter;
    }
}