package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.adapters.FavouritesAdapter;
import com.example.weatherapp.fragments.FavouritesCityFragment;
import com.example.weatherapp.fragments.LoginFragment;
import com.example.weatherapp.fragments_from_navigation_drawer.FragmentAboutApp;
import com.example.weatherapp.fragments_from_navigation_drawer.FragmentSendingEmail;

import com.example.weatherapp.helper.Keys;
import com.example.weatherapp.parcelableEntities.FavouriteCity;
import com.example.weatherapp.utils.NetworkUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, LoginFragment.OnLoginFragmentDataListener {

    private static final int REQUEST_CODE = 1;
    private AutoCompleteTextView autoCompleteTextView;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int SETTINGS_CODE = 1;
    private ArrayList<FavouriteCity> favouritesCities;
    private FavouritesAdapter favouritesAdapter;
    private FavouritesCityFragment fragment;
    private LoginFragment loginFragment;
    private TextView header_instruction;
    private ImageView imageView_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autoCompleteTextView = findViewById(R.id.textInput_enter_city);
        Button button_show =findViewById(R.id.button_show);
        button_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherDescription.class);
                String value = autoCompleteTextView.getText().toString().trim();
                if (!value.isEmpty()) {
                    Log.d(TAG, "передача бандла " + value);
                    intent.putExtra(Keys.CITY, value);
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    Snackbar.make(v, R.string.enter_city, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        Toolbar toolbar = initToolbar();
        initDrawer(toolbar);
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
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
//        if (requestCode != REQUEST_CODE) {
//            super.onActivityResult(requestCode, resultCode, data);
//            return;
//        }
        if (resultCode == RESULT_OK && requestCode==REQUEST_CODE) {
            Log.v(TAG, " получено значение из другой активити " + data.getParcelableExtra(Keys.FAVOURITES));
            if (favouritesCities == null) {
                favouritesCities = new ArrayList<>();
                Log.v(TAG, " создаем новый эрей лист");
            }
            favouritesCities.add(data.getParcelableExtra(Keys.FAVOURITES));
            Log.v(TAG, " добавии в лист значение" + data.getParcelableExtra(Keys.FAVOURITES));
            prepareFavourites();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        favouritesAdapter = fragment.getFavouritesAdapter();

        switch (id) {
            case R.id.open_context:
                favouritesAdapter.open(favouritesAdapter.getMenuPosition());
                return true;
            case R.id.delete_context:
                favouritesAdapter.delete(favouritesAdapter.getMenuPosition());
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.settings1:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(intent, SETTINGS_CODE);
                return true;
            case R.id.loginPassword:
                loginFragment = new LoginFragment();
                loginFragment.show(getSupportFragmentManager(), "fragment");
                //getSupportFragmentManager().beginTransaction().replace(R.id.frame_for_extra, loginFragment).addToBackStack(null).commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.v(TAG, " вызов метода onNavigationItemSelected");
        int id = item.getItemId();
        switch (id) {
            case R.id.write_to_developer:
                FragmentSendingEmail fragment_email = new FragmentSendingEmail();
                fragment_email.show(getSupportFragmentManager(), "fragment");
                //getSupportFragmentManager().beginTransaction().replace(R.id.frame_for_extra, fragment_email).addToBackStack(null).commit();
                break;
            case R.id.about_me:
                break;
            case R.id.about_app:
                FragmentAboutApp fragmentAboutApp = new FragmentAboutApp();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_for_extra, fragmentAboutApp).addToBackStack(null).commit();
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void sendName(String name) {
        Log.v(TAG, " получено " + name);
        if (loginFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(loginFragment).commit();
            correctHeader(name);
        }
    }

    private void correctHeader(String name) {
        header_instruction = findViewById(R.id.header_instruction);
        imageView_header = findViewById(R.id.imageView_header);
        imageView_header.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_tag_faces_24));
        header_instruction.setText(getString(R.string.Welcome, name));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        super.onSaveInstanceState(saveInstanceState);
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