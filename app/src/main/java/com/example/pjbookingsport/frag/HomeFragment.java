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
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pjbookingsport.API.RetrofitClient;
import com.example.pjbookingsport.API.ServiceAPI;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.activity.LoginActivity;
import com.example.pjbookingsport.adapter.PhotoAdapter;
import com.example.pjbookingsport.model.LocationViewModel;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements OnMapReadyCallback {


    public HomeFragment() {
        // Required empty public constructor
    }

    private GoogleMap mMap;
    private ViewPager2 viewPager2;
    private ImageButton btnSearchIcon, btnUser;
    private EditText searchBar;
    private ProgressBar searchProgressBar;

    private int vitri = 0;
    private ServiceAPI apiService;
    private List<SportFacility> sportFacilities;
    private List<SportFacility> filteredFacilities;
    private PhotoAdapter photoAdapter;
    private String imgUrl;
    private LocationViewModel locationViewModel;
    private Location currentLocation;

    private Handler searchHandler;
    private static final long SEARCH_DELAY_MS = 300;
    private Call<List<SportFacility>> currentSearchCall;


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgUrl = getString(R.string.img_url);
        searchHandler = new Handler(Looper.getMainLooper());
        apiService = RetrofitClient.getClient().create(ServiceAPI.class);
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
        searchProgressBar = view.findViewById(R.id.searchProgressBar);

        if (searchProgressBar == null) {
            // If searchProgressBar doesn't exist in layout, log a warning
            Log.w("HomeFragment", "searchProgressBar not found in layout");
        } else {
            searchProgressBar.setVisibility(View.GONE);
        }

        // Load animation
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);

        searchBar.setVisibility(View.GONE);
        btnSearchIcon.setVisibility(View.VISIBLE);

        setupViewPager();

        // Sự kiện nhấn vào icon tìm kiếm
        btnSearchIcon.setOnClickListener(v -> {
            btnSearchIcon.startAnimation(fadeOut);
            btnSearchIcon.setVisibility(View.GONE);

            searchBar.setVisibility(View.VISIBLE);
            searchBar.startAnimation(fadeIn);
            searchBar.requestFocus();
        });

        setupSearchBarInteractions(fadeIn, fadeOut);

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


        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        locationViewModel.getLocation().observe(getViewLifecycleOwner(), location -> {
            if (location != null) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                currentLocation = location;
                Log.d("HomeFragment", "Received location: " + lat + ", " + lng);
                getNearbyFacilities(lat, lng); // API gọi danh sách sân gần
            }
        });
    }


    private void setupViewPager() {
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                vitri = position;
                LoadingMap(vitri);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupSearchBarInteractions(Animation fadeIn, Animation fadeOut) {
        // Clear button (X) handling
        searchBar.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (searchBar.getRight() - searchBar.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // Hide EditText, show search icon again
                    searchBar.startAnimation(fadeOut);
                    searchBar.setVisibility(View.GONE);
                    searchBar.setText("");
                    btnSearchIcon.setVisibility(View.VISIBLE);
                    btnSearchIcon.startAnimation(fadeIn);

                    // Cancel any ongoing search
                    cancelOngoingSearch();

                    // Restore original facilities list
                    if (sportFacilities != null) {
                        updateFacilitiesList(sportFacilities);
                    }
                    return true;
                }
            }
            return false;
        });

        // Text change listener for search
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Reset search handler to avoid calling API continuously
                searchHandler.removeCallbacksAndMessages(null);

                // Cancel any ongoing search request
                cancelOngoingSearch();

                // Add delay to search
                searchHandler.postDelayed(() -> {
                    String query = s.toString().trim();
                    if (query.isEmpty()) {
                        // If empty, restore original list
                        if (searchProgressBar != null) {
                            searchProgressBar.setVisibility(View.GONE);
                        }
                        if (sportFacilities != null) {
                            updateFacilitiesList(sportFacilities);
                        }
                    } else {
                        // Perform search
                        if (searchProgressBar != null) {
                            searchProgressBar.setVisibility(View.VISIBLE);
                        }
                        performSearch(query);
                    }
                }, SEARCH_DELAY_MS);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });

        // Handle search action
        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String query = searchBar.getText().toString().trim();
                if (!query.isEmpty()) {
                    if (searchProgressBar != null) {
                        searchProgressBar.setVisibility(View.VISIBLE);
                    }
                    performSearch(query);
                }
                return true;
            }
            return false;
        });
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
                    updateFacilitiesList(sportFacilities);
                } else {
                    Log.d("API ERROR", "Không thể lấy danh sách sân gần bạn");
                    Toast.makeText(getContext(), "Không thể tải danh sách sân gần bạn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SportFacility>> call, Throwable t) {
                Log.d("API ERROR", "Không thể lấy danh sách sân gần bạn: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFacilitiesList(List<SportFacility> facilities) {
        if (facilities == null || facilities.isEmpty()) {
            // Handle empty results
            Log.d("HomeFragment", "No facilities to display");
            if (photoAdapter != null) {
                filteredFacilities = new ArrayList<>();
                photoAdapter.updateData(filteredFacilities);
                viewPager2.setAdapter(photoAdapter);
            }
            return;
        }

        filteredFacilities = facilities;

        if (!filteredFacilities.isEmpty() && mMap != null) {
            LoadingMap(0);
        }

        if (photoAdapter == null) {
            // Create adapter if it doesn't exist
            photoAdapter = new PhotoAdapter(HomeFragment.this, filteredFacilities, new PhotoAdapter.OnItemClickListener() {
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
            // Update existing adapter
            photoAdapter.updateData(filteredFacilities);
        }

        vitri = 0;
        viewPager2.setCurrentItem(0, false);
    }

    private void performSearch(String query) {
        if (query.isEmpty()) {
            return;
        }

        // Cancel any ongoing search
        cancelOngoingSearch();

        // Execute new search
        currentSearchCall = apiService.getSportsFacilities(query);
        currentSearchCall.enqueue(new Callback<List<SportFacility>>() {
            @Override
            public void onResponse(Call<List<SportFacility>> call, Response<List<SportFacility>> response) {
                if (searchProgressBar != null) {
                    searchProgressBar.setVisibility(View.GONE);
                }

                if (call.isCanceled()) {
                    return;
                }

                if (response.isSuccessful()) {
                    List<SportFacility> searchResults = response.body();
                    if (searchResults != null && !searchResults.isEmpty()) {
                        updateFacilitiesList(searchResults);
                    } else {
                        // Handle empty results
                        Toast.makeText(getContext(), "Không tìm thấy kết quả nào cho: " + query, Toast.LENGTH_SHORT).show();
                        updateFacilitiesList(new ArrayList<>());
                    }
                } else {
                    // Handle error response
                    Toast.makeText(getContext(), "Lỗi tìm kiếm: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("Search API", "Error code: " + response.code());
                }

                currentSearchCall = null;
            }

            @Override
            public void onFailure(Call<List<SportFacility>> call, Throwable t) {
                if (searchProgressBar != null) {
                    searchProgressBar.setVisibility(View.GONE);
                }

                if (!call.isCanceled()) {
                    Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Search API", "Failure: " + t.getMessage());
                }

                currentSearchCall = null;
            }
        });
    }

    private void cancelOngoingSearch() {
        if (currentSearchCall != null && !currentSearchCall.isCanceled()) {
            currentSearchCall.cancel();
            currentSearchCall = null;
        }
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
        searchHandler.removeCallbacksAndMessages(null);
        cancelOngoingSearch();
        super.onDestroyView();
    }


}
