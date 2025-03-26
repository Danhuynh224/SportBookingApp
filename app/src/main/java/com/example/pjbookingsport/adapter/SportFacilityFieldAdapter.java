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
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.model.SportFacility;

import java.util.List;

public class SportFacilityFieldAdapter extends RecyclerView.Adapter<SportFacilityFieldAdapter.ViewHolder> {
    private Context context;
    private List<SportFacility> sportFacilityList;
    private String imgUrl;
    private OnItemClickListener listener;

    public SportFacilityFieldAdapter(Context context, List<SportFacility> sportFacilityList, String imgUrl, OnItemClickListener listener) {
        this.context = context;
        this.sportFacilityList = sportFacilityList;
        this.imgUrl = imgUrl;
        this.listener = listener;
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
        SportFacility sportFacility = sportFacilityList.get(position);
        holder.title.setText(sportFacility.getName());
        holder.address.setText(sportFacility.getAddress());

        Glide.with(context)
                .load(imgUrl + sportFacility.getSportsFacilityId())
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(sportFacility);
            }
        });

    }

    @Override
    public int getItemCount() {
        return sportFacilityList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, address;
        Button bookButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            address = itemView.findViewById(R.id.address);
            bookButton = itemView.findViewById(R.id.book_button);
        }
    }

    public void updateList(List<SportFacility> newList) {
        DiffUtil.Callback diffCallback = new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return sportFacilityList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return sportFacilityList.get(oldItemPosition).getSportsFacilityId() ==
                        newList.get(newItemPosition).getSportsFacilityId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return sportFacilityList.get(oldItemPosition).equals(newList.get(newItemPosition));
            }
        };

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        sportFacilityList.clear();
        sportFacilityList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    public interface OnItemClickListener {
        void onItemClick(SportFacility facility);
    }
}
