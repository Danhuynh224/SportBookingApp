package com.example.pjbookingsport.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    String imgUrl ;
    private List<SportFacility> mListFacility;
    private HomeFragment context;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    private OnItemClickListener listener;

    public PhotoAdapter(HomeFragment context, List<SportFacility> mListPhoto, OnItemClickListener listener) {
        this.mListFacility = mListPhoto;
        this.context = context;
        this.imgUrl = context.getString(R.string.img_url);
        this.listener = listener;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent,false);
        return  new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        SportFacility sportFacility = mListFacility.get(position);
        if(sportFacility == null){
            return;
        }
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
        Glide.with(holder.itemView.getContext())
                .load(imgUrl+sportFacility.getSportsFacilityId())
                .placeholder(R.drawable.ic_launcher_foreground) // Hình ảnh mặc định nếu không có ảnh
                .into(holder.imgPhoto);
        holder.txtName.setText(sportFacility.getName());
        holder.txtAddress.setText(sportFacility.getAddress());
    }

    @Override
    public int getItemCount() {
        return mListFacility != null ? mListFacility.size() : 0;
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgPhoto;
        private TextView txtName;
        private TextView txtAddress;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto=itemView.findViewById(R.id.img_photo);
            txtName=itemView.findViewById(R.id.name);
            txtAddress= itemView.findViewById(R.id.address);
        }
    }


}
