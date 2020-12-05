package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.weatherapp.adapters.WeekTempAdapter;
import com.example.weatherapp.current.weather.entity.WeatherRequest;
import com.example.weatherapp.fragments.CurrentWeatherFragment;
import com.example.weatherapp.helper.Keys;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;


public class WeatherDescription extends AppCompatActivity {
    private static final String TAG = WeatherDescription.class.getSimpleName();
    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/forecast?q=%s&lang=ru&units=metric&appid=%s";
    private boolean flag = false;
    private String city;
    private CurrentWeather currentWeather;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_description);
        if (getIntent().hasExtra(Keys.CITY)) {
            city = getIntent().getStringExtra(Keys.CITY);
        }

        DownloadWeatherTask task = new DownloadWeatherTask();
        task.execute(String.format(WEATHER_URL, city, BuildConfig.WEATHER_API_KEY));

        //show();

    }


        private WeekTempAdapter initRecycleView (ArrayList<WeekWeather> weatherList) {
            RecyclerView recyclerView = findViewById(R.id.recycleView_for_week_weather);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            WeekTempAdapter weekTempAdapter = new WeekTempAdapter(this,weatherList);
            DividerItemDecoration itemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
            itemDecoration.setDrawable(getDrawable(R.drawable.separator));
            recyclerView.addItemDecoration(itemDecoration);
            recyclerView.setAdapter(weekTempAdapter);
            return weekTempAdapter;
        }

    private void getData(WeatherRequest weatherRequest) {
        String name = weatherRequest.getCity().getName();
        int temp = (int) weatherRequest.getList()[0].getMain().getTemp();
        String description = weatherRequest.getList()[0].getWeather()[0].getDescription();
        String icon = weatherRequest.getList()[0].getWeather()[0].getIcon();
        currentWeather = new CurrentWeather(name, temp, description, icon);
        Log.v("проверка", currentWeather.toString());
        Bundle bundle = new Bundle();
        bundle.putParcelable(Keys.CURRENT_WEATHER, currentWeather);
        Log.v("проверка1", " кладем объект " + currentWeather.toString());
        CurrentWeatherFragment currentWeatherFragment = CurrentWeatherFragment.init(currentWeather);
        boolean isPressureTrue = getIntent().getBooleanExtra(Keys.PRESSURE, false);
        boolean isWindSpeedTrue = getIntent().getBooleanExtra(Keys.WIND_SPEED, false);

        if (isPressureTrue || isWindSpeedTrue) {
            if (isPressureTrue) {
                int pressure = weatherRequest.getList()[0].getMain().getPressure();
                bundle.putInt(Keys.PRESSURE, pressure);
                Log.v(TAG, " получено давление" + pressure);
            }
            if (isWindSpeedTrue) {
                int wind = (int) weatherRequest.getList()[0].getWind().getSpeed();
                bundle.putInt(Keys.WIND_SPEED, wind);
            }
        }
        currentWeatherFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_for_current_weather_fragment, currentWeatherFragment).commit();
    }

    private void initDataSource(WeatherRequest weatherRequest){
        ArrayList<WeekWeather>weekWeathersList = new ArrayList<>();
        String data1 = "";
        for (int i=0; i<weatherRequest.getList().length;i++) {
            WeekWeather current = new WeekWeather();
            String text = weatherRequest.getList()[i].getDt_txt();
            String data = editDay(text);
            if (data.equals(data1)) {
                Log.v(TAG, " повтор");
            } else {
                current.setDay(data);
                data1=data;
                current.setTemp(weatherRequest.getList()[i].getMain().getTemp());
                current.setIcon(weatherRequest.getList()[i].getWeather()[0].getIcon());
                weekWeathersList.add(current);
            }
        }
        initRecycleView(weekWeathersList);

    }

    private String editDay (String data) {
        //"2020-12-06 00:00:00"
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        SimpleDateFormat fmt2 = new SimpleDateFormat("E, d MMM",Locale.getDefault());
        try {
            Date date = fmt.parse(data);
            return fmt2.format(date);
        }
        catch(ParseException pe) {

            return "Date";
        }

//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss", Locale.getDefault());
//        SimpleDateFormat output = new SimpleDateFormat("d MMM", Locale.getDefault());
//        String formattedTime;
//        try {
//            Date res = df.parse(data);
//            formattedTime = output.format(res); // Это результат
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//            Log.v("ошибка", " ошибка");
//        }
//
//        return formattedTime;
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
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
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
            Gson gson = new Gson();
            WeatherRequest weatherRequest = gson.fromJson(s, WeatherRequest.class);
            getData(weatherRequest);
            initDataSource (weatherRequest);
        }
    }
}
    /*
    private void show() {
        try {
            URL uri = new URL(String.format(WEATHER_URL, city, BuildConfig.WEATHER_API_KEY));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpURLConnection) uri.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setReadTimeout(10000);
                        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        String result = getLines(in);
                        Gson gson = new Gson();
                        WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getData(weatherRequest);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    }
                }
            }).start();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining("\n"));
    }
    private void getWeatherData (WeatherRequest weatherRequest){
        String name = weatherRequest.getName();
        String temp = String.valueOf(weatherRequest.getMain().getTemp());
        String description = weatherRequest.getWeather().


        CurrentWeather currentWeather=new CurrentWeather
    }
//    private void init() {
//        textViewTemperature = findViewById(R.id.textViewTemperature);
//        textViewCity = findViewById(R.id.textViewCity);
//        textViewDescription = findViewById(R.id.textViewDescription);
//        favourites_button = findViewById(R.id.favourites_button);
//        favourites_button.setOnClickListener(clickListener);
//    }

//    View.OnClickListener clickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if (!flag) {
//                Drawable drawable1 = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_star_24);
//                favourites_button.setIcon(drawable1);
//                flag = true;
//                String message = getString(R.string.snackbar_message_add, city);
//                Snackbar
//                        .make(v, message, Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            } else {
//                String message = getString(R.string.snackbar_message_delete, city);
//                Drawable drawable1 = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_star_border_24);
//                favourites_button.setIcon(drawable1);
//                flag = false;
//                Snackbar
//                        .make(v, message, Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        }
//    };

    private void initDataSource() {
        ArrayList<WeekWeather>weekWeathers = new ArrayList<>();
        initRecycleView(weekWeathers);
       // String[] data = getResources().getStringArray(R.array.week);
        //initRecycleView(data);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
       // outState.putString(CITY, textViewCity.getText().toString());
       // outState.putString(TEMPERATURE, textViewTemperature.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
       // textViewCity.setText(savedInstanceState.getString(CITY));
      // textViewTemperature.setText(savedInstanceState.getString(TEMPERATURE));
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

    //private WeekTempAdapter initRecycleView(String[] data_source) {
    private WeekTempAdapter initRecycleView(ArrayList<WeekWeather> weatherList) {
        RecyclerView recyclerView = findViewById(R.id.recycleView_for_week_weather);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
       // WeekTempAdapter weekTempAdapter = new WeekTempAdapter(data_source);
        WeekTempAdapter weekTempAdapter = new WeekTempAdapter(weatherList);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getDrawable(R.drawable.separator));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(weekTempAdapter);
        return weekTempAdapter;
    }

    private void show(){
        try {
            final URL uri = new URL("http://api.openweathermap.org/data/2.5/forecast?q=Moscow&units=metric&lang=ru&appid=192b737722d8aace39cdae0123e27a47");
            final Handler handler = new Handler(); // Запоминаем основной поток
            new Thread(new Runnable() {
                public void run() {
                    HttpsURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpsURLConnection) uri.openConnection();
                        urlConnection.setRequestMethod("GET"); // установка метода получения данных -GET
                        urlConnection.setReadTimeout(10000); // установка таймаута - 10 000 миллисекунд
                        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // читаем  данные в поток
                        String result = getLines(in);
                        // преобразование данных запроса в модель
                        Gson gson = new Gson();
                        final WeatherRequest1 weatherRequest1 = gson.fromJson(result, WeatherRequest1.class);
                        // Возвращаемся к основному потоку
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                displayWeather(weatherRequest1);
                            }
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "Fail connection", e);
                        e.printStackTrace();
                    } finally {
                        if (null != urlConnection) {
                            urlConnection.disconnect();
                        }
                    }
                }
            }).start();
        } catch (MalformedURLException e) {
            Log.e(TAG, "Fail URI", e);
            e.printStackTrace();
        }
    }

    private String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining("\n"));
    }

    private void displayWeather(WeatherRequest1 weatherRequest1){
       // textViewCity.setText(weatherRequest.getCity().getName());
       // textViewTemperature.setText(String.format("%f2", weatherRequest.getList());
       // pressure.setText(String.format("%d", weatherRequest.getMain().getPressure()));
      //  humidity.setText(String.format("%d", weatherRequest.getMain().getHumidity()));
        //windSpeed.setText(String.format("%d", weatherRequest.getWind().getSpeed()));
    }


    private void displayWeather1(String temp, String city, String description, String pressure, String windSpeed,String moisture){
       // textViewTemperature.setText(String.format("%s °", temp));
        //textViewCity.setText(city);
        //textViewDescription.setText(description);
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

}

    */