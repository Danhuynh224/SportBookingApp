package com.example.pjbookingsport.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pjbookingsport.API.RetrofitClient;
import com.example.pjbookingsport.API.ServiceAPI;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.model.Booking;
import com.example.pjbookingsport.model.FacilityType;
import com.example.pjbookingsport.model.SubFacility;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubFaAdapter extends RecyclerView.Adapter<SubFaAdapter.SubFaViewHolder> {

    private List<SubFacility> subFacilities;
    private List<LocalTime> hours;
    private SlotAdapter.OnSlotClickListener listener;
    private LocalDate dateBook;
    private ServiceAPI serviceAPI;

    public SubFaAdapter(List<SubFacility> subFacilities, List<LocalTime> hours, LocalDate dateBook, SlotAdapter.OnSlotClickListener listener) {
        this.subFacilities = subFacilities;
        this.hours = hours;
        this.listener = listener;
        this.dateBook = dateBook;

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
        getBookedList(dateBook,subFacility,holder);

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
    public void getBookedList(LocalDate bookingDate, SubFacility subFacility, @NonNull SubFaViewHolder holder){
        serviceAPI = RetrofitClient.getClient().create(ServiceAPI.class);
        serviceAPI.getByDateAndTypeAndSubFa(bookingDate,subFacility.getFacilityType().getFacilityTypeId(),subFacility.getSubFacilityId()).enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                List<Booking> bookedList ;
                bookedList = response.body();
                holder.txtSub.setText(subFacility.getName());
                // Tạo adapter cho RecyclerView con và thiết lập LayoutManager
                SlotAdapter slotAdapter = new SlotAdapter(hours, subFacility,dateBook, bookedList, listener);
                holder.rvSlots.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL, false));
                holder.rvSlots.setAdapter(slotAdapter);

            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                Log.d("API ERROR", " " + t.getMessage());
            }
        });
    }

}
