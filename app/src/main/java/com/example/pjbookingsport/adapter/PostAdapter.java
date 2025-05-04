package com.example.pjbookingsport.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.activity.LoginActivity;
import com.example.pjbookingsport.model.Post;
import com.example.pjbookingsport.model.SportFacility;
import com.example.pjbookingsport.sharedPreferences.SharedPreferencesHelper;

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

        ImageSliderPostAdapter sliderAdapter = new ImageSliderPostAdapter(context, post.getImg(context));
        holder.viewPager.setAdapter(sliderAdapter);
        holder.indicator.setViewPager(holder.viewPager);
        sliderAdapter.registerAdapterDataObserver(holder.indicator.getAdapterDataObserver());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(post));
        sliderAdapter.setOnImageClickListener(() -> listener.onItemClick(post));
        holder.bookButton.setOnClickListener(v -> {
            if(SharedPreferencesHelper.checkUserIsSave(holder.itemView.getContext())) {
                if (listener != null) {
                    listener.onBookClick(post.getSportFacility());
                }
            }
            else {
                showLogoutDialog();
            }
        });

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
        void onBookClick(SportFacility sportFacility);

    }
    public void showLogoutDialog() {
        android.app.Dialog dialog = new android.app.Dialog(context, R.style.CustomDialog);
        dialog.setContentView(R.layout.dialog_confirm_login);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0.5f);

        android.view.Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout((int) (context.getResources().getDisplayMetrics().widthPixels * 0.9),
                    android.view.WindowManager.LayoutParams.WRAP_CONTENT);
        }

        androidx.appcompat.widget.AppCompatButton btnHuy = dialog.findViewById(R.id.huyBtn);
        androidx.appcompat.widget.AppCompatButton btnLogout = dialog.findViewById(R.id.loginButton);

        btnHuy.setOnClickListener(v -> dialog.dismiss());

        btnLogout.setOnClickListener(v -> {
            com.example.pjbookingsport.sharedPreferences.SharedPreferencesHelper.clearAccount(context);
            com.example.pjbookingsport.sharedPreferences.SharedPreferencesHelper.clearUser(context);

            android.content.Intent intent = new android.content.Intent(context, com.example.pjbookingsport.activity.LoginActivity.class);
            intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);

            dialog.dismiss();
        });

        dialog.show();
    }
}


