package com.example.weatherapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.parcelableEntities.WeekWeather;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WeekTempAdapter extends RecyclerView.Adapter<WeekTempAdapter.ViewHolder> {
    private ArrayList<WeekWeather> dailyWeather;
    protected Context context;
    private String urlImage = "http://openweathermap.org/img/wn/%s@2x.png";

    public WeekTempAdapter(Context context, ArrayList<WeekWeather> dailyWeather) {
        this.context = context;
        this.dailyWeather = dailyWeather;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.day_of_the_week.setText(dailyWeather.get(position).getDay());
        holder.daily_temp.setText((Math.round(dailyWeather.get(position).getTemp()) + "°"));
        Picasso.with(context).load(String.format(urlImage, dailyWeather.get(position).getIconUrl())).into(holder.image_view_for_weather);
    }

    @Override
    public int getItemCount() {
        return dailyWeather.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView day_of_the_week;
        private TextView daily_temp;
        private ImageView image_view_for_weather;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // itemView это доступ к нашей разметке;
            day_of_the_week = itemView.findViewById(R.id.textview_day_of_the_week);
            daily_temp = itemView.findViewById(R.id.textview_day_temp);
            image_view_for_weather = itemView.findViewById(R.id.image_view_for_weather);
        }
    }
}

