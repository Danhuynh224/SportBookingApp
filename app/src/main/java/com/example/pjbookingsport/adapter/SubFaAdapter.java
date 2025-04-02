package com.example.pjbookingsport.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pjbookingsport.R;
import com.example.pjbookingsport.model.SubFacility;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SubFaAdapter extends RecyclerView.Adapter<SubFaAdapter.SubFaViewHolder> {

    private List<SubFacility> subFacilities;
    private List<LocalTime> hours;
    private SlotAdapter.OnSlotClickListener listener;
    private LocalDate dateBook;

    public SubFaAdapter(List<SubFacility> subFacilities, List<LocalTime> hours, LocalDate dateBook, SlotAdapter.OnSlotClickListener listener) {
        this.subFacilities = subFacilities;
        this.hours = hours;
        this.listener = listener;
        this.dateBook = dateBook;
        Log.d("Ngày trong Sub",this.dateBook.toString());
    }

    @NonNull
    @Override
    public SubFaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subfa, parent, false);
        return new SubFaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubFaViewHolder holder, int position) {
        SubFacility subFacility = subFacilities.get(position);
        holder.txtSub.setText(subFacility.getName());

        // Tạo adapter cho RecyclerView con và thiết lập LayoutManager
        SlotAdapter slotAdapter = new SlotAdapter(hours, subFacility,dateBook, listener);
        holder.rvSlots.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL, false));
        holder.rvSlots.setAdapter(slotAdapter);
    }

    @Override
    public int getItemCount() {
        return subFacilities.size();
    }

    static class SubFaViewHolder extends RecyclerView.ViewHolder {
        TextView txtSub;
        RecyclerView rvSlots;

        public SubFaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSub = itemView.findViewById(R.id.txtSubfa);
            rvSlots = itemView.findViewById(R.id.rv_slots);
        }
    }

}
