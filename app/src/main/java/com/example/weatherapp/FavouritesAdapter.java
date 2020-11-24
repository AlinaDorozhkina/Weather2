package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder> {
    private ArrayList<String> cities;

    public FavouritesAdapter(ArrayList<String> cities) {
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
        holder.addCity(cities.get(position));
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public class FavouritesViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_favourite_city;

        public FavouritesViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_favourite_city = itemView.findViewById(R.id.textView_favourite_city);
        }

        public void addCity(String name) {
            textView_favourite_city.setText(name);
        }
    }
}
