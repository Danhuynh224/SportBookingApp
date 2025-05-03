package com.example.pjbookingsport.frag;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pjbookingsport.R;
import com.example.pjbookingsport.adapter.ReviewAdapter;
import com.example.pjbookingsport.model.Price;
import com.example.pjbookingsport.model.Review;
import com.example.pjbookingsport.model.SportFacility;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FacilityInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FacilityInfoFragment extends Fragment {
    private static final String ARG_FACILITY = "FACILITY";
    private SportFacility facility;
    private TextView tvAddress, tvTypes, tvDescription, tvNoReview;
    private RecyclerView rvReview;
    private ReviewAdapter reviewAdapter;

    public static FacilityInfoFragment newInstance(SportFacility facility) {
        FacilityInfoFragment fragment = new FacilityInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FACILITY, facility);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facility_info, container, false);
        tvDescription = view.findViewById(R.id.tv_description);
        tvAddress = view.findViewById(R.id.tv_address);
        tvTypes = view.findViewById(R.id.tv_types);
        rvReview = view.findViewById(R.id.rv_review);
        tvNoReview = view.findViewById(R.id.tvNoReview);

        if (getArguments() != null) {
            facility = (SportFacility) getArguments().getSerializable(ARG_FACILITY);
            if (facility != null) {
                tvDescription.setText(facility.getDetail());
                tvAddress.setText(facility.getAddress());

                tvAddress.setPaintFlags(tvAddress.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                tvAddress.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark));
                tvAddress.setClickable(true);


                List<Price> prices = facility.getPrices();
                List<String> types= new ArrayList<>();
                if (prices != null && !prices.isEmpty()) {
                    for(Price price : prices){
                        types.add(price.getFacilityType().getName());
                    }
                }
                tvTypes.setText(TextUtils.join(", ", types));

                List<Review> reviews = facility.getReviews();
                if (reviews != null && !reviews.isEmpty()) {
                    tvNoReview.setVisibility(View.GONE);
                    rvReview.setVisibility(View.VISIBLE);

                    rvReview.setLayoutManager(new LinearLayoutManager(getContext()));
                    reviewAdapter = new ReviewAdapter(reviews);
                    rvReview.setAdapter(reviewAdapter);
                } else {
                    tvNoReview.setVisibility(View.VISIBLE);
                    rvReview.setVisibility(View.GONE);
                }
            }
        }

        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat = facility.getLatitude();
                double lng = facility.getLongitude();
                String label = facility.getName();

                // Mở Google Maps với vị trí cụ thể
                String uri = "geo:" + lat + "," + lng + "?q=" + lat + "," + lng + "(" + Uri.encode(label) + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps"); // mở bằng Google Maps
                if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        return view;
    }
}