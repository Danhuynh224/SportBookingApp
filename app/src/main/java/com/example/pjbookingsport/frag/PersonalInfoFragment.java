package com.example.pjbookingsport.frag;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pjbookingsport.API.AddressAPI;
import com.example.pjbookingsport.API.RetrofitAddress;
import com.example.pjbookingsport.API.RetrofitClient;
import com.example.pjbookingsport.API.ServiceAPI;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.adapter.FacilityPagerAdapter;
import com.example.pjbookingsport.adapter.PersonalPagerAdapter;
import com.example.pjbookingsport.model.Province;
import com.example.pjbookingsport.model.ProvinceResponse;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Spinner spinnerGender ;
    EditText edtBirthday ;
    private TextView cityPicker;
    List<Province> provinces;
    List<String> provinceNames = new ArrayList<>();

    private AddressAPI addressAPI;
    String[] genders = {"Chọn giới tính", "Nam", "Nữ", "Khác"};


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PersonalInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalInfoFragment newInstance(String param1, String param2) {
        PersonalInfoFragment fragment = new PersonalInfoFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_info, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerGender = view.findViewById(R.id.spinnerGender);
        edtBirthday = view.findViewById(R.id.edtBirthday);
        cityPicker = view.findViewById(R.id.city_picker);



        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
        spinnerGender.setSelection(0);

        edtBirthday.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    null,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            datePickerDialog.setOnShowListener(dialog -> {
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setOnClickListener(posView -> {
                    int year = datePickerDialog.getDatePicker().getYear();
                    int month = datePickerDialog.getDatePicker().getMonth();
                    int day = datePickerDialog.getDatePicker().getDayOfMonth();
                    String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d",
                            day, month + 1, year);
                    edtBirthday.setText(selectedDate);
                    datePickerDialog.dismiss();
                });
            });

            datePickerDialog.show();
        });

        cityPicker.setOnClickListener(view1 -> {
            if (provinces != null && !provinces.isEmpty()) {
                showCityPickerDialog();
            } else {
                // Gọi API ngay khi tạo Fragment
                getProvinces();
            }
        });
    }
    private void showCityPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Chọn tỉnh/thành phố");

        // Chuyển List<String> sang String[]
        String[] provinceArray = provinceNames.toArray(new String[0]);

        builder.setItems(provinceArray, (dialog, which) -> {
            cityPicker.setText(provinceArray[which]);
        });
        builder.show();
    }

    private  void getProvinces(){
        addressAPI = RetrofitAddress.getClient().create(AddressAPI.class);
        addressAPI.getProvinces().enqueue(new Callback<ProvinceResponse>() {
            @Override
            public void onResponse(Call<ProvinceResponse> call, Response<ProvinceResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    provinces = response.body().getData();
                    provinceNames.clear();
                    for (Province province : provinces) {
                        provinceNames.add(province.getName());
                    }
                    showCityPickerDialog();
                }
            }

            @Override
            public void onFailure(Call<ProvinceResponse> call, Throwable t) {

            }
        });
    }

}