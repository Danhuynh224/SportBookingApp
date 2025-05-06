package com.example.pjbookingsport.frag;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pjbookingsport.API.AddressAPI;
import com.example.pjbookingsport.API.RetrofitAddress;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.adapter.ButtonCardAdapter;
import com.example.pjbookingsport.model.District;
import com.example.pjbookingsport.model.DistrictResponse;
import com.example.pjbookingsport.model.Province;
import com.example.pjbookingsport.model.ProvinceResponse;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterFragment extends Fragment {

    private ImageButton btnBack;
    private AutoCompleteTextView provincePicker, districtPicker;
    private TextView districtLabel;
    private TextInputLayout districtLayout;
    private List<Province> provinces = new ArrayList<>();
    private List<District> districts = new ArrayList<>();
    private List<String> provinceNames = new ArrayList<>();
    private List<String> districtNames = new ArrayList<>();
    private String idProvince;
    private RecyclerView recyclerView;
    private ButtonCardAdapter adapter;
    private AddressAPI addressAPI;
    private AppCompatButton resetButton, applyButton;

    boolean[] selectedStates;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // anh xa
        btnBack = view.findViewById(R.id.btn_back);
        provincePicker = view.findViewById(R.id.province_picker);
        districtPicker = view.findViewById(R.id.district_picker);
        districtLabel = view.findViewById(R.id.district_label);
        districtLayout = view.findViewById(R.id.district_layout);
        recyclerView = view.findViewById(R.id.rvBoMon);
        resetButton = view.findViewById(R.id.reset_button);
        applyButton = view.findViewById(R.id.apply_button);
        addressAPI = RetrofitAddress.getClient().create(AddressAPI.class);

        // nút back
        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // datetimepicker
//        datetimepicker.setOnClickListener(v -> {
//            DialogFragment newFragment = new DateTimePickerFragment(datetimepicker);
//            newFragment.show(getParentFragmentManager(), "dateTimePicker");
//        });

        // citypicker
        getProvinces();

        provincePicker.setOnClickListener(v -> provincePicker.showDropDown());
        districtPicker.setOnClickListener(v -> districtPicker.showDropDown());

        provincePicker.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedProvince = provinceNames.get(position);
            provincePicker.setText(selectedProvince, false);

            if ("Tất cả".equals(selectedProvince)) {
                idProvince = null;
                districtLabel.setVisibility(View.GONE);
                districtLayout.setVisibility(View.GONE);
                districtNames.clear();
                districtNames.add("Tất cả");
                districtPicker.setText("Tất cả", false);

                ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_dropdown_item_1line, districtNames);
                districtPicker.setAdapter(districtAdapter);
                return;
            }

            // Nếu không chọn "Tất cả", tìm ID tỉnh
            for (Province province : provinces) {
                if (province.getProvince_name().equals(selectedProvince)) {
                    idProvince = province.getProvince_id();
                    break;
                }
            }

            districtLabel.setVisibility(View.VISIBLE);
            districtLayout.setVisibility(View.VISIBLE);
            districtPicker.setText("Tất cả", false);
            getDistricts();
        });

        districtPicker.setOnItemClickListener((parent, view12, position, id) -> {
            String selectedDistrict = districtNames.get(position);
            districtPicker.setText(selectedDistrict, false);
        });

        // recyclerview type
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        recyclerView.setLayoutManager(layoutManager);

        List<String> options = Arrays.asList("Cầu lông", "Pickleball", "Tennis", "Bi-a", "Bóng đá", "Bóng rổ");
        adapter = new ButtonCardAdapter(getContext(), options);
        recyclerView.setAdapter(adapter);

        // bat su kien nut xoa
        resetButton.setOnClickListener(v -> resetFilters());

        // bat su kien nut apply
        applyButton.setOnClickListener(v ->
                applyFilters()
        );
    }

    private void resetFilters() {
        adapter.clearSelection();
        provincePicker.setText("Tất cả", false);
        districtPicker.setText("Tất cả", false);
        districtLabel.setVisibility(View.GONE);
        districtLayout.setVisibility(View.GONE);

    }

    private void applyFilters() {
        List<String> selectedTypes = adapter.getSelectedItems();
        String selectedCity = provincePicker.getText().toString();
        String selectedDistrict = districtPicker.getText().toString();

        BigDecimal minPrice = null;
        BigDecimal maxPrice = null;

        Bundle result = new Bundle();
        result.putStringArrayList("selectedTypes", new ArrayList<>(selectedTypes));
        result.putString("selectedCity", selectedCity.equals("Tất cả") ? null : selectedCity);
        result.putString("selectedDistrict", selectedDistrict.equals("Tất cả") ? null : selectedDistrict);
        result.putSerializable("minPrice", minPrice);
        result.putSerializable("maxPrice", maxPrice);

        getParentFragmentManager().setFragmentResult("filterRequest", result);

        // Quay lai ListFragment
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void getProvinces() {
        addressAPI.getProvinces().enqueue(new Callback<ProvinceResponse>() {
            @Override
            public void onResponse(Call<ProvinceResponse> call, Response<ProvinceResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    provinces = response.body().getResults();
                    provinceNames.clear();
                    provinceNames.add("Tất cả");

                    for (Province province : provinces) {
                        provinceNames.add(province.getProvince_name());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                            android.R.layout.simple_dropdown_item_1line, provinceNames);
                    provincePicker.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ProvinceResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Không thể tải tỉnh/thành phố", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDistricts() {
        if (idProvince == null) return;

        addressAPI.getDistricts(idProvince).enqueue(new Callback<DistrictResponse>() {
            @Override
            public void onResponse(Call<DistrictResponse> call, Response<DistrictResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    districts = response.body().getResults();
                    districtNames.clear();
                    districtNames.add("Tất cả");
                    for (District district : districts) {
                        districtNames.add(district.getDistrict_name());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                            android.R.layout.simple_dropdown_item_1line, districtNames);
                    districtPicker.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<DistrictResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Không thể tải quận/huyện", Toast.LENGTH_SHORT).show();
            }
        });
    }

}