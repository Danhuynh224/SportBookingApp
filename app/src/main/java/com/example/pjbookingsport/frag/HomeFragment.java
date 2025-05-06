package com.example.pjbookingsport.frag;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.pjbookingsport.API.RetrofitClient;
import com.example.pjbookingsport.API.ServiceAPI;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.activity.LoginActivity;
import com.example.pjbookingsport.adapter.PhotoAdapter;
import com.example.pjbookingsport.model.SportFacility;
import com.example.pjbookingsport.sharedPreferences.SharedPreferencesHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }
    private GoogleMap mMap;
    private ViewPager2 viewPager2;
    private int vitri=0;
    private ServiceAPI apiService;
    private List<SportFacility> sportFacilities;
    String imgUrl;
    private ImageButton btnSearchIcon, btnUser;
    private EditText searchBar;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    private Location currentLocation;

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        imgUrl = getString(R.string.img_url);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Khởi tạo Fragment bản đồ
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Ánh xạ view

        viewPager2 = view.findViewById(R.id.view_pager_2);
        btnSearchIcon = view.findViewById(R.id.btnSearchIcon);
        searchBar = view.findViewById(R.id.search_bar);
        btnUser = view.findViewById(R.id.btnUser);

        // Load animation
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);

        searchBar.setVisibility(View.GONE);
        btnSearchIcon.setVisibility(View.VISIBLE);

        //Setting viewpager2
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r =1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                vitri = position;  // Cập nhật vị trí mới khi kéo
                LoadingMap(vitri); // Load lại map tương ứng với vị trí mới
            }
        });


        // Sự kiện nhấn vào icon tìm kiếm
        btnSearchIcon.setOnClickListener(v -> {
            btnSearchIcon.startAnimation(fadeOut);
            btnSearchIcon.setVisibility(View.GONE);

            searchBar.setVisibility(View.VISIBLE);
            searchBar.startAnimation(fadeIn);
            searchBar.requestFocus();
        });

        searchBar.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (searchBar.getRight() - searchBar.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // Ẩn EditText, hiện lại icon search

                    searchBar.startAnimation(fadeOut);
                    searchBar.setVisibility(View.GONE);
                    searchBar.setText("");
                    btnSearchIcon.setVisibility(View.VISIBLE);
                    btnSearchIcon.startAnimation(fadeIn);
                    return true;
                }
            }
            return false;
        });

        // Xu ly su kien nut User
        btnUser.setOnClickListener(v -> {
            if(SharedPreferencesHelper.checkUserIsSave(this.getContext()))
            {
                UserFragment userFragment = new UserFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, userFragment)
                        .addToBackStack(null)
                        .commit();
            }
            else {
                showLogoutDialog();
            }

        });


        // Khởi tạo fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

// Tạo yêu cầu vị trí
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(5000);

// Tạo callback để xử lý khi có vị trí mới
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;

                Location location = locationResult.getLastLocation();
                if (location != null) {
                    currentLocation = location;
                    Log.d("DEBUG", "Location from update: " + location.getLatitude() + ", " + location.getLongitude());

                    // GỌI API LẤY SÂN GẦN nếu chưa gọi
                    getNearbyFacilities(location.getLatitude(), location.getLongitude());
                }
            }
        };

// Yêu cầu cập nhật vị trí
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style));
        // Kiểm tra sportFacilities đã có dữ liệu chưa
        if (sportFacilities != null && !sportFacilities.isEmpty()) {
            LoadingMap(0);
        }
    }

    @SuppressLint("DefaultLocale")
    private void LoadingMap(int vitri) {
        SportFacility sportFacility = sportFacilities.get(vitri);
        LatLng location = new LatLng(sportFacility.getLatitude(), sportFacility.getLongitude());
        mMap.clear();

        // Lấy tọa độ của người dùng
        double userLatitude = currentLocation.getLatitude();
        double userLongitude = currentLocation.getLongitude();

        Log.d("Loadmap", "userLatitude: " + userLatitude);
        Log.d("Loadmap", "userLongitude: " + userLongitude);

        // Tính khoảng cách
        double distance = calculateDistance(userLatitude, userLongitude, sportFacility.getLatitude(), sportFacility.getLongitude());

        mMap.addMarker(new MarkerOptions()
                .position(location)
                .title(sportFacility.getName())
                .snippet("Khoảng cách: " + String.format("%.2f", distance) + " km"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15), 500, null);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Bán kính trái đất (đơn vị km)

        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // Khoảng cách (km)
    }

    private void getNearbyFacilities(double lat, double lng) {
        apiService = RetrofitClient.getClient().create(ServiceAPI.class);
        apiService.getSportFacilityNearMe(lat, lng).enqueue(new Callback<List<SportFacility>>() {
            @Override
            public void onResponse(Call<List<SportFacility>> call, Response<List<SportFacility>> response) {
                if (response.isSuccessful()) {
                    sportFacilities = response.body();
                    if (!sportFacilities.isEmpty()) {
                        LoadingMap(0);
                    }
                    PhotoAdapter photoAdapter = new PhotoAdapter(HomeFragment.this, sportFacilities, new PhotoAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(SportFacility facility) {
                            FacilityDetailFragment detailFragment = FacilityDetailFragment.newInstance(facility);

                            requireActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragMain, detailFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }

                        @Override
                        public void onBookClick(SportFacility facility) {
                            BookFragment bookFragment = BookFragment.newInstance(facility);

                            requireActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragMain, bookFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    });
                    viewPager2.setAdapter(photoAdapter);
                } else {
                    Log.d("API ERROR", "Không thể lấy danh sách sân gần bạn");
                }
            }

            @Override
            public void onFailure(Call<List<SportFacility>> call, Throwable t) {
                Log.d("API ERROR", "Không thể lấy danh sách sân gần bạn: " + t.getMessage());
            }
        });
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }


}
