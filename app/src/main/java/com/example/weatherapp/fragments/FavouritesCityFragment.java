package com.example.weatherapp.fragments;

import android.os.Bundle;
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

import java.util.ArrayList;

public class FavouritesCityFragment extends Fragment {
    private  View view;
    private FavouritesAdapter favouritesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_favourites_city, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public FavouritesAdapter setFavoriteCity(ArrayList<FavouriteCity> cities) {
        RecyclerView recyclerView=view.findViewById(R.id.recycleView_for_favourites);
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
