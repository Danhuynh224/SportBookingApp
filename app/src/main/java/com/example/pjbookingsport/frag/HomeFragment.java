package com.example.pjbookingsport.frag;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pjbookingsport.R;
import com.example.pjbookingsport.model.Field;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

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
    private ArrayList<Field> fields;
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
        // Tạo danh sách sân cầu lông
        fields = new ArrayList<>();
        fields.add(new Field("Sân Cầu Lông Victory", 10.836353204826953, 106.75468618989, "victory"));
        fields.add(new Field("B-ZONE 11", 10.84469880757791, 106.75306846670372, "bzone11"));
        fields.add(new Field("Galaxy Badminton", 10.835030475299105, 106.76412455448373, "galaxy"));
        fields.add(new Field("CLB Cầu Lông An Bình 2", 10.867798100203814, 106.75132328893021, "anbinh2"));
        fields.add(new Field("Sân Cầu Lông Cây Keo", 10.860572472016461, 106.74007410046538, "caykeo"));

        btnRight.setOnClickListener(v -> {
            vitri = (vitri + 1) % fields.size();
            LoadingMap(vitri);
        });

        btnLeft.setOnClickListener(v -> {
            vitri = (vitri == 0) ? fields.size() - 1 : vitri - 1;
            LoadingMap(vitri);
        });
    }
    private void LoadingMap(int vitri) {
        Field field = fields.get(vitri);
        LatLng location = new LatLng(field.getLatitude(), field.getLongitude());
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(location).title(field.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

        @SuppressLint("DiscouragedApi")
        int imageId = getResources().getIdentifier(field.getImg(), "drawable", getActivity().getPackageName());
        imgMain.setImageResource(imageId);
        txtName.setText(field.getName());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style));
        LoadingMap(0);  // Load vị trí đầu tiên khi mở Fragment
    }

}
