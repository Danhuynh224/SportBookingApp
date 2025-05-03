package com.example.pjbookingsport.frag;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pjbookingsport.API.RetrofitClient;
import com.example.pjbookingsport.API.ServiceAPI;
import com.example.pjbookingsport.activity.LoginActivity;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.model.Account;
import com.example.pjbookingsport.sharedPreferences.SharedPreferencesHelper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalAccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Account newAccount;

    private TextView textErrorOldPass, textErrorNewPass, textErrorConfirmPass;
    private EditText txtLoginName, txtPassword, txtNewPass, txtPassConfirm;
    private Button updateBtn, logOutBtn;
    private ServiceAPI serviceAPI;
    Account account;
    public PersonalAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalAccountFragment newInstance(String param1, String param2) {
        PersonalAccountFragment fragment = new PersonalAccountFragment();
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
        return inflater.inflate(R.layout.fragment_personal_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        account = SharedPreferencesHelper.getAccount(getContext());
        txtLoginName = view.findViewById(R.id.txtLoginName);
        txtPassword = view.findViewById(R.id.txtPassword);
        txtNewPass =  view.findViewById(R.id.txtNewPass);
        txtPassConfirm = view.findViewById(R.id.txtPassConfirm);
        textErrorNewPass = view.findViewById(R.id.textErrorNewPass);
        textErrorOldPass = view.findViewById(R.id.textErrorOldPass);
        textErrorConfirmPass = view.findViewById(R.id.textErrorConfirmPass);
        updateBtn = view.findViewById(R.id.updateBtn);
        logOutBtn = view.findViewById(R.id.logOutBtn);
        txtLoginName.setText(account.getUsername());

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean canUpdate = true;
                newAccount = new Account(txtLoginName.getText().toString(), txtNewPass.getText().toString());
                if(!account.getPassword().equals(txtPassword.getText().toString())){
                    textErrorOldPass.setText("Mật khẩu cũ nhập lại không đúng");
                    canUpdate=false;
                }
                else {
                    textErrorOldPass.setText("");
                }
                if(newAccount.getPassword().length()<=6)
                {
                    textErrorNewPass.setText("Mật khẩu phải hơn 6 kí tự");
                    canUpdate = false;
                }
                else {
                    textErrorNewPass.setText("");
                }
                if(!newAccount.getPassword().equals(txtPassConfirm.getText().toString()))
                {
                    textErrorConfirmPass.setText("Mật khẩu xác nhận lại không khớp");
                    canUpdate = false;
                }
                else {
                    textErrorConfirmPass.setText("");
                }
                if(canUpdate) {
                    serviceAPI = RetrofitClient.getClient().create(ServiceAPI.class);
                    serviceAPI.updateAccount(newAccount).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            showResultDialog(response.isSuccessful());
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }
        });

        logOutBtn.setOnClickListener(view1 -> showLogoutDialog());
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
            SharedPreferencesHelper.saveAccount(getContext(),newAccount);
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
            SharedPreferencesHelper.clearAccount(requireContext());
            SharedPreferencesHelper.clearUser(requireContext());
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            dialog.dismiss();
        });

        dialog.show();
    }
}