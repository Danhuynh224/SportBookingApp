package com.example.pjbookingsport.adapter;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pjbookingsport.R;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {
    private List<LocalDate> days;
    private OnDayClickListener listener;
    private int selectedPosition =0;

    public DayAdapter(List<LocalDate> days, OnDayClickListener listener) {
        this.days = days;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        LocalDate date = days.get(position);
        holder.tvDay.setText(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, new Locale("vi")));
        holder.tvDate.setText(String.valueOf(date.getDayOfMonth()));

        // Kiểm tra nếu là vị trí đang được chọn
        if (selectedPosition == position) {
            holder.cardDay.setCardBackgroundColor(Color.parseColor("#308EFF")); // Màu xanh khi được chọn
            holder.tvDay.setTextColor(Color.WHITE);
            holder.tvDate.setTextColor(Color.WHITE);
        } else {
            holder.cardDay.setCardBackgroundColor(Color.parseColor("#DBECFF")); // Màu trắng mặc định
            holder.tvDay.setTextColor(Color.BLACK);
            holder.tvDate.setTextColor(Color.BLACK);
        }

        // Bắt sự kiện click
        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = holder.getBindingAdapterPosition(); // Lấy vị trí thực tế

            // Chỉ cập nhật lại item nếu vị trí hợp lệ
            if (previousPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(previousPosition);
            }
            notifyItemChanged(selectedPosition);
            listener.onDayClick(date, selectedPosition);
        });
    }


    @Override
    public int getItemCount() {
        return days.size();
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvDay;
        CardView cardDay;

        DayViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDay = itemView.findViewById(R.id.tvDay);
            cardDay = itemView.findViewById(R.id.cardDay);
        }
    }

    public interface OnDayClickListener {
        void onDayClick(LocalDate date, int position);
    }
}
