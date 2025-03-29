package com.example.pjbookingsport.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.adapter.FacilityPagerAdapter;
import com.example.pjbookingsport.model.SportFacility;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FacilityDetailFragment extends Fragment {
    private SportFacility facility;
    private ImageView imgFacility;
    private ImageButton btnBack;
    private TextView tvName;
    private Button btnBook;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    String imgUrl;

    private static final String ARG_FACILITY = "FACILITY";

    public static FacilityDetailFragment newInstance(SportFacility facility) {
        FacilityDetailFragment fragment = new FacilityDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FACILITY, facility);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facility_detail, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgUrl = getString(R.string.img_url);
        imgFacility = view.findViewById(R.id.img_facility);
        tvName = view.findViewById(R.id.tv_name);
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        btnBack = view.findViewById(R.id.btn_back);
        btnBook = view.findViewById(R.id.btn_book);

        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        if (getArguments() != null) {
            facility = (SportFacility) getArguments().getSerializable(ARG_FACILITY);
            if (facility != null) {
                displayFacilityDetails(facility);

                // Setup ViewPager2 với FacilityPagerAdapter
                FacilityPagerAdapter adapter = new FacilityPagerAdapter(this, facility);
                viewPager.setAdapter(adapter);

                // Kết nối TabLayout với ViewPager2
                new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
                    if (position == 0) {
                        tab.setText("Thông tin");
                    } else if (position == 1) {
                        tab.setText("Bảng giá");
                    }
                }).attach();
            }
        }

        btnBook.setOnClickListener(v -> {
            BookFragment bookFragment = BookFragment.newInstance(facility);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragMain, bookFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
    private void displayFacilityDetails(SportFacility facility) {
        tvName.setText(facility.getName());

        Glide.with(this)
                .load(imgUrl + facility.getSportsFacilityId())
                .placeholder(R.drawable.ic_launcher_foreground)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgFacility);
    }

}
