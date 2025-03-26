package com.example.pjbookingsport.frag;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pjbookingsport.R;
import com.example.pjbookingsport.model.Price;
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
    private TextView tvAddress, tvTypes, tvDescription;
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

        if (getArguments() != null) {
            facility = (SportFacility) getArguments().getSerializable(ARG_FACILITY);
            if (facility != null) {
                tvDescription.setText(facility.getDetail());
                tvAddress.setText(facility.getAddress());

                List<Price> prices = facility.getPrices();
                List<String> types= new ArrayList<>();
                if (prices != null && !prices.isEmpty()) {
                    for(Price price : prices){
                        types.add(price.getFacilityType().getName());
                    }
                }
                tvTypes.setText(TextUtils.join(", ", types));
            }
        }

        return view;
    }
}