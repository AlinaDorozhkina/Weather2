package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int SETTINGS_CODE = 1;
    private ArrayList<String> favouritesCities;
    private FavouritesCityFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(intent, SETTINGS_CODE);
            }
        });
    }

    private void prepareFavourites() {
        FragmentManager manager = getSupportFragmentManager();
        fragment = (FavouritesCityFragment) manager.findFragmentById(R.id.fragment_for_favorites);
        if (fragment != null) {
            fragment.setFavoriteCity(favouritesCities);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_CODE) {
            recreate();
        }
        if (data != null) {
            if (favouritesCities == null) {
                favouritesCities = new ArrayList<>();
            }
            favouritesCities.add(data.getStringExtra(Keys.FAVOURITES));
            prepareFavourites();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(), "onStart()", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(), "onResume()", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(), "onPause()", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle saveInstanceState) {
        Toast.makeText(getApplicationContext(), "onSaveInstanceState()", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onSaveInstanceState()");
        if (favouritesCities != null) {
            saveInstanceState.putStringArrayList("favourites", favouritesCities);
            super.onSaveInstanceState(saveInstanceState);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle saveInstanceState) {
        super.onRestoreInstanceState(saveInstanceState);
        Toast.makeText(getApplicationContext(), "Повторный запуск!! - onRestoreInstanceState()", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Повторный запуск!! - onRestoreInstanceState()");

        if (saveInstanceState != null) {
            if (saveInstanceState.containsKey("favourites")) {
                this.favouritesCities = saveInstanceState.getStringArrayList("favourites");
                prepareFavourites();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(getApplicationContext(), "onStop()", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(getApplicationContext(), "onRestart()", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onRestart");
//        if (favouritesCities!=null) {
//            save();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "onDestroy()", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onDestroy()");
    }

   /* public void save (){
   Делала эти два метода, чтоб при перезапуске приложения избранные города .Но не получилось
        Set<String> set = new HashSet<>(favouritesCities);
        prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putStringSet(SAVED_DATA, set);
        ed.apply();
    }
    public void load (){
        prefs = getPreferences(MODE_PRIVATE);
        Set<String> set = new HashSet<>(favouritesCities);
        Set<String> saved = prefs.getStringSet(SAVED_DATA, set);
        favouritesCities = new ArrayList<>(saved);
    }

    */

}