package com.example.pjbookingsport;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.example.pjbookingsport.R;


import com.example.pjbookingsport.databinding.ActivityMainBinding;
import com.example.pjbookingsport.frag.*;


public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
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

        // Load Fragment mặc định
        loadFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragmentSelected = null;
            int idItem= item.getItemId();
            if(idItem==R.id.nav_home)
                fragmentSelected =new HomeFragment();
            else if (idItem==R.id.nav_list) {
                fragmentSelected =new ListFragment();
            }
            else if (idItem==R.id.nav_booked) {
                fragmentSelected =new BookedFragment();
            }
            else {
                fragmentSelected =new PostFragment();
            }
            loadFragment(fragmentSelected);
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragMain, fragment)
                .commit();
    }
}