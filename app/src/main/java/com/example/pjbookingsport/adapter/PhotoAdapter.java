package com.example.pjbookingsport.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.frag.HomeFragment;
import com.example.pjbookingsport.model.SportFacility;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private String imgUrl;
    private List<SportFacility> mListFacility;
    private HomeFragment context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SportFacility facility);
        void onBookClick(SportFacility facility);
    }

    public PhotoAdapter(HomeFragment context, List<SportFacility> mListPhoto, OnItemClickListener listener) {
        this.mListFacility = mListPhoto;
        this.context = context;
        this.imgUrl = context.getString(R.string.img_url);
        this.listener = listener;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        SportFacility sportFacility = mListFacility.get(position);
        if (sportFacility == null) {
            return;
        }

        holder.bind(sportFacility, listener, imgUrl);
    }

    @Override
    public int getItemCount() {
        return mListFacility != null ? mListFacility.size() : 0;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPhoto;
        private TextView txtName;
        private TextView txtAddress;
        private Button btnBook;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_photo);
            txtName = itemView.findViewById(R.id.name);
            txtAddress = itemView.findViewById(R.id.address);
            btnBook = itemView.findViewById(R.id.book_button);
        }

        public void bind(SportFacility facility, OnItemClickListener listener, String imgUrl) {
            txtName.setText(facility.getName());
            txtAddress.setText(facility.getAddress());

            Glide.with(itemView.getContext())
                    .load(imgUrl + facility.getSportsFacilityId())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(imgPhoto);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(facility);
                }
            });

            btnBook.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBookClick(facility);
                }
            });
        }
    }
}
