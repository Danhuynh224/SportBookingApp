package com.example.pjbookingsport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pjbookingsport.R;
import com.example.pjbookingsport.frag.FacilityPriceFragment;
import com.example.pjbookingsport.model.Price;

import java.text.DecimalFormat;
import java.util.List;

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.PriceViewHolder> {
    private  List<Price> mPrice;
    private  Context mContext;
    private LayoutInflater mLayoutInflater;
    public PriceAdapter(Context context, List<Price> price) {
        mPrice = price;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PriceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.table_price_item, parent, false);
        return new PriceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceViewHolder holder, int position) {
        DecimalFormat df = new DecimalFormat("#.##");
        Price price = mPrice.get(position);
        holder.tvEarly.setText(df.format(price.getEarlyTime())+"VND");
        holder.tvDay.setText(df.format(price.getDayTime())+"VND");
        holder.tvNight.setText(df.format(price.getNightTime())+"VND");
        holder.tvWeek.setText(df.format(price.getWeekTime())+"VND");
        holder.tvType.setText(price.getFacilityType().getName());
    }

    @Override
    public int getItemCount() {
        return mPrice != null ? mPrice.size() : 0;
    }

    public static class PriceViewHolder extends RecyclerView.ViewHolder {
        private TextView tvEarly;
        private TextView tvDay;
        private TextView tvNight;
        private TextView tvWeek;
        private TextView tvType;

        public PriceViewHolder(View itemView) {
            super(itemView);
            tvEarly = itemView.findViewById(R.id.tvEarly);
            tvDay = itemView.findViewById(R.id.tvDay);
            tvNight = itemView.findViewById(R.id.tvNight);
            tvWeek = itemView.findViewById(R.id.tvWeek);
            tvType = itemView.findViewById(R.id.tvType);

        }
    }
}
