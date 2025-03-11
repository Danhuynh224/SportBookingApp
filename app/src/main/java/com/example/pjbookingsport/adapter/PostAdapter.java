package com.example.pjbookingsport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pjbookingsport.R;
import com.example.pjbookingsport.model.PostModel;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private static List<PostModel> mPosts;
    private static Context mContext;
    private LayoutInflater mLayoutInflater;

    public PostAdapter(Context context, List<PostModel> posts) {
        mPosts = posts;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.row_item_post, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostModel post = mPosts.get(position);
        holder.tvTitle.setText(post.getTitle());
        holder.imgBackground.setImageResource(post.getImgBackground());
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgBackground;
        private TextView tvTitle;

        public PostViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            imgBackground = itemView.findViewById(R.id.imgBackground);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    PostModel post = mPosts.get(position);
                    Toast.makeText(mContext, post.getTitle(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}


