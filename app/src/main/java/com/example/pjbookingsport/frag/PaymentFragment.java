package com.example.pjbookingsport.frag;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.pjbookingsport.API.RetrofitClient;
import com.example.pjbookingsport.API.ServiceAPI;
import com.example.pjbookingsport.PaymentVNPayActivity;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.adapter.BookInForAdapter;
import com.example.pjbookingsport.adapter.BookInforPayAdapter;
import com.example.pjbookingsport.enums.Role;
import com.example.pjbookingsport.model.Booking;
import com.example.pjbookingsport.model.SportFacility;
import com.example.pjbookingsport.model.User;
import com.example.pjbookingsport.sharedPreferences.SharedPreferencesHelper;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FACILITY = "FACILITY";
    private SportFacility facility;
    private static final String ARG_BOOK = "BOOK";
    private Booking booking;
    private TextView txtName, txtAddress, txtType, txtDate, txtTotal;
    private RadioGroup radioGroup;
    private RadioButton radioCash, radioBank;
    private EditText txtUserName, txtPhone;
    private Button huyBtn, bookBtn;
    private RecyclerView rcInfo;
    private ImageButton btnBack;
    BookInforPayAdapter bookInForAdapter;
    User user;

    private ServiceAPI serviceAPI;
    public PaymentFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PaymentFragment newInstance(SportFacility facility, Booking booking) {
        PaymentFragment fragment = new PaymentFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FACILITY, facility);
        args.putSerializable(ARG_BOOK, booking);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            facility = (SportFacility) getArguments().getSerializable(ARG_FACILITY);
            booking = (Booking) getArguments().getSerializable(ARG_BOOK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        txtName = view.findViewById(R.id.txtName);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtType = view.findViewById(R.id.txtType);
        txtDate = view.findViewById(R.id.txtDate);
        txtTotal = view.findViewById(R.id.txtTotal);
        rcInfo = view.findViewById(R.id.rcInfo);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioBank = view.findViewById(R.id.radioBank);
        radioCash = view.findViewById(R.id.radioCash);
        txtUserName = view.findViewById(R.id.txtUserName);
        txtPhone = view.findViewById(R.id.txtPhone);
        huyBtn = view.findViewById(R.id.huyBtn);
        bookBtn = view.findViewById(R.id.bookBtn);
        btnBack = view.findViewById(R.id.btn_back);
        user = SharedPreferencesHelper.getUser(getContext());
        LoadBookingInfo();
    }

    private void LoadBookingInfo() {

        txtName.setText(facility.getName());
        txtAddress.setText(facility.getAddress());
        txtType.setText(booking.getBookingInfos().get(0).getSubFacility().getFacilityType().getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());
        String dateString = booking.getBookingDate().format(formatter);
        txtDate.setText(dateString);
        bookInForAdapter = new BookInforPayAdapter(booking.getBookingInfos());
        rcInfo.setAdapter(bookInForAdapter);
        rcInfo.setLayoutManager(new LinearLayoutManager(getContext()));
        // Định dạng số với dấu chấm phân cách hàng nghìn
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.'); // Dùng dấu chấm (.) để phân tách hàng nghìn

        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        txtTotal.setText(decimalFormat.format(booking.getTotalPrice())+" VND");
        txtUserName.setText(user.getFullName());
        txtPhone.setText(user.getPhone());
        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!radioCash.isChecked() && !radioBank.isChecked())
                {
                    showResultDialog(false);
                }
                if(radioCash.isChecked()){
                    if(!user.getFullName().equals(txtUserName.getText().toString()) || !user.getPhone().equals(txtPhone.getText().toString())){
                        updateInfoUser();
                    }
                    Intent intent = new Intent(getContext(), PaymentVNPayActivity.class);
                    intent.putExtra("booking", booking); // gửi object
                    startActivity(intent);

                }
            }
        });
    }

    private void updateInfoUser() {
        user.setFullName(txtUserName.getText().toString());
        user.setPhone(txtPhone.getText().toString());
        SharedPreferencesHelper.saveUser(getContext(),user);
        serviceAPI = RetrofitClient.getClient().create(ServiceAPI.class);
        serviceAPI.updateUser(user).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Lỗi API", "Không truy cập được API: " + t.getMessage());
            }
        });
    }

    private void addNewBooking() {
        serviceAPI = RetrofitClient.getClient().create(ServiceAPI.class);
        serviceAPI.addBooking(booking).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    showResultDialog(true);
                }
                else
                {
                    showResultDialog(false);
                    String errorBody = null;
                    try {
                        errorBody = response.errorBody().string();
                        Log.e("API RESPONSE", "Lỗi: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showResultDialog(false);
                Log.d("API ERROR", "Không thể đặt: " + t.getMessage());
            }
        });
    }

    private void showResultDialog(boolean isSuccess) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if (isSuccess && !booking.isEmpty()) {
            builder.setTitle("Đã đặt sân 🎉");
            builder.setMessage("Bạn đã đặt sân thành công");
        } else {
            builder.setTitle("Thất bại ❌");
            builder.setMessage("Vui lòng điền đầy đủ thông tin");
        }

        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss(); // Đóng dialog
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}