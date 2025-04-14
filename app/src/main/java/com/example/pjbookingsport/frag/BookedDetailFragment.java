package com.example.pjbookingsport.frag;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pjbookingsport.R;
import com.example.pjbookingsport.adapter.BookedAdapter;
import com.example.pjbookingsport.adapter.ImageSliderPostAdapter;
import com.example.pjbookingsport.model.Booking;
import com.example.pjbookingsport.model.Post;

public class BookedDetailFragment extends Fragment {

    private Booking booking;
    private ImageButton btnBack;

    private TextView tvTenSan, tvDiaChi, tvNgayGio, tvTongTien;

    private LinearLayout layoutTimeSlots;
    private static final String ARG_BOOKING = "BOOKING";


    public BookedDetailFragment() {
        // Required empty public constructor
    }

    public static BookedDetailFragment newInstance(Booking booking) {
        BookedDetailFragment fragment = new BookedDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BOOKING, booking);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTenSan = view.findViewById(R.id.tvTenSan);
        tvDiaChi = view.findViewById(R.id.tvDiaChi);
        tvNgayGio = view.findViewById(R.id.tvNgayGio);
        tvTongTien = view.findViewById(R.id.tvTongTien);
        layoutTimeSlots = view.findViewById(R.id.layoutTimeSlots);
        btnBack = view.findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        if (getArguments() != null) {
            booking = (Booking) getArguments().getSerializable(ARG_BOOKING);
            if (booking != null) {
                displayBookingDetails(booking);

            }
        }
    }

    private void displayBookingDetails(Booking booking) {


    }
}