package com.example.pjbookingsport.frag;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pjbookingsport.R;
import com.example.pjbookingsport.model.SportFacility;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FacilityPriceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FacilityPriceFragment extends Fragment {

    private static final String ARG_FACILITY = "FACILITY";
    private SportFacility facility;
    private TextView tvPriceList;
    public static FacilityPriceFragment newInstance(SportFacility facility) {
        FacilityPriceFragment fragment = new FacilityPriceFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FACILITY, facility);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facility_price, container, false);
//        tvPriceList = view.findViewById(R.id.tv_price_list);
//
//        if (getArguments() != null) {
//            facility = (SportFacility) getArguments().getSerializable(ARG_FACILITY);
//            if (facility != null) {
//                tvPriceList.setText(facility.getPriceList());
//            }
//        }

        return view;
    }
}