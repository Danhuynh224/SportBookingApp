package com.example.pjbookingsport.frag;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pjbookingsport.API.RetrofitClient;
import com.example.pjbookingsport.API.ServiceAPI;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.adapter.PostAdapter;
import com.example.pjbookingsport.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostFragment extends Fragment {

    private RecyclerView rvPosts;
    private PostAdapter postAdapter;
    private List<Post> postList = new ArrayList<>();;

    private ServiceAPI apiService;

    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        rvPosts = view.findViewById(R.id.rv_postList);
//        mPosts.add(new Post(R.drawable.pickleball_lon_nhat, "Giải pickleball lớn nhất thế giới tại Việt Nam"));
//        mPosts.add(new Post(R.drawable.pickleball_lon_nhat, "Giải pickleball lớn nhất thế giới tại Việt Nam"));
//        mPosts.add(new Post(R.drawable.pickleball_lon_nhat, "Giải pickleball lớn nhất thế giới tại Việt Nam"));

        String imgUrl = getString(R.string.img_url_post);
        postAdapter = new PostAdapter(getContext(), postList, imgUrl);
        rvPosts.setAdapter(postAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        rvPosts.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvPosts.getContext(),
                linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getContext(), R.drawable.custom_divider)));
        rvPosts.addItemDecoration(dividerItemDecoration);

        GetAllPosts();

        return view;
    }

    void GetAllPosts() {
        apiService = RetrofitClient.getClient().create(ServiceAPI.class);
        apiService.getAllPosts().enqueue(new Callback<List<Post>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    postList.clear();
                    postList.addAll(response.body());
                    postAdapter.notifyDataSetChanged();
                } else {
                    Log.e("API ERROR", "Không thể lấy danh sách các bài Post");
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("API ERROR", "Lỗi khi gọi API bài Post: " + t.getMessage());
            }
        });
    }


}