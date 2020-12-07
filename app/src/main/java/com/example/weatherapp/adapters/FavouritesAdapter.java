package com.example.weatherapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.FavouriteCity;
import com.example.weatherapp.R;

import java.util.ArrayList;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder> {
    private ArrayList<FavouriteCity> cities;

    public FavouritesAdapter(ArrayList<FavouriteCity> cities) {
        this.cities = cities;
    }

    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_cities, parent, false);
        return new FavouritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesViewHolder holder, int position) {
        holder.textView_favourite_city.setText(cities.get(position).getName());
        holder.textView_favourite_city_temp.setText(cities.get(position).getTemperature());
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
