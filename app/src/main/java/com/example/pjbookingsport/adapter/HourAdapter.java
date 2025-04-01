package com.example.pjbookingsport.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pjbookingsport.R;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {
    private List<LocalTime> hours;

    public HourAdapter(List<LocalTime> hour) {
        this.hours = hour;
    }

    @NonNull
    @Override
    public HourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timehour, parent, false);
        return new HourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourViewHolder holder, int position) {
            LocalTime startTime = hours.get(position);
            LocalTime endTime = startTime.plusHours(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String timeRange = startTime.format(formatter) + " - " + endTime.format(formatter);
            Log.d("Giờ", timeRange);
            holder.txtHour.setText(timeRange);
    }

    @Override
    public int getItemCount() {
        return hours.size();
    }

    static class HourViewHolder extends RecyclerView.ViewHolder{
        TextView txtHour;
        HourViewHolder(View itemView){
            super(itemView);
            txtHour = itemView.findViewById(R.id.txtHour);
        }
    }
}
