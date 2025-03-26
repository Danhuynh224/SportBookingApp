package com.example.pjbookingsport.frag;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pjbookingsport.R;
import com.example.pjbookingsport.adapter.PriceAdapter;
import com.example.pjbookingsport.model.SportFacility;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FacilityPriceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FacilityPriceFragment extends Fragment {

    private static final String ARG_FACILITY = "FACILITY";
    private SportFacility facility;
    private RecyclerView recyclerView;
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
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        recyclerView = view.findViewById(R.id.rv_priceList);
        if (getArguments() != null) {
            facility = (SportFacility) getArguments().getSerializable(ARG_FACILITY);
            if (facility != null && facility.getPrices() != null) {
                Log.d("FacilityPriceFragment", "Price list size: " + facility.getPrices().size());
            }

        }
        assert facility != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        PriceAdapter priceAdapter = new PriceAdapter(getContext(), facility.getPrices());
        recyclerView.setAdapter(priceAdapter);

    }
}