package com.example.weatherapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import com.example.weatherapp.adapters.WeekTempAdapter;
import com.example.weatherapp.fragments.CurrentWeatherFragment;
import com.example.weatherapp.helper.Keys;
import com.example.weatherapp.parcelableEntities.CurrentWeather;
import com.example.weatherapp.parcelableEntities.FavouriteCity;
import com.example.weatherapp.parcelableEntities.WeekWeather;
import com.example.weatherapp.utils.MyIntentService;
import com.example.weatherapp.utils.NetworkUtils;
import java.net.URL;
import java.util.ArrayList;


public class WeatherDescription extends AppCompatActivity implements CurrentWeatherFragment.OnCurrentWeatherFragmentDataListener {

    public static final String BROADCAST_ACTION_FINISHED = "service get result";
    private static final String TAG = WeatherDescription.class.getSimpleName();
    private String city;
    private FavouriteCity favouriteCity;
    private CurrentWeather currentWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_description);
        if (getIntent().hasExtra(Keys.CITY)) {
            city = getIntent().getStringExtra(Keys.CITY);
            Log.v(TAG, " получен интент " + city);
        }
        URL url = NetworkUtils.buildURL(city);
            Intent intentMyIntentService = new Intent(this, MyIntentService.class);
            startService(intentMyIntentService.putExtra(Keys.URL, url.toString()));
//        DownloadWeatherTask task = new DownloadWeatherTask();
//        task.execute(String.format(WEATHER_URL, city, BuildConfig.WEATHER_API_KEY));
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(JsonResultReceiver, new IntentFilter(BROADCAST_ACTION_FINISHED));
    }

    private BroadcastReceiver JsonResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(Keys.NULL)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WeatherDescription.this);
                builder.setTitle(R.string.error)
                        .setMessage(R.string.city_error)
                        .setIcon(ContextCompat.getDrawable(WeatherDescription.this, R.drawable.ic_baseline_error_24))
                        .setCancelable(false)
                        .setPositiveButton(R.string.button_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();

            }
            if (intent.hasExtra(Keys.CURRENT_WEATHER)) {
                currentWeather = intent.getParcelableExtra(Keys.CURRENT_WEATHER);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Keys.CURRENT_WEATHER, currentWeather);
                CurrentWeatherFragment currentWeatherFragment = CurrentWeatherFragment.init(currentWeather);
                currentWeatherFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_for_current_weather_fragment, currentWeatherFragment).commit();
            }
            if (intent.hasExtra(Keys.JSON_RESULT)) {
                initRecycleView(intent.getParcelableArrayListExtra(Keys.JSON_RESULT));
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(JsonResultReceiver);
    }

    private WeekTempAdapter initRecycleView(ArrayList<WeekWeather> weatherList) {
        RecyclerView recyclerView = findViewById(R.id.recycleView_for_week_weather);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        WeekTempAdapter weekTempAdapter = new WeekTempAdapter(this, weatherList);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getDrawable(R.drawable.separator));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(weekTempAdapter);
        return weekTempAdapter;
    }

    @Override
    public void sendCityAndTemp(String city, String temp) {
        favouriteCity = new FavouriteCity(city, temp);
    }

    @Override
    public void onBackPressed() {
        if (favouriteCity != null) {
            Intent intentResult = new Intent(WeatherDescription.this, MainActivity.class);
            intentResult.putExtra(Keys.FAVOURITES, favouriteCity);
            setResult(RESULT_OK, intentResult);
            Log.v(TAG, "передано " + favouriteCity.toString());
            finish();
        }
        super.onBackPressed();
    }
}


/*использовался до создания сервиса
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
                Log.v(TAG, "(MalformedURLException e");
                e.printStackTrace();
            } catch (IOException e) {
                Log.v(TAG, "IOException e");
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
            if (s == null) {
                Log.v(TAG, " в onPostExecute пришел null");
                AlertDialog.Builder builder = new AlertDialog.Builder(WeatherDescription.this);
                builder.setTitle(R.string.error)
                        .setMessage(R.string.city_error)
                        .setIcon(ContextCompat.getDrawable(WeatherDescription.this, R.drawable.ic_baseline_error_24))
                        .setCancelable(false)
                        .setPositiveButton(R.string.button_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                super.onPostExecute(s);
                Gson gson = new Gson();
                WeatherRequest weatherRequest = gson.fromJson(s, WeatherRequest.class);
                // getData(weatherRequest);
                // initDataSource(weatherRequest);
            }
        }
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

    private void initDataSource(WeatherRequest weatherRequest) {
        ArrayList<WeekWeather> weekWeathersList = new ArrayList<>();
        String data1 = "";
        for (int i = 0; i < weatherRequest.getList().length; i++) {
            WeekWeather current = new WeekWeather();
            String text = weatherRequest.getList()[i].getDt_txt();
            String data = editDay(text);
            if (data.equals(data1)) {
                Log.v(TAG, " повтор");
            } else {
                current.setDay(data);
                data1 = data;
                current.setTemp(weatherRequest.getList()[i].getMain().getTemp());
                current.setIcon(weatherRequest.getList()[i].getWeather()[0].getIcon());
                weekWeathersList.add(current);
            }
        }
        initRecycleView(weekWeathersList);
    }
        private String editDay(String data) {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat fmt2 = new SimpleDateFormat("E, d MMM", Locale.getDefault());
            try {
                Date date = fmt.parse(data);
                return fmt2.format(date);
            } catch (ParseException pe) {
                return "Date";
            }
        }

 */
