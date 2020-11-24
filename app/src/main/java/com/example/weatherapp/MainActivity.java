package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private final String SAVED_DATA = "savedData";
    private ArrayList<String> favouritesCities;
    private FavouritesCityFragment fragment;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  load();

//        String instanceState;
//        if (savedInstanceState == null) {
//            instanceState = "Первый запуск!";
//            Log.d(TAG, "лог: первый запуск");
//        } else {
//            instanceState = "Повторный запуск!";
//            Log.d(TAG, "лог: повторный запуск");
//        }
//        Toast.makeText(getApplicationContext(), instanceState + " - onCreate()", Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "on create");
    }
    private void prepareFavourites (){
        FragmentManager manager = getSupportFragmentManager();
        fragment  = (FavouritesCityFragment) manager.findFragmentById(R.id.fragment_for_favorites);
        if (fragment!=null){
            fragment.setFavoriteCity(favouritesCities);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (favouritesCities==null){
                favouritesCities=new ArrayList<>();
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

        if (saveInstanceState!=null) {
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