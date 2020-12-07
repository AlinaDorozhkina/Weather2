package com.example.weatherapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weatherapp.CurrentWeather;
import com.example.weatherapp.R;
import com.example.weatherapp.helper.Keys;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class CurrentWeatherFragment extends Fragment {
    private static final String CURRENT_WEATHER = "current_weather";
    private TextView textViewTemperature;
    private TextView textViewCity;
    private TextView textViewDescription;
    private MaterialButton favourites_button;
    private ImageView imageViewWeatherIcon;
    private Context context;
    private String city;
    private CurrentWeather currentWeather;
    private String imageUrl = "http://openweathermap.org/img/wn/%s@2x.png";

    private TextView textViewPressureValue;
    private TextView textViewWindSpeedValue;
    private TextView textViewPressure;
    private TextView textViewSpeed;
    private OnCurrentWeatherFragmentDataListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.layout_for_current_wether_fragment, container, false);
        initView(layout);
        return layout;
    }

    private void initView(View layout) {
        textViewCity = layout.findViewById(R.id.textViewCity);
        textViewTemperature = layout.findViewById(R.id.textViewTemperature);
        textViewDescription = layout.findViewById(R.id.textViewDescription);
        favourites_button = layout.findViewById(R.id.favourites_button);
        favourites_button.setOnClickListener(clickListener);
        textViewPressureValue = layout.findViewById(R.id.textViewPressureValue);
        textViewWindSpeedValue = layout.findViewById(R.id.textViewWindSpeedValue);
        textViewPressure = layout.findViewById(R.id.textViewPressure);
        textViewSpeed = layout.findViewById(R.id.textViewSpeed);
        imageViewWeatherIcon = layout.findViewById(R.id.imageViewWeatherIcon);
        TextView textViewData = layout.findViewById(R.id.textViewData);
        textViewData.setText(getTodayDateInStringFormat());
        context = getActivity();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            currentWeather = args.getParcelable(Keys.CURRENT_WEATHER);
            city = currentWeather.getCityName();
            textViewCity.setText(currentWeather.getCityName());
            textViewTemperature.setText(String.format("%s °", currentWeather.getTemperature()));
            textViewDescription.setText(currentWeather.getDescription());
            String icon = currentWeather.getIcon();
            Picasso.with(getContext()).load(String.format(imageUrl, icon)).into(imageViewWeatherIcon);

            if (args.containsKey(Keys.PRESSURE)) {
                Log.v("currentweather", String.valueOf(args.getInt(Keys.PRESSURE)));
                textViewPressureValue.setText(String.format("%s мм.рт.ст", args.getInt(Keys.PRESSURE)));
            } else {
                Log.v("currentweather", "пусто");
                textViewPressure.setVisibility(View.INVISIBLE);
            }
            if (args.containsKey(Keys.WIND_SPEED)) {
                textViewWindSpeedValue.setText(String.format("%s м/с", args.getInt(Keys.WIND_SPEED)));
            } else {
                textViewSpeed.setVisibility(View.INVISIBLE);
            }
        }
    }

    public static CurrentWeatherFragment init(CurrentWeather currentWeather) {
        CurrentWeatherFragment currentWeatherFragment = new CurrentWeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Keys.CURRENT_WEATHER, currentWeather);
        currentWeatherFragment.setArguments(bundle);
        return currentWeatherFragment;
    }


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String message = getString(R.string.snackbar_message_add, city);
            mListener.sendCityAndTemp(city, textViewTemperature.getText().toString());
            Snackbar
                    .make(v, message, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    };


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CURRENT_WEATHER, currentWeather);
    }

    private String getTodayDateInStringFormat() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("E, d MMMM", Locale.getDefault());
        return df.format(c.getTime());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCurrentWeatherFragmentDataListener) {
            mListener = (OnCurrentWeatherFragmentDataListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragment1DataListener");
        }
    }

    public interface OnCurrentWeatherFragmentDataListener {
        void sendCityAndTemp(String city, String temp);
    }
}