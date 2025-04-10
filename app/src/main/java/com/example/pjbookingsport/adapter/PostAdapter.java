package com.example.pjbookingsport.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.model.Post;
import com.example.pjbookingsport.model.SportFacility;

import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> postsList;
    private Context context;
    private OnItemClickListener listener;

    public PostAdapter(Context context, List<Post> postsList, OnItemClickListener listener) {
        this.postsList = postsList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_post, parent, false);
        return new PostViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postsList.get(position);
        holder.tvTitle.setText(post.getTitle());

        ImageSliderPostAdapter sliderAdapter = new ImageSliderPostAdapter(context, post.getImg());
        holder.viewPager.setAdapter(sliderAdapter);
        holder.indicator.setViewPager(holder.viewPager);
        sliderAdapter.registerAdapterDataObserver(holder.indicator.getAdapterDataObserver());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(post));
        sliderAdapter.setOnImageClickListener(() -> listener.onItemClick(post));

    }

    @Override
    public int getItemCount() {
        if(postsList != null) {
            return postsList.size();
        }
        return 0;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        Button bookButton;
        ViewPager2 viewPager;
        CircleIndicator3 indicator;

        public PostViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            viewPager = itemView.findViewById(R.id.viewPagerSlider);
            indicator = itemView.findViewById(R.id.indicator);
            bookButton = itemView.findViewById(R.id.btnDatLich);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(Post post);
        void onBookClick(Post post);

    }
}


