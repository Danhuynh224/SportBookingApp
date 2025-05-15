package com.example.pjbookingsport.adapter;

import static com.example.pjbookingsport.R.*;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pjbookingsport.R;
import com.example.pjbookingsport.model.Booking;
import com.example.pjbookingsport.model.BookingInfo;
import com.example.pjbookingsport.model.SubFacility;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.SlotViewHolder> {

    private List<LocalTime> hourSlots;
    private SubFacility subFacility;
    private OnSlotClickListener listener;
    private int startPosition =-1 ;
    private int endPostion =-1 ;
    private int selectedPosition =-1 ;
    List<Booking> bookedList;
    private LocalDate dateBook;

    public SlotAdapter(List<LocalTime> hourSlots, SubFacility subFacility,LocalDate dateBook, List<Booking> bookedList ,OnSlotClickListener listener) {
        this.hourSlots = hourSlots;
        this.subFacility = subFacility;
        this.bookedList = bookedList;
        this.listener = listener;
        this.dateBook = dateBook;
        Log.d("Ngày trong Slot",this.dateBook.toString());
    }

    @NonNull
    @Override
    public SlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slot, parent,false);
        return new SlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotViewHolder holder, int position) {

        List<BookingInfo> bookingInfos;
        if(dateBook.equals(LocalDate.now()) && (hourSlots.get(position).isBefore(LocalTime.now())||hourSlots.get(position).equals(LocalTime.now())))
        {
            holder.cardSlot.setCardBackgroundColor(Color.parseColor("#858585"));
            holder.imgSlot.setImageResource(R.drawable.baseline_lock_24);
            holder.cardSlot.setClickable(false);
        }
        else if ( position >= startPosition && position<=endPostion) {
            holder.cardSlot.setCardBackgroundColor(Color.parseColor("#308EFF"));
            holder.imgSlot.setImageResource(R.drawable.baseline_check_24);
        }
        else {
            holder.cardSlot.setCardBackgroundColor(Color.parseColor("#DBECFF"));
            holder.imgSlot.setImageDrawable(null);

        }
        if(bookedList!=null){
            for(Booking booking : bookedList){
                bookingInfos = booking.getBookingInfos();
                for(BookingInfo bookingInfo: bookingInfos ){
                    if((hourSlots.get(position).equals(bookingInfo.getStartTime()) || (hourSlots.get(position).isAfter(bookingInfo.getStartTime()) && hourSlots.get(position).isBefore(bookingInfo.getEndTime())))
                            && subFacility.getSubFacilityId().equals(bookingInfo.getSubFacility().getSubFacilityId()))
                    {
                        holder.cardSlot.setCardBackgroundColor(Color.parseColor("#858585"));
                        holder.imgSlot.setImageResource(R.drawable.baseline_lock_24);
                        holder.cardSlot.setClickable(false);
                    }
                }
            }
        }

        holder.itemView.setOnClickListener(v->{

            selectedPosition = holder.getBindingAdapterPosition(); // Lấy vị trí thực tế
            if(startPosition==endPostion && startPosition==-1)
            {
                startPosition = selectedPosition;
                endPostion = selectedPosition;
            }
            else {
                if(selectedPosition==startPosition)
                {
                    startPosition+=1;
                }
                else if (endPostion == selectedPosition){
                    endPostion-=1;
                }
                else if((selectedPosition-startPosition <= endPostion - selectedPosition) ||selectedPosition <startPosition ){
                    startPosition = selectedPosition;
                }

                else {
                    endPostion = selectedPosition;
                }
                if(startPosition>endPostion){
                    startPosition=-1;
                    endPostion =-1;
                }
            }

            notifyDataSetChanged();

            if(startPosition!=-1 && endPostion!=-1) {
                listener.onSlotClick(hourSlots.get(startPosition), hourSlots.get(endPostion).plusHours(1), subFacility);
            }
            else {
                listener.onSlotClick(null, null, subFacility);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hourSlots.size();
    }

    static class SlotViewHolder extends RecyclerView.ViewHolder{
        ImageView imgSlot;
        CardView cardSlot;

        public SlotViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSlot = itemView.findViewById(R.id.imgSlot);
            cardSlot = itemView.findViewById(R.id.cardSlot);
        }
    }
    public interface OnSlotClickListener {
        void onSlotClick(LocalTime startHour, LocalTime endHour, SubFacility subFacility);
    }
}
