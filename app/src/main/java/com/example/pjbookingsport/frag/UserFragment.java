package com.example.pjbookingsport.frag;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pjbookingsport.R;
import com.example.pjbookingsport.activity.LoginActivity;
import com.example.pjbookingsport.activity.MainActivity;
import com.example.pjbookingsport.model.User;
import com.example.pjbookingsport.sharedPreferences.SharedPreferencesHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class UserFragment extends Fragment {

    private TextView nameUser;
    private ImageButton btnBack;
    private ImageView avaUser;
    private ConstraintLayout policy, aboutUs, logOut;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameUser = view.findViewById(R.id.name_user);
        btnBack = view.findViewById(R.id.btn_back);
        avaUser = view.findViewById(R.id.imageAva);
        policy = view.findViewById(R.id.policy);
        aboutUs = view.findViewById(R.id.aboutUs);
        logOut = view.findViewById(R.id.logOut);

        User user = SharedPreferencesHelper.getUser(requireContext());

        assert user != null;
        nameUser.setText(user.getFullName());

        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        View.OnClickListener goToPersonalFragment = v -> {
            ((MainActivity) requireActivity()).binding.bottomNavigationView.setSelectedItemId(R.id.nav_user);
        };

        avaUser.setOnClickListener(goToPersonalFragment);
        nameUser.setOnClickListener(goToPersonalFragment);

        aboutUs.setOnClickListener(v -> {
            AboutUsFragment aboutUsFragment = new AboutUsFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, aboutUsFragment)
                    .addToBackStack(null)
                    .commit();
        });

        policy.setOnClickListener(v -> {
            PolicyFragment policyFragment = new PolicyFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, policyFragment)
                    .addToBackStack(null)
                    .commit();
        });

        logOut.setOnClickListener(v -> {
            showLogoutDialog();
        });
    }

    private void showLogoutDialog() {
        Dialog dialog = new Dialog(requireContext(), R.style.CustomDialog);
        dialog.setContentView(R.layout.dialog_confirm_logout);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0.5f);

        Window window = dialog.getWindow();
        if (window != null) {
            // Mở rộng chiều ngang của dialog (ví dụ 90% chiều rộng màn hình)
            window.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.9), WindowManager.LayoutParams.WRAP_CONTENT);
        }

        AppCompatButton btnHuy = dialog.findViewById(R.id.huyBtn);
        AppCompatButton btnLogout = dialog.findViewById(R.id.logOutBtn);

        btnHuy.setOnClickListener(v -> dialog.dismiss());

        btnLogout.setOnClickListener(v -> {
            User user = SharedPreferencesHelper.getUser(this.getContext());
            if(!user.isSave()) {
                SharedPreferencesHelper.clearAccount(requireContext());
                SharedPreferencesHelper.clearUser(requireContext());
                SharedPreferencesHelper.clearJWT(requireContext());
            }
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            dialog.dismiss();

        });
        dialog.show();
    }
}