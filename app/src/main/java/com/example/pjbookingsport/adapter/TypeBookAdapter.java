package com.example.pjbookingsport.adapter;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pjbookingsport.R;
import com.example.pjbookingsport.model.FacilityType;
import com.example.pjbookingsport.model.Price;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class TypeBookAdapter extends RecyclerView.Adapter<TypeBookAdapter.TypeBookViewHolder> {
    private List<Price> prices;
    private OnDayClickListener listener;
    private int selectedPosition =0;

    public TypeBookAdapter(List<Price> prices, OnDayClickListener listener) {
        this.prices = prices;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TypeBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type, parent, false);
        return new TypeBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeBookViewHolder holder, int position) {
        Price price = prices.get(position);
        String type = price.getFacilityType().getName();
        holder.tvType.setText(type);
        //Thêm Icon

        switch (type){
            case "Cầu lông":
                holder.imgType.setImageResource(R.drawable.badminton);
                break;
            case "Bóng đá":
                holder.imgType.setImageResource(R.drawable.football);
                break;
            case "Tennis":
                holder.imgType.setImageResource(R.drawable.tennis);
                break;
            case "Pickleball":
                holder.imgType.setImageResource(R.drawable.pickle);
                break;
            case "Bóng rổ":
                holder.imgType.setImageResource(R.drawable.basketball);
                break;
            default:
                holder.imgType.setImageResource(R.drawable.volleyball);
                break;

        }


        // Kiểm tra nếu là vị trí đang được chọn
        if (selectedPosition == position) {
            holder.cardType.setCardBackgroundColor(Color.parseColor("#308EFF")); // Màu xanh khi được chọn
            holder.tvType.setTextColor(Color.WHITE);
        } else {
            holder.cardType.setCardBackgroundColor(Color.parseColor("#DBECFF")); // Màu trắng mặc định
            holder.tvType.setTextColor(Color.BLACK);
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
            listener.onDayClick(price.getFacilityType(), selectedPosition);
        });
    }


    @Override
    public int getItemCount() {
        return prices.size();
    }

    static class TypeBookViewHolder extends RecyclerView.ViewHolder {
        TextView tvType;
        ImageView imgType;
        CardView cardType;

        TypeBookViewHolder(View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvType);
            imgType = itemView.findViewById(R.id.imgType);
            cardType = itemView.findViewById(R.id.cardType);
        }
    }

    public interface OnDayClickListener {
        void onDayClick(FacilityType type, int position);
    }
}
