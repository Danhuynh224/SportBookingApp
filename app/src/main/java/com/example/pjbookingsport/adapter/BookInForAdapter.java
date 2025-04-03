package com.example.pjbookingsport.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pjbookingsport.R;
import com.example.pjbookingsport.model.BookingInfo;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class BookInForAdapter extends RecyclerView.Adapter<BookInForAdapter.BookInforViewHolder> {
    private final List<BookingInfo> bookingInfos;

    public BookInForAdapter(List<BookingInfo> bookingInfos) {
        this.bookingInfos = bookingInfos;
    }

    @NonNull
    @Override
    public BookInforViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inforbook, parent, false);
        return new BookInforViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookInforViewHolder holder, int position) {
        BookingInfo bookingInfo = bookingInfos.get(position);
        holder.tvSubName.setText(bookingInfo.getSubFacility().getName());
        holder.tvHour.setText(bookingInfo.getStartTime() +" - "+bookingInfo.getEndTime());
        // Định dạng số với dấu chấm phân cách hàng nghìn
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.'); // Dùng dấu chấm (.) để phân tách hàng nghìn

        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        holder.tvMoney.setText(decimalFormat.format(bookingInfo.getTotalPrice())+" VND");

    }

    @Override
    public int getItemCount() {
        Log.d("getItemCount", "Số lượng phần tử: " + (bookingInfos == null ? 0 : bookingInfos.size()));
        return bookingInfos == null ? 0 : bookingInfos.size();
    }

    static  class BookInforViewHolder extends RecyclerView.ViewHolder{
        TextView tvSubName, tvHour, tvMoney;
        public BookInforViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubName = itemView.findViewById(R.id.subnametv);
            tvHour = itemView.findViewById(R.id.hourtv);
            tvMoney = itemView.findViewById(R.id.moneytv);
        }
    }
}
