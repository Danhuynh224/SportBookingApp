package com.example.pjbookingsport.frag;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.activity.LoginActivity;
import com.example.pjbookingsport.adapter.FacilityPagerAdapter;
import com.example.pjbookingsport.model.SportFacility;
import com.example.pjbookingsport.sharedPreferences.SharedPreferencesHelper;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FacilityDetailFragment extends Fragment {
    private SportFacility facility;
    private ImageView imgFacility;
    private ImageButton btnBack;
    private TextView tvName, tvRating, tvTotalRating;
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
        tvRating = view.findViewById(R.id.tvRating);
        tvTotalRating = view.findViewById(R.id.tvTotalRating);
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
            if(SharedPreferencesHelper.checkUserIsSave(this.getContext())) {
                BookFragment bookFragment = BookFragment.newInstance(facility);

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragMain, bookFragment)
                        .addToBackStack(null)
                        .commit();
            }
            else {
                showLogoutDialog();
            }
        });
    }
    @SuppressLint("SetTextI18n")
    private void displayFacilityDetails(SportFacility facility) {
        tvName.setText(facility.getName());
        tvRating.setText(facility.getAverageRating() + "/5");
        tvTotalRating.setText("(" + facility.getReviewCount() + ")");

        Glide.with(this)
                .load(imgUrl + facility.getSportsFacilityId())
                .placeholder(R.drawable.ic_launcher_foreground)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgFacility);
    }
    private void showLogoutDialog() {
        Dialog dialog = new Dialog(requireContext(), R.style.CustomDialog);
        dialog.setContentView(R.layout.dialog_confirm_login);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0.5f);

        Window window = dialog.getWindow();
        if (window != null) {
            // Mở rộng chiều ngang của dialog (ví dụ 90% chiều rộng màn hình)
            window.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.9), WindowManager.LayoutParams.WRAP_CONTENT);
        }

        AppCompatButton btnHuy = dialog.findViewById(R.id.huyBtn);
        AppCompatButton btnLogout = dialog.findViewById(R.id.loginButton);

        btnHuy.setOnClickListener(v -> dialog.dismiss());

        btnLogout.setOnClickListener(v -> {
            SharedPreferencesHelper.clearAccount(requireContext());
            SharedPreferencesHelper.clearUser(requireContext());
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            dialog.dismiss();
        });
        dialog.show();
    }

}
