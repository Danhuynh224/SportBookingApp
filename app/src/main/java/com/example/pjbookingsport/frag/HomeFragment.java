package com.example.pjbookingsport.frag;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
        btnLeft = view.findViewById(R.id.btnLeft);
        btnRight = view.findViewById(R.id.btnRight);
        imgMain = view.findViewById(R.id.imgMain);
        txtName = view.findViewById(R.id.txtName);
        GetAllSportFa();
        btnRight.setOnClickListener(v -> {
            vitri = (vitri + 1) % sportFacilities.size();
            LoadingMap(vitri);
        });

        btnLeft.setOnClickListener(v -> {
            vitri = (vitri == 0) ? sportFacilities.size() - 1 : vitri - 1;
            LoadingMap(vitri);
        });
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
        Glide.with(this)
                .load(imgUrl+sportFacility.getSportsFacilityId())
                .placeholder(R.drawable.ic_launcher_foreground) // Hình ảnh mặc định nếu không có ảnh
                .into(imgMain);
        txtName.setText(sportFacility.getName());
    }
}
