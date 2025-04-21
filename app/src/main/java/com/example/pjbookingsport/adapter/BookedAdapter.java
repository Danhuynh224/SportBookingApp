package com.example.pjbookingsport.adapter;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pjbookingsport.API.RetrofitClient;
import com.example.pjbookingsport.API.ServiceAPI;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.frag.BookedFragment;
import com.example.pjbookingsport.model.Booking;
import com.example.pjbookingsport.model.BookingInfo;
import com.example.pjbookingsport.model.Price;
import com.example.pjbookingsport.model.SportFacility;
import com.example.pjbookingsport.model.SubFacility;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        holder.tvFacilityName.setText(subFacility.getSportsFacility().getName());
        holder.tvAddress.setText(subFacility.getSportsFacility().getAddress());
        holder.layoutIcon.removeAllViews();
        List<Price> prices = subFacility.getSportsFacility().getPrices();
        List<String> types= new ArrayList<>();
        if (prices != null && !prices.isEmpty()) {
            for(Price price : prices){
                types.add(price.getFacilityType().getName());
            }
        }
        Log.d("LoaiSan", types.toString());
        // Danh sách icon phù hợp với loại sân thể thao
        List<Integer> iconList = new ArrayList<>();

        Log.d("DanhSachIcon:", iconList.toString());

        for (String type: types) {
            if (type.equals("Cầu lông")) {
                iconList.add(R.drawable.badminton);
            } else if (type.equals("Bóng đá")) {
                iconList.add(R.drawable.football);
            } else if (type.equals("Tennis")) {
                iconList.add(R.drawable.tennis);
            } else if (type.equals("Pickleball")) {
                iconList.add(R.drawable.pickle);
            } else if (type.equals("Bóng rổ")) {
                iconList.add(R.drawable.basketball);
            } else {
                iconList.add(R.drawable.volleyball);
            }
        }

        // Thêm icon vào LinearLayout
        for (int iconRes : iconList) {
            ImageView iconView = new ImageView(holder.itemView.getContext());
            int sizeInDp = 24;
            int marginTopInDp = 6;
            float scale = holder.itemView.getContext().getResources().getDisplayMetrics().density;
            int sizeInPx = (int) (sizeInDp * scale + 0.5f);
            int marginTopInPx = (int) (marginTopInDp * scale + 0.5f);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(sizeInPx, sizeInPx);
            params.setMargins(0, marginTopInPx, 0, 0);
            iconView.setLayoutParams(params);
            iconView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iconView.setImageResource(iconRes);

            // Căn giữa ngang giống layout XML
            iconView.setLayoutParams(params);
            iconView.setAdjustViewBounds(true);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            holder.layoutIcon.addView(iconView);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        holder.tvDate.setText(booking.getBookingDate().format(formatter));

        holder.layoutTimeSlots.removeAllViews();
        for (int i = 0; i < bookingInfos.size(); i++) {
            BookingInfo info = bookingInfos.get(i);
            SubFacility sub = info.getSubFacility();
            TextView timeSlotText = new TextView(holder.itemView.getContext());
            timeSlotText.setText(sub.getName() + ": " + info.getStartTime() + " - " + info.getEndTime());
            timeSlotText.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));
            timeSlotText.setTypeface(null, Typeface.BOLD);
            holder.layoutTimeSlots.addView(timeSlotText);
        }

        holder.tvBookingTime.setText("Đã đặt lúc " + booking.getBookingDate().format(formatter));
        holder.tvBookingID.setText("ID: " + booking.getBookingId());

        // Định dạng tiền
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.'); // Dùng dấu chấm (.) để phân tách hàng nghìn
        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);

        holder.tvPrice.setText(decimalFormat.format(booking.getTotalPrice())  + " VND");

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
        LinearLayout layoutTimeSlots, layoutIcon;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvFacilityName = itemView.findViewById(R.id.tvFacilityName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            layoutTimeSlots = itemView.findViewById(R.id.layoutTimeSlots);
            layoutIcon = itemView.findViewById(R.id.layoutIcon);
            tvBookingTime = itemView.findViewById(R.id.tvBookingTime);
            tvBookingID = itemView.findViewById(R.id.tvBookingID);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Booking booking);
    }

}
