package com.example.pjbookingsport.activity;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.example.pjbookingsport.R;


import com.example.pjbookingsport.databinding.ActivityMainBinding;
import com.example.pjbookingsport.frag.*;
import com.example.pjbookingsport.model.User;
import com.example.pjbookingsport.sharedPreferences.SharedPreferencesHelper;


public class MainActivity extends AppCompatActivity {


    public ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inflate layout bằng ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Trả về View gốc

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        User user = SharedPreferencesHelper.getUser(this);

        // Load Fragment mặc định
        loadFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragmentSelected = null;
            int idItem= item.getItemId();
            if(idItem==R.id.nav_home) {
                fragmentSelected = new HomeFragment();
                loadFragment(fragmentSelected);
//                fragmentSelected =new BookFragment();
            }
            else if (idItem==R.id.nav_list) {
                fragmentSelected =new ListFragment();
                loadFragment(fragmentSelected);
            }
            else if (idItem==R.id.nav_booked) {
                if(SharedPreferencesHelper.checkUserIsSave(this)) {
                    fragmentSelected = new BookedFragment();
                    loadFragment(fragmentSelected);
                }
                else {
                    showLogoutDialog();
                }
            }
            else if (idItem==R.id.nav_post)  {
                fragmentSelected =new PostFragment();
                loadFragment(fragmentSelected);
            }
            else if (idItem==R.id.nav_user){
                if(SharedPreferencesHelper.checkUserIsSave(this)) {
                    fragmentSelected = new PersonalFragment();
                    loadFragment(fragmentSelected);
                }
                else {
                    showLogoutDialog();
                }
            }

            return true;
        });
    }

//    public void loadHomeFragment() {
//        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragMain);
//
//        if (!(currentFragment instanceof HomeFragment)) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragMain, new HomeFragment())
//                    .commit();
//        }
//
//        // Cập nhật trạng thái của BottomNavigationView
//        binding.bottomNavigationView.setSelectedItemId(R.id.nav_home);
//    }

    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragMain, fragment)
                .commit();
    }
    public void showLogoutDialog() {
        Dialog dialog = new Dialog(this, R.style.CustomDialog);
        dialog.setContentView(R.layout.dialog_confirm_login);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0.5f);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.9), WindowManager.LayoutParams.WRAP_CONTENT);
        }

        AppCompatButton btnHuy = dialog.findViewById(R.id.huyBtn);
        AppCompatButton btnLogout = dialog.findViewById(R.id.loginButton);

        btnHuy.setOnClickListener(v -> dialog.dismiss());

        btnLogout.setOnClickListener(v -> {
            SharedPreferencesHelper.clearAccount(this);
            SharedPreferencesHelper.clearUser(this);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            dialog.dismiss();
        });

        dialog.show();
    }

}