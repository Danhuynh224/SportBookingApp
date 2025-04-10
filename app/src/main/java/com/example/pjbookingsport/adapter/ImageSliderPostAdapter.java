package com.example.pjbookingsport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.model.Post;

import java.util.List;

public class ImageSliderPostAdapter extends RecyclerView.Adapter<ImageSliderPostAdapter.SliderViewHolder> {

    private List<String> imageUrls;
    private Context context;

    private OnImageClickListener listener;
    public ImageSliderPostAdapter(Context context, List<String> imageUrls) {
        this.imageUrls = imageUrls;
        this.context = context;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_slider_image_post, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderPostAdapter.SliderViewHolder holder, int position) {
        String url = imageUrls.get(position);
        Glide.with(context)
                .load(url)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onImageClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(imageUrls != null) {
            return imageUrls.size();
        }
        return 0;
    }

    public static class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public SliderViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    public interface OnImageClickListener {
        void onImageClick();
    }

    public void setOnImageClickListener(OnImageClickListener listener) {
        this.listener = listener;
    }
}
