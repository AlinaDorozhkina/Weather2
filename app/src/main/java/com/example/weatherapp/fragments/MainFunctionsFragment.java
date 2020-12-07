package com.example.weatherapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.weatherapp.R;
import com.example.weatherapp.WeatherDescription;
import com.example.weatherapp.helper.Keys;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;


public class MainFunctionsFragment extends Fragment {
    private final static String TAG = MainFunctionsFragment.class.getSimpleName();
    private static final int REQUEST_CODE = 1;
    private CheckBox pressure;
    private CheckBox speed;
    private TextInputEditText autoCompleteTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main_functions, container, false);
        pressure = layout.findViewById(R.id.pressure);
        speed = layout.findViewById(R.id.speed);
        autoCompleteTextView = layout.findViewById(R.id.textInput_enter_city);
        Button button_show = layout.findViewById(R.id.button_show);
        button_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WeatherDescription.class);
                String value = autoCompleteTextView.getText().toString().trim();
                if (!value.isEmpty()) {
                    Log.d(TAG, "передача бандла " + value);
                    intent.putExtra(Keys.CITY, value);
                    if (pressure.isChecked()) {
                        intent.putExtra(Keys.PRESSURE, true);
                    }
                    if (speed.isChecked()) {
                        intent.putExtra(Keys.WIND_SPEED, true);
                    }
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    Snackbar.make(v, R.string.enter_city, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        return layout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        Log.v(TAG, " выполние onActivityResult");
        if (requestCode != REQUEST_CODE) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (resultCode == Activity.RESULT_OK) {
            Log.v(TAG, " получено значение из другой активити " + data.getParcelableExtra(Keys.FAVOURITES));
        }
    }
}
