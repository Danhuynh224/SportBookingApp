package com.example.pjbookingsport.frag;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.activity.LoginActivity;
import com.example.pjbookingsport.adapter.FacilityPagerAdapter;
import com.example.pjbookingsport.adapter.ImageSliderPostAdapter;
import com.example.pjbookingsport.model.Post;
import com.example.pjbookingsport.model.SportFacility;
import com.example.pjbookingsport.sharedPreferences.SharedPreferencesHelper;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import me.relex.circleindicator.CircleIndicator3;

public class PostDetailFragment extends Fragment {
    private Post post;
    private ImageButton btnBack;
    private TextView tvTitle, tvSummary, tvContent;
    private Button btnBook;
    private ViewPager2 viewPager;

    private CircleIndicator3 indicator3;

    private static final String ARG_POST = "POST";

    public PostDetailFragment() {
        // Required empty public constructor
    }

    public static PostDetailFragment newInstance(Post post) {
        PostDetailFragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_POST, post);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle = view.findViewById(R.id.title);
        tvSummary = view.findViewById(R.id.summary);
        tvContent = view.findViewById(R.id.content);
        viewPager = view.findViewById(R.id.viewPagerSlider);
        indicator3 = view.findViewById(R.id.indicator);
        btnBack = view.findViewById(R.id.btn_back);
        btnBook = view.findViewById(R.id.btn_book);

        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        if (getArguments() != null) {
            post = (Post) getArguments().getSerializable(ARG_POST);
            if (post != null) {
                displayPostDetails(post);

            }
        }
        btnBook.setOnClickListener(v -> {
            if(SharedPreferencesHelper.checkUserIsSave(this.getContext())) {
                BookFragment bookFragment = BookFragment.newInstance(post.getSportFacility());

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

    private void displayPostDetails(Post post) {
        tvTitle.setText(post.getTitle());
        tvSummary.setText(post.getSummary());
        tvContent.setText(post.getContent());

        ImageSliderPostAdapter sliderAdapter = new ImageSliderPostAdapter(requireContext(), post.getImg(getContext()));
        viewPager.setAdapter(sliderAdapter);

        indicator3.setViewPager(viewPager);
        sliderAdapter.registerAdapterDataObserver(indicator3.getAdapterDataObserver());

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