package com.example.pjbookingsport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pjbookingsport.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ButtonCardAdapter extends RecyclerView.Adapter<ButtonCardAdapter.ViewHolder> {
    private List<String> items;
    private Context context;
    private Set<Integer> selectedPositions = new HashSet<>();

    public ButtonCardAdapter(Context context, List<String> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_btn_filter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = items.get(position);
        holder.textView.setText(item);

        if (selectedPositions.contains(position)) {
            // Khi được chọn
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.primary));
            holder.cardView.setStrokeWidth(0); // Ẩn border
            holder.textView.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        } else {
            // Khi không được chọn
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.background));
            holder.cardView.setStrokeWidth(1); // Hiện border
            holder.cardView.setStrokeColor(ContextCompat.getColor(context, R.color.textDisabled));
            holder.textView.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        }

        holder.cardView.setOnClickListener(v -> {
            if (selectedPositions.contains(position)) {
                selectedPositions.remove(position);
            } else {
                selectedPositions.add(position);
            }
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Set<Integer> getSelectedPositions() {
        return selectedPositions;
    }

    public void clearSelection() {
        selectedPositions.clear();
        notifyDataSetChanged();
    }

    public List<String> getSelectedItems() {
        List<String> selectedItems = new ArrayList<>();
        for (Integer position : selectedPositions) {
            selectedItems.add(items.get(position));
        }
        return selectedItems;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardButton);
            textView = itemView.findViewById(R.id.tvType);
        }
    }
}