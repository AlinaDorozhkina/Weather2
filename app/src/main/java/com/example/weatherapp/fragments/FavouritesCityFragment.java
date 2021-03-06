package com.example.weatherapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.parcelableEntities.FavouriteCity;
import com.example.weatherapp.R;
import com.example.weatherapp.adapters.FavouritesAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class FavouritesCityFragment extends Fragment {
    private FavouritesAdapter favouritesAdapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites_city, container, false);
        recyclerView = view.findViewById(R.id.recycleView_for_favourites_city);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public FavouritesAdapter setFavoriteCities(ArrayList<FavouriteCity> cities) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        favouritesAdapter =new FavouritesAdapter(cities, getActivity());
        recyclerView.setAdapter(favouritesAdapter);
        return favouritesAdapter;
    }

    public FavouritesAdapter getFavouritesAdapter() {
        return favouritesAdapter;
    }
}
