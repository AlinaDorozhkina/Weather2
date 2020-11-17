package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String instanceState;
        if (savedInstanceState == null) {
            instanceState = "Первый запуск!";
            Log.d(TAG, "лог: первый запуск");
        } else {
            instanceState = "Повторный запуск!";
            Log.d(TAG, "лог: повторный запуск");
        }
        Toast.makeText(getApplicationContext(), instanceState + " - onCreate()", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "on create");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Log.d(TAG, " вызов onActivityResult" + data.getStringExtra(Keys.FAVOURITES));
            FragmentManager manager = getSupportFragmentManager();
            FavouritesCityFragment fragment = (FavouritesCityFragment) manager.findFragmentById(R.id.fragment_for_favorites);
            if (fragment!=null) {
                Log.d(TAG, " передаю значение");
                fragment.createViewForFavouriteCity(data.getStringExtra(Keys.FAVOURITES));
            } else {
                Log.d(TAG, " фрагмент пуст");
            }
        }
    }

    @Override
    protected void onStart() {
        // вызывается после onCreate, активити еще не видима,  после  активити станет видимой
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
        super.onSaveInstanceState(saveInstanceState);
        Toast.makeText(getApplicationContext(), "onSaveInstanceState()", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onSaveInstanceState()");
    }
    @Override
    protected void onRestoreInstanceState(Bundle saveInstanceState) {
        super.onRestoreInstanceState(saveInstanceState);

        Toast.makeText(getApplicationContext(), "Повторный запуск!! - onRestoreInstanceState()", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Повторный запуск!! - onRestoreInstanceState()");
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "onDestroy()", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onDestroy()");
    }
}