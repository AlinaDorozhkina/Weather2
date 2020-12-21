package com.example.weatherapp.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class JsonFileReaderTask extends AsyncTaskLoader<ArrayList<String>> {
    private String fileName;

    public JsonFileReaderTask (Context context, String fileName){
        super(context);
        this.fileName=fileName;
    }

    @Nullable
    @Override
    public ArrayList<String> loadInBackground() {
        ArrayList<String> cities = new ArrayList<>();
        JsonReader jsonReader = null;
        try {
            InputStream is =getContext().getAssets().open(fileName);
            InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
            jsonReader = new JsonReader(streamReader);
            Gson gson = new GsonBuilder().create();
            jsonReader.beginArray();
            while (jsonReader.hasNext()){
                AllCity allCity = gson.fromJson(jsonReader, AllCity.class);
                String city = allCity.getName();
                cities.add(city);
            }
            jsonReader.endArray();
            Log.v("JsonFileReader", cities.toString());
            Log.v("JsonFileReader", ""+cities.size());
            return cities;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    @Override
//    protected ArrayList<String> doInBackground(String... strings) {
//        ArrayList<String> cities = new ArrayList<>();
//        JsonReader jsonReader = null;
//        try {
//            InputStream is =context.getAssets().open(strings[0]);
//            InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
//            jsonReader = new JsonReader(streamReader);
//            Gson gson = new GsonBuilder().create();
//            jsonReader.beginArray();
//            while (jsonReader.hasNext()){
//                AllCity allCity = gson.fromJson(jsonReader, AllCity.class);
//                String city = allCity.getName();
//                cities.add(city);
//            }
//            jsonReader.endArray();
//            Log.v("JsonFileReader", cities.toString());
//            Log.v("JsonFileReader", ""+cities.size());
//            return cities;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


//    public static ArrayList<String> read (Context context, String file){
//        ArrayList<String> cities = new ArrayList<>();
//        try {
//            InputStream is =context.getAssets().open(file);
//            InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
//            JsonReader jsonReader = new JsonReader(streamReader);
//            Gson gson = new GsonBuilder().create();
//            jsonReader.beginArray();
//            while (jsonReader.hasNext()){
//                AllCity allCity = gson.fromJson(jsonReader, AllCity.class);
//                String city = allCity.getName();
//                cities.add(city);
//            }
//            jsonReader.endArray();
//            Log.v("Util", cities.toString());
//            Log.v("Util", ""+cities.size());
//            return cities;
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//
//        }
//        return null;
//    }
}



