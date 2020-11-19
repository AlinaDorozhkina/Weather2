package com.example.weatherapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FavouritesCityFragment extends Fragment {
    private static final String TAG = FavouritesCityFragment.class.getSimpleName();
    private View layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites_city, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layout = view;
    }

    public void createViewForFavouriteCity(String name) {
        Log.d(TAG, " получено значение " + name);
        TextView textView = new TextView(getContext());
        textView.setText(name);
        textView.setTextSize(40);
        LinearLayout layoutView =layout.findViewById(R.id.liner_for_favourites);
        layoutView.addView(textView);
    }
}
