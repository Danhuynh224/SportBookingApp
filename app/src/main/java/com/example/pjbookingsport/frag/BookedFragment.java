package com.example.pjbookingsport.frag;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pjbookingsport.API.RetrofitClient;
import com.example.pjbookingsport.API.ServiceAPI;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.adapter.BookedAdapter;
import com.example.pjbookingsport.model.Booking;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookedFragment extends Fragment {

    private RecyclerView rvBooked;
    private BookedAdapter adapter;

    private List<Booking> bookingList = new ArrayList<>();
    private ServiceAPI apiService;

    public BookedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booked, container, false);
        rvBooked = view.findViewById(R.id.rv_bookedList);
        adapter = new BookedAdapter(bookingList, booking -> {
            BookedDetailFragment detailFragment = BookedDetailFragment.newInstance(booking);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });
        rvBooked.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvBooked.setLayoutManager(layoutManager);

        getBookingsByUserId(3L);

        return view;
    }

    private void getBookingsByUserId(Long userId) {
        apiService = RetrofitClient.getClient().create(ServiceAPI.class);
        apiService.getBookingsByUserId(userId).enqueue(new Callback<List<Booking>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bookingList.clear();
                    bookingList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("API ERROR", "Không thể lấy danh sách đã đặt");
                }
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                Log.e("API ERROR", "Lỗi khi gọi API lấy danh sách đặt: " + t.getMessage());
            }
        });
    }
}