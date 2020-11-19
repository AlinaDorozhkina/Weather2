package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeekTempAdapter extends RecyclerView.Adapter<WeekTempAdapter.ViewHolder> {
    private String[] dataSource;

    public WeekTempAdapter(String[] dataSource) {
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(dataSource[position]);
    }

    @Override
    public int getItemCount() {
        return dataSource.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView day_of_the_week;
        private TextView daily_temp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // textView=(TextView)itemView;
            day_of_the_week = itemView.findViewById(R.id.textview_day_of_the_week);
            daily_temp = itemView.findViewById(R.id.textview_day_temp);
        }

        public void setData(String text) {
            day_of_the_week.setText(text);
            daily_temp.setText(String.valueOf((int) (Math.random() * 30)));
        }
    }
}
