package com.example.pjbookingsport.frag;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pjbookingsport.API.RetrofitClient;
import com.example.pjbookingsport.API.ServiceAPI;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.adapter.PhotoAdapter;
import com.example.pjbookingsport.model.Field;
import com.example.pjbookingsport.model.SportFacility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
    private ImageButton btnLeft;
    private ImageButton btnRight;
    private ViewPager2 viewPager2;
    private ImageView imgMain;
    private TextView txtName;
    private int vitri=0;
    private ServiceAPI apiService;
    private List<SportFacility> sportFacilities;
    String imgUrl;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Ánh xạ view
//        btnLeft = view.findViewById(R.id.btnLeft);
//        btnRight = view.findViewById(R.id.btnRight);
//        imgMain = view.findViewById(R.id.imgMain);
        viewPager2 = view.findViewById(R.id.view_pager_2);
//        txtName = view.findViewById(R.id.txtName);

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

        GetAllSportFa();

//        PhotoAdapter photoAdapter = new PhotoAdapter(this, sportFacilities);
//        viewPager2.setAdapter(photoAdapter);
//        btnRight.setOnClickListener(v -> {
//            vitri = (vitri + 1) % sportFacilities.size();
//            LoadingMap(vitri);
//        });
//
//        btnLeft.setOnClickListener(v -> {
//            vitri = (vitri == 0) ? sportFacilities.size() - 1 : vitri - 1;
//            LoadingMap(vitri);
//        });
    }

    private void GetAllSportFa() {
        apiService = RetrofitClient.getClient().create(ServiceAPI.class);
        apiService.getAllSportFacility().enqueue(new Callback<List<SportFacility>>() {
            @Override
            public void onResponse(Call<List<SportFacility>> call, Response<List<SportFacility>> response) {
                if(response.isSuccessful()){
                    sportFacilities=response.body();
                    if (!sportFacilities.isEmpty()) {
                        LoadingMap(0);
                    }
                    PhotoAdapter photoAdapter = new PhotoAdapter(HomeFragment.this, sportFacilities, new PhotoAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Log.d("HomeFragment", "Clicked position: " + vitri);
                        }
                    });
                    viewPager2.setAdapter(photoAdapter);
                }
                else {
                    Log.d("API ERROR", "Không thể lấy danh sách");
                }
            }

            @Override
            public void onFailure(Call<List<SportFacility>> call, Throwable t) {
                Log.d("API ERROR", "Không thể lấy danh mục" + t.getMessage());
            }
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


    private void LoadingMap(int vitri) {
        SportFacility sportFacility = sportFacilities.get(vitri);
        LatLng location = new LatLng(sportFacility.getLatitude(), sportFacility.getLongitude());
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(location).title(sportFacility.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
//        txtName.setText(sportFacility.getName());
    }
}
