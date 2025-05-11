package com.example.pjbookingsport.frag;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pjbookingsport.API.AddressAPI;
import com.example.pjbookingsport.API.RetrofitAddress;
import com.example.pjbookingsport.API.RetrofitClient;
import com.example.pjbookingsport.API.ServiceAPI;
import com.example.pjbookingsport.activity.LoginActivity;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.model.District;
import com.example.pjbookingsport.model.DistrictResponse;
import com.example.pjbookingsport.model.Province;
import com.example.pjbookingsport.model.ProvinceResponse;
import com.example.pjbookingsport.model.User;
import com.example.pjbookingsport.sharedPreferences.SharedPreferencesHelper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.ResponseBody;
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

    Button updateBtn, logOutBtn;
    EditText edtBirthday, txtUserName, txtPhone, txtEmail ;
    private TextView cityPicker,districtPicker, sexPicker;
    List<Province> provinces;
    List<String> provinceNames = new ArrayList<>();
    List<District> districts;
    List<String> ditrictNames = new ArrayList<>();

    String idProvince;

    User user ;
    private AddressAPI addressAPI;
    private ServiceAPI serviceAPI;
    String[] genders = { "Nam", "Nữ", "LGBT"};


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
        addressAPI = RetrofitAddress.getClient().create(AddressAPI.class);
        sexPicker = view.findViewById(R.id.sex_picker);
        edtBirthday = view.findViewById(R.id.edtBirthday);
        cityPicker = view.findViewById(R.id.city_picker);
        districtPicker = view.findViewById(R.id.district_picker);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtUserName = view.findViewById(R.id.txtUserName);
        txtPhone = view.findViewById(R.id.txtPhone);
        user = SharedPreferencesHelper.getUser(getContext());
        logOutBtn = view.findViewById(R.id.logOutBtn);
        updateBtn = view.findViewById(R.id.updateBtn);
        logOutBtn.setOnClickListener(view2 -> showLogoutDialog());
        updateBtn.setOnClickListener(view3 -> update());
        binding();
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

        sexPicker.setOnClickListener(view1 -> {
            showSexPickerDialog();
        });

        cityPicker.setOnClickListener(view1 -> {
            if (provinces != null && !provinces.isEmpty()) {
                showCityPickerDialog();
            } else {
                // Gọi API ngay khi tạo Fragment
                getProvinces();
            }
        });

        districtPicker.setOnClickListener(view1 -> {
            if (districts != null && !districts.isEmpty()) {
                showDistrictPickerDialog();
            } else {
                // Gọi API ngay khi tạo Fragment
                getDistrict();
            }
        });
    }

    private void showSexPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Chọn giới tính");

        builder.setItems(genders, (dialog, which) -> {
            sexPicker.setText(genders[which]);
        });
        builder.show();
    }

    private void showCityPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Chọn tỉnh/thành phố");

        // Chuyển List<String> sang String[]
        String[] provinceArray = provinceNames.toArray(new String[0]);

        builder.setItems(provinceArray, (dialog, which) -> {
            cityPicker.setText(provinceArray[which]);
            for(Province province : provinces ){
                if(Objects.equals(province.getFull_name(), provinceArray[which])){
                    idProvince = province.getId();
                }
            }
        });
        builder.show();
    }

    private void showDistrictPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Chọn huyện/quận");

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
                    Log.d("Test Province:", provinces.toString());
                    provinceNames.clear();
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

    private void binding(){
        txtUserName.setText(user.getFullName());
        txtPhone.setText(user.getPhone());
        txtEmail.setText(user.getEmail());
        if(!user.getAddress().isEmpty()){
            String[] parts = user.getAddress().split("\\|");
            districtPicker.setText(parts[0]);
            cityPicker.setText(parts[1]);
        }
        sexPicker.setText(user.getSex());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String selectedDate = user.getBirthday().format(formatter);
        edtBirthday.setText(selectedDate);
    }

    private void update(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String fullName = txtUserName.getText().toString();
        String phone = txtPhone.getText().toString();
        String email = txtEmail.getText().toString();
        String sex = sexPicker.getText().toString();
        String address = districtPicker.getText().toString() + "|" + cityPicker.getText().toString();

        LocalDate birthday = null;
        try {
            birthday = LocalDate.parse(edtBirthday.getText().toString(), formatter);
        } catch (Exception e) {
            e.printStackTrace(); // xử lý nếu người dùng nhập sai format
        }

        user.setFullName(fullName);
        user.setPhone(phone);
        user.setEmail(email);
        user.setSex(sex);
        user.setAddress(address);
        user.setBirthday(birthday);
        SharedPreferencesHelper.saveUser(getContext(),user);
        serviceAPI = RetrofitClient.getClient().create(ServiceAPI.class);
        serviceAPI.updateUser(user).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    showResultDialog(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Lỗi API", "Không truy cập được API: " + t.getMessage());
            }
        });

    }
    private void showResultDialog(boolean isSuccess) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if (isSuccess) {
            builder.setTitle("Cập nhật thành công 🎉");
            builder.setMessage("Thông tin của bạn đã được cập nhật");
        } else {
            builder.setTitle("Thất bại ❌");
            builder.setMessage("Cập nhật thông tin thất bại vui lòng kiểm tra lại");
        }

        builder.setPositiveButton("OK", (dialog, which) -> {
            if(isSuccess) {
                binding();
            }
            dialog.dismiss(); // Đóng dialog
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showLogoutDialog() {
        Dialog dialog = new Dialog(requireContext(), R.style.CustomDialog);
        dialog.setContentView(R.layout.dialog_confirm_logout);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0.5f);

        Window window = dialog.getWindow();
        if (window != null) {
            // Mở rộng chiều ngang của dialog (ví dụ 90% chiều rộng màn hình)
            window.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.9), WindowManager.LayoutParams.WRAP_CONTENT);
        }

        AppCompatButton btnHuy = dialog.findViewById(R.id.huyBtn);
        AppCompatButton btnLogout = dialog.findViewById(R.id.logOutBtn);

        btnHuy.setOnClickListener(v -> dialog.dismiss());

        btnLogout.setOnClickListener(v -> {
            if(!user.isSave()) {
                SharedPreferencesHelper.clearAccount(requireContext());
                SharedPreferencesHelper.clearUser(requireContext());
            }
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            dialog.dismiss();
        });

        dialog.show();
    }

}