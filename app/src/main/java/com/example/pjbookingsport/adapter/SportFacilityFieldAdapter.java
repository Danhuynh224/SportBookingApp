package com.example.pjbookingsport.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pjbookingsport.R;
import com.example.pjbookingsport.model.SportFacility;

import java.util.List;

public class SportFacilityFieldAdapter extends RecyclerView.Adapter<SportFacilityFieldAdapter.ViewHolder> {
    private Context context;
    private List<SportFacility> sportFieldList;

    public SportFacilityFieldAdapter(Context context, List<SportFacility> sportFieldList) {
        this.context = context;
        this.sportFieldList = sportFieldList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_facility, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SportFacility sportField = sportFieldList.get(position);

//        holder.image.setImageResource(sportField.getImg());
//        holder.title.setText(sportField.getTitle());
//        holder.address.setText(sportField.getAddress());
//        holder.ratingValue.setText(sportField.getRating() + "/5");
//
//        holder.bookButton.setOnClickListener(v -> {
//            // Xử lý khi nhấn nút Đặt Lịch
//        });
    }

    @Override
    public int getItemCount() {
        return sportFieldList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, address, ratingValue;
        Button bookButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            address = itemView.findViewById(R.id.address);
            ratingValue = itemView.findViewById(R.id.rating_value);
            bookButton = itemView.findViewById(R.id.book_button);
        }
    }
}
