package com.example.pjbookingsport.adapter;

import android.content.Context;
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
import com.example.pjbookingsport.model.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> postsList;
    private Context context;

    private String imgUrl;

    public PostAdapter(Context context, List<Post> postsList, String imgUrl) {
        this.postsList = postsList;
        this.context = context;
        this.imgUrl = imgUrl;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postsList.get(position);
        holder.tvTitle.setText(post.getTitle());

        Glide.with(context)
                .load(imgUrl + post.getPostId())
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imgBackground);
    }

    @Override
    public int getItemCount() {
        if(postsList != null) {
            return postsList.size();
        }
        return 0;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBackground;
        TextView tvTitle;
        Button bookButton;

        public PostViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            imgBackground = itemView.findViewById(R.id.imgBackground);
            bookButton = itemView.findViewById(R.id.btnDatLich);

        }
    }
}


