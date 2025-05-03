package com.example.pjbookingsport.frag;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pjbookingsport.API.AddressAPI;
import com.example.pjbookingsport.API.RetrofitAddress;
import com.example.pjbookingsport.API.RetrofitClient;
import com.example.pjbookingsport.API.ServiceAPI;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.adapter.ButtonCardAdapter;
import com.example.pjbookingsport.model.District;
import com.example.pjbookingsport.model.DistrictResponse;
import com.example.pjbookingsport.model.Province;
import com.example.pjbookingsport.model.ProvinceResponse;
import com.example.pjbookingsport.model.SportFacility;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ServiceAPI apiService;
    private AddressAPI addressAPI;
    private AppCompatButton resetButton, applyButton;
    private MaterialCardView cardPriceBelow500, cardPrice500_1m, cardPriceAbove1m;
    private TextView textPriceBelow500, textPrice500_1m, textPriceAbove1m;

    boolean[] selectedStates;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        // anh xa
        btnBack = view.findViewById(R.id.btn_back);
        provincePicker = view.findViewById(R.id.province_picker);
        districtPicker = view.findViewById(R.id.district_picker);
        districtLabel = view.findViewById(R.id.district_label);
        districtLayout = view.findViewById(R.id.district_layout);
        recyclerView = view.findViewById(R.id.rvBoMon);
        resetButton = view.findViewById(R.id.reset_button);
        applyButton = view.findViewById(R.id.apply_button);
        cardPriceBelow500 = view.findViewById(R.id.card_price_below_500);
        cardPrice500_1m = view.findViewById(R.id.card_price_500_1m);
        cardPriceAbove1m = view.findViewById(R.id.card_price_above_1m);
        textPriceBelow500 = view.findViewById(R.id.text_price_below_500);
        textPrice500_1m = view.findViewById(R.id.text_price_500_1m);
        textPriceAbove1m = view.findViewById(R.id.text_price_above_1m);
        MaterialCardView[] cards = {cardPriceBelow500, cardPrice500_1m, cardPriceAbove1m};
        TextView[] texts = {textPriceBelow500, textPrice500_1m, textPriceAbove1m};


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
            provincePicker.setText(selectedProvince, false); // false để tránh trigger lại dropdown

            // Lấy idProvince
            for (Province province : provinces) {
                if (province.getProvince_name().equals(selectedProvince)) {
                    idProvince = province.getProvince_id();
                    break;
                }
            }

            districtLabel.setVisibility(View.VISIBLE);
            districtLayout.setVisibility(View.VISIBLE);

            // Reset quận/huyện khi chọn tỉnh mới
            districtPicker.setText("Tất cả");
            districtNames.clear();
            districts.clear();

            getDistricts(); // Gọi API lấy quận/huyện
        });

        districtPicker.setOnItemClickListener((parent, view12, position, id) -> {
            String selectedDistrict = districtNames.get(position);
            districtPicker.setText(selectedDistrict, false);
            // Bạn có thể lấy id Quận nếu muốn tương tự như trên
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

        selectedStates = new boolean[cards.length];

        for (int i = 0; i < cards.length; i++) {
            final int index = i;
            cards[i].setOnClickListener(v -> {
                // Nếu card đang được chọn, bỏ chọn nó
                if (selectedStates[index]) {
                    selectedStates[index] = false;
                    updateCardState(cards[index], texts[index], false);
                } else {
                    // Đặt lại tất cả về trạng thái mặc định trước khi chọn mới
                    for (int j = 0; j < cards.length; j++) {
                        selectedStates[j] = (j == index); // Chỉ giữ trạng thái của button hiện tại
                        updateCardState(cards[j], texts[j], j == index);
                    }
                }
            });
        }


        return view;
    }


    private void resetFilters() {
        adapter.clearSelection();

        provincePicker.setText("Tất cả");

        updateCardState(cardPriceBelow500, textPriceBelow500, false);
        updateCardState(cardPrice500_1m, textPrice500_1m, false);
        updateCardState(cardPriceAbove1m, textPriceAbove1m, false);

        ListFragment listFragment = new ListFragment();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, listFragment)
                .addToBackStack(null)
                .commit();
        listFragment.GetSportFacilities();

    }

    private void applyFilters() {
        List<String> selectedTypes = adapter.getSelectedItems();
        String selectedCity = provincePicker.getText().toString();
        BigDecimal minPrice = null;
        BigDecimal maxPrice = null;

        // Kiểm tra xem có chọn bộ lọc giá hay không
        if (selectedStates[0]) { // Card giá dưới 500k
            minPrice = new BigDecimal(0);
            maxPrice = new BigDecimal(500000);
        } else if (selectedStates[1]) { // Card giá từ 500k đến 1M
            minPrice = new BigDecimal(500000);
            maxPrice = new BigDecimal(1000000);
        } else if (selectedStates[2]) { // Card giá trên 1M
            minPrice = new BigDecimal(1000000);
            maxPrice = new BigDecimal(Integer.MAX_VALUE);
        }

        // Tạo bundle để gửi dữ liệu
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("selectedTypes", new ArrayList<>(selectedTypes));
        bundle.putString("selectedCity", selectedCity.isEmpty() ? null : selectedCity);
        bundle.putSerializable("minPrice", minPrice);
        bundle.putSerializable("maxPrice", maxPrice);

        // Tạo ListFragment và truyền bundle
        ListFragment listFragment = new ListFragment();
        listFragment.setArguments(bundle);

        // Chuyển về ListFragment
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, listFragment)
                .addToBackStack("FilterFragment")
                .commit();

    }

    private void updateCardState(MaterialCardView card, TextView text, boolean isSelected) {
        if (isSelected) {
            card.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.primary));
            card.setStrokeColor(ContextCompat.getColor(requireContext(), android.R.color.transparent));
            text.setTextColor(Color.WHITE);
        } else {
            card.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
            card.setStrokeColor(ContextCompat.getColor(requireContext(), R.color.textDisabled));
            text.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
        }
    }

    private void getProvinces() {
        addressAPI = RetrofitAddress.getClient().create(AddressAPI.class);
        addressAPI.getProvinces().enqueue(new Callback<ProvinceResponse>() {
            @Override
            public void onResponse(Call<ProvinceResponse> call, Response<ProvinceResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    provinces = response.body().getResults();
                    provinceNames.clear();
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
        if (idProvince != null) {
            addressAPI = RetrofitAddress.getClient().create(AddressAPI.class);
            addressAPI.getDistricts(idProvince).enqueue(new Callback<DistrictResponse>() {
                @Override
                public void onResponse(Call<DistrictResponse> call, Response<DistrictResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        districts = response.body().getResults();
                        districtNames.clear();
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


}