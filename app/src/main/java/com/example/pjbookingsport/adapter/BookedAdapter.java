package com.example.pjbookingsport.adapter;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pjbookingsport.R;
import com.example.pjbookingsport.frag.BookedFragment;
import com.example.pjbookingsport.model.Booking;
import com.example.pjbookingsport.model.BookingInfo;
import com.example.pjbookingsport.model.SportFacility;
import com.example.pjbookingsport.model.SubFacility;

import java.util.List;

public class BookedAdapter extends RecyclerView.Adapter<BookedAdapter.BookingViewHolder> {
    private List<Booking> bookingList;
    private OnItemClickListener listener;

    public BookedAdapter(List<Booking> bookingList, OnItemClickListener listener) {
        this.bookingList = bookingList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_book, parent, false);
        return new BookingViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        List<BookingInfo> bookingInfos = booking.getBookingInfos();

        if (bookingInfos == null || bookingInfos.isEmpty()) {
            return;
        }

        SubFacility subFacility = bookingInfos.get(0).getSubFacility();
        SportFacility sportFacility = subFacility.getSportFacility();


        holder.tvDate.setText(booking.getBookingDate().toString());
        holder.tvFacilityName.setText(sportFacility.getName());
        holder.tvAddress.setText(sportFacility.getAddress());

        holder.layoutTimeSlots.removeAllViews();
        for (int i = 0; i < bookingInfos.size(); i++) {
            BookingInfo info = bookingInfos.get(i);
            SubFacility sub = info.getSubFacility();
            TextView timeSlotText = new TextView(holder.itemView.getContext());
            timeSlotText.setText(sub.getName() + ": " + info.getStartTime() + " - " + info.getEndTime());
            timeSlotText.setTextColor(R.color.black);
            timeSlotText.setTypeface(null, Typeface.BOLD);
            holder.layoutTimeSlots.addView(timeSlotText);
        }

        holder.tvBookingTime.setText("Đã đặt lúc " + booking.getBookingDate());
        holder.tvBookingID.setText("ID: " + booking.getBookingId());
        holder.tvPrice.setText(booking.getTotalPrice() + " VND");

        holder.itemView.setOnClickListener(v -> listener.onItemClick(booking));
    }

    @Override
    public int getItemCount() {
        if(bookingList != null){
            return bookingList.size();
        }
        return 0;
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvFacilityName, tvAddress, tvBookingTime, tvBookingID, tvPrice;
        LinearLayout layoutTimeSlots;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvFacilityName = itemView.findViewById(R.id.tvFacilityName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            layoutTimeSlots = itemView.findViewById(R.id.layoutTimeSlots);
            tvBookingTime = itemView.findViewById(R.id.tvBookingTime);
            tvBookingID = itemView.findViewById(R.id.tvBookingID);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Booking booking);
    }

}
