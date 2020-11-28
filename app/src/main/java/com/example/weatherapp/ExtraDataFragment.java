package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ExtraDataFragment extends Fragment {
    private TextView textViewPressureValue;
    private TextView textViewWindSpeedValue;
    private TextView textViewMoistureValue;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_extra_data, container, false);
        textViewPressureValue = view.findViewById(R.id.textViewPressureValue);
        textViewWindSpeedValue = view.findViewById(R.id.textViewWindSpeedValue);
        textViewMoistureValue = view.findViewById(R.id.textViewMoistureValue);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().getBoolean(Keys.PRESSURE)) {
                textViewPressureValue.setText(setDataParameter());
            }
            if (getArguments().getBoolean(Keys.WIND_SPEED)) {
                textViewWindSpeedValue.setText(setDataParameter());
            }
            if (getArguments().getBoolean(Keys.MOISTURE)) {
                textViewMoistureValue.setText(setDataParameter());
            }
        }
    }

    private String setDataParameter() {
        return String.valueOf((int) (Math.random() * 30));
    }

}




