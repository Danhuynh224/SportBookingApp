package com.example.pjbookingsport.frag;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.RatingBar;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterFragment extends Fragment {

    private ImageButton btnBack;
    private TextView provincePicker, districtPicker;
    private TextView districtLabel, ratingText;
    List<Province> provinces;
    List<String> provinceNames = new ArrayList<>();
    List<District> districts;
    List<String> ditrictNames = new ArrayList<>();
    private RatingBar ratingBar;
    private String idProvince;
    private RecyclerView recyclerView;
    private ButtonCardAdapter adapter;
    private AddressAPI addressAPI;
    private AppCompatButton resetButton, applyButton;

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
        recyclerView = view.findViewById(R.id.rvBoMon);
        resetButton = view.findViewById(R.id.reset_button);
        applyButton = view.findViewById(R.id.apply_button);
        addressAPI = RetrofitAddress.getClient().create(AddressAPI.class);
        ratingBar = view.findViewById(R.id.rating_filter_bar);
        ratingText = view.findViewById(R.id.rating_text);

        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            int star = (int) rating;
            if (star == 0) {
                ratingText.setText("Tất cả đánh giá");
            } else {
                ratingText.setText(star + " sao trở lên");
            }
        });

        // nút back
        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        provincePicker.setOnClickListener(v -> {
            if (provinces != null && !provinces.isEmpty()) {
                showCityPickerDialog();
            } else {
                // Gọi API ngay khi tạo Fragment
                getProvinces();
            }
        });

        districtPicker.setOnClickListener(v -> {
            if (districts != null && !districts.isEmpty()) {
                showDistrictPickerDialog();
            } else {
                // Gọi API ngay khi tạo Fragment
                getDistrict();
            }
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
        provincePicker.setText("Tất cả");
        districtPicker.setText("Tất cả");
        districtLabel.setVisibility(View.GONE);
        districtPicker.setVisibility(View.GONE);
        ratingBar.setRating(0);
        ratingText.setText("Tất cả đánh giá");

        Bundle result = new Bundle();
        result.putBoolean("resetFilters", true);
        getParentFragmentManager().setFragmentResult("resetFilters", result);

    }

    private void applyFilters() {
        List<String> selectedTypes = adapter.getSelectedItems();
        String selectedCity = provincePicker.getText().toString();
        String selectedDistrict = districtPicker.getText().toString();
        int selectedRating = (int) ratingBar.getRating();

        Bundle result = new Bundle();
        result.putStringArrayList("selectedTypes", new ArrayList<>(selectedTypes));
        result.putString("selectedCity", selectedCity.equals("Tất cả") ? null : selectedCity);
        result.putString("selectedDistrict", selectedDistrict.equals("Tất cả") ? null : selectedDistrict);
        result.putInt("selectedRating", selectedRating);

        getParentFragmentManager().setFragmentResult("filterRequest", result);

        // Quay lai ListFragment
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void showCityPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Chọn tỉnh/thành phố");

        String[] provinceArray = provinceNames.toArray(new String[0]);

        builder.setItems(provinceArray, (dialog, which) -> {
            String selectedProvince = provinceArray[which];
            provincePicker.setText(selectedProvince);

            if ("Tất cả".equals(selectedProvince)) {
                idProvince = null;
                districtLabel.setVisibility(View.GONE);
                districtPicker.setVisibility(View.GONE);
                districtPicker.setText("Tất cả");
            } else {
                for (Province province : provinces) {
                    if (province.getProvince_name().equals(selectedProvince)) {
                        idProvince = province.getProvince_id();
                        break;
                    }
                }
                districtLabel.setVisibility(View.VISIBLE);
                districtPicker.setVisibility(View.VISIBLE);
            }
        });

        builder.show();
    }

    private void showDistrictPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Chọn quận/huyện");

        // Chuyển List<String> sang String[]
        String[] districtArray = ditrictNames.toArray(new String[0]);

        builder.setItems(districtArray, (dialog, which) -> {
            districtPicker.setText(districtArray[which]);
        });
        builder.show();
    }

    private  void getProvinces(){
        addressAPI.getProvinces().enqueue(new Callback<ProvinceResponse>() {
            @Override
            public void onResponse(Call<ProvinceResponse> call, Response<ProvinceResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    provinces = response.body().getResults();
                    provinceNames.clear();
                    provinceNames.add("Tất cả");
                    for (Province province : provinces) {
                        provinceNames.add(province.getProvince_name());
                    }
                    showCityPickerDialog();
                }
            }

            @Override
            public void onFailure(Call<ProvinceResponse> call, Throwable t) {

            }
        });
    }
    private void getDistrict(){
        if(idProvince!=null){
            addressAPI.getDistricts(idProvince).enqueue(new Callback<DistrictResponse>() {
                @Override
                public void onResponse(Call<DistrictResponse> call, Response<DistrictResponse> response) {
                    if(response.isSuccessful() && response.body() !=null){
                        districts = response.body().getResults();
                        ditrictNames.clear();
                        ditrictNames.add("Tất cả");
                        for(District district : districts){
                            ditrictNames.add(district.getDistrict_name());
                        }
                        showDistrictPickerDialog();
                    }
                }

                @Override
                public void onFailure(Call<DistrictResponse> call, Throwable t) {

                }
            });
        }
    }

}