package com.example.weatherapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.parcelableEntities.FavouriteCity;
import com.example.weatherapp.R;
import com.example.weatherapp.WeatherDescription;
import com.example.weatherapp.helper.Keys;

import java.util.ArrayList;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder> {
    private final String TAG = FavouritesAdapter.class.getSimpleName();
    private ArrayList<FavouriteCity> cities;
    private Context context;
    private int menuPosition;

    public FavouritesAdapter(ArrayList<FavouriteCity> cities, Context context) {
        this.cities = cities;
        this.context = context;
    }

    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_cities, parent, false);
        return new FavouritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesViewHolder holder, final int position) {
        holder.textView_favourite_city.setText(cities.get(position).getName());
        Log.v(TAG, " установка значений"+cities.get(position).getName());
        holder.textView_favourite_city_temp.setText(cities.get(position).getTemperature());
        Log.v(TAG, " установка значений"+cities.get(position).getTemperature());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                menuPosition = position;
                return false;
            }
        });

        ((Activity) context).registerForContextMenu(holder.itemView);
    }

    public void delete(int position) {
        cities.remove(position);
        notifyItemRemoved(position);

    }

    public void open(int position) {
        Intent intent = new Intent(context, WeatherDescription.class);
        intent.putExtra(Keys.CITY, cities.get(position).getName());
        Log.v(TAG, cities.get(position).getName());
        context.startActivity(intent);
    }
    public int getMenuPosition() {
        return menuPosition;
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public class FavouritesViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_favourite_city;
        private TextView textView_favourite_city_temp;

        public FavouritesViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_favourite_city = itemView.findViewById(R.id.textView_favourite_city);
            textView_favourite_city_temp = itemView.findViewById(R.id.textView_favourite_city_temp);
        }
    }
}
