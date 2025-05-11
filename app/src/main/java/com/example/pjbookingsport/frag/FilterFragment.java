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

    private ArrayList<String> savedSelectedTypes;
    private String savedProvince = "Tất cả";
    private String savedDistrict = "Tất cả";
    private float savedRating = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            savedSelectedTypes = args.getStringArrayList("selectedTypes");
            savedProvince = args.getString("selectedProvince", "Tất cả");
            savedDistrict = args.getString("selectedDistrict", "Tất cả");
            savedRating = args.getFloat("selectedRating", 0);
        }
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

        if (savedProvince != null && !savedProvince.equals("Tất cả")) {
            provincePicker.setText(savedProvince);
            districtLabel.setVisibility(View.VISIBLE);
            districtPicker.setVisibility(View.VISIBLE);
        } else {
            districtLabel.setVisibility(View.GONE);
            districtPicker.setVisibility(View.GONE);
        }

        // Always set the district text, regardless of visibility
        if (savedDistrict != null) {
            districtPicker.setText(savedDistrict);
        } else {
            districtPicker.setText("Tất cả");
        }

        if (savedRating > 0) {
            ratingBar.setRating(savedRating);
            int star = (int) savedRating;
            ratingText.setText(star + " sao trở lên");
        }

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
            if (idProvince != null) {
                if (districts != null && !districts.isEmpty()) {
                    showDistrictPickerDialog();
                } else {
                    // Gọi API lấy danh sách quận/huyện
                    getDistrict();
                }
            } else {
                Toast.makeText(getContext(), "Vui lòng chọn tỉnh/thành phố trước", Toast.LENGTH_SHORT).show();
            }
        });

        if (savedProvince != null && !savedProvince.equals("Tất cả")) {
            loadInitialProvinceData();
        }

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


        // Thiết lập lại các lựa chọn thể loại
        if (savedSelectedTypes != null && !savedSelectedTypes.isEmpty()) {
            adapter.setSelectedItems(savedSelectedTypes);
        }


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
        result.putStringArrayList("selectedTypes", new ArrayList<>());
        result.putString("selectedCity", null);
        result.putString("selectedDistrict", null);
        result.putInt("selectedRating", 0);
        result.putFloat("savedRating", 0);

        getParentFragmentManager().setFragmentResult("filterRequest", result);

        // Quay lại ListFragment
        requireActivity().getSupportFragmentManager().popBackStack();

    }

    private void applyFilters() {
        List<String> selectedTypes = adapter.getSelectedItems();
        String selectedCity = provincePicker.getText().toString();
        int selectedRating = (int) ratingBar.getRating();

        String selectedDistrict = districtPicker.getText().toString().trim();
        String[] prefixes = {"Quận", "Huyện", "Thị xã", "Thành phố"};
        // Bỏ tiền tố của quận, huyện
        for (String prefix : prefixes) {
            if (selectedDistrict.startsWith(prefix + " ")) {
                selectedDistrict = selectedDistrict.substring(prefix.length()).trim();
                break;
            }
        }

        Bundle result = new Bundle();
        result.putStringArrayList("selectedTypes", new ArrayList<>(selectedTypes));
        result.putString("selectedCity", selectedCity.equals("Tất cả") ? null : selectedCity);
        result.putString("selectedDistrict", selectedDistrict.equals("Tất cả") ? null : selectedDistrict);
        result.putInt("selectedRating", selectedRating);

        result.putStringArrayList("savedSelectedTypes", new ArrayList<>(selectedTypes));
        result.putString("savedProvince", selectedCity);
        result.putString("savedDistrict", selectedDistrict);
        result.putFloat("savedRating", ratingBar.getRating());

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
                    if (province.getFull_name().equals(selectedProvince)) {
                        idProvince = province.getId();
                        break;
                    }
                }

                districts = null;
                ditrictNames.clear();
                districtPicker.setText("Tất cả");

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
                    provinces = response.body().getData();
                    provinceNames.clear();
                    provinceNames.add("Tất cả");
                    for (Province province : provinces) {
                        provinceNames.add(province.getFull_name());
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
                        districts = response.body().getData();
                        ditrictNames.clear();
                        ditrictNames.add("Tất cả");
                        for(District district : districts){
                            ditrictNames.add(district.getFull_name());
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

    private void loadInitialProvinceData() {
        addressAPI.getProvinces().enqueue(new Callback<ProvinceResponse>() {
            @Override
            public void onResponse(Call<ProvinceResponse> call, Response<ProvinceResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    provinces = response.body().getData();
                    provinceNames.clear();
                    provinceNames.add("Tất cả");

                    for (Province province : provinces) {
                        provinceNames.add(province.getFull_name());

                        if (province.getFull_name().equals(savedProvince)) {
                            idProvince = province.getId();

                            // Nếu có quận/huyện đã chọn và không phải "Tất cả", cần lấy danh sách quận/huyện
                            if (savedDistrict != null && !savedDistrict.equals("Tất cả")) {
                                loadInitialDistrictData();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ProvinceResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Không thể tải danh sách tỉnh/thành phố", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadInitialDistrictData() {
        if (idProvince != null) {
            addressAPI.getDistricts(idProvince).enqueue(new Callback<DistrictResponse>() {
                @Override
                public void onResponse(Call<DistrictResponse> call, Response<DistrictResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        districts = response.body().getData();
                        ditrictNames.clear();
                        ditrictNames.add("Tất cả");

                        for (District district : districts) {
                            ditrictNames.add(district.getFull_name());
                        }
                    }
                }

                @Override
                public void onFailure(Call<DistrictResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Không thể tải danh sách quận/huyện", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}