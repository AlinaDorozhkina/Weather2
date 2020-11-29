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
    private TextView textViewPressure;
    private TextView textViewSpeed;
    private TextView textViewMoisture;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_extra_data, container, false);
        textViewPressureValue = view.findViewById(R.id.textViewPressureValue);
        textViewWindSpeedValue = view.findViewById(R.id.textViewWindSpeedValue);
        textViewMoistureValue = view.findViewById(R.id.textViewMoistureValue);
        textViewPressure =view.findViewById(R.id.textViewPressure);
        textViewSpeed =view.findViewById(R.id.textViewSpeed);
        textViewMoisture=view.findViewById(R.id.textViewMoisture);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments()!=null){
            if (getArguments().getString(Keys.PRESSURE)!=null){
                textViewPressureValue.setText(getArguments().getString(Keys.PRESSURE));
            } else {
                textViewPressure.setVisibility(View.INVISIBLE);
            }
            if (getArguments().getString(Keys.WIND_SPEED)!=null) {
                textViewWindSpeedValue.setText(getArguments().getString(Keys.WIND_SPEED));
            }else {
                textViewSpeed.setVisibility(View.INVISIBLE);
            }
            if (getArguments().getString(Keys.MOISTURE)!=null) {
                textViewMoistureValue.setText(getArguments().getString(Keys.MOISTURE));
            }else{
                textViewMoisture.setVisibility(View.INVISIBLE);
            }
        }
    }
}





