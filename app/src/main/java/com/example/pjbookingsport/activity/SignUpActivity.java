package com.example.pjbookingsport.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pjbookingsport.API.AuthAPI;
import com.example.pjbookingsport.API.RetrofitClient;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.model.RegisterRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    EditText textUsername, textEmail, textPass, textPassConfirm;
    Button btnSignUp;
    TextView textViewSignIn, textErrorEmail, textErrorPass, textErrorPassConfirm, textErrorName ;
    String userName, email, password, passConfirm;
    AuthAPI authAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textUsername = findViewById(R.id.textUsername);
        textEmail = findViewById(R.id.textEmail);
        textPass = findViewById(R.id.textPass);
        textPassConfirm = findViewById(R.id.textPassConfirm);
        btnSignUp = findViewById(R.id.btnSignUp);
        textViewSignIn = findViewById(R.id.textViewSignIn);
        textErrorEmail = findViewById(R.id.textErrorEmail);
        textErrorPass = findViewById(R.id.textErrorPass);
        textErrorPassConfirm = findViewById(R.id.textErrorPassConfirm);
        textErrorName = findViewById(R.id.textErrorName);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean canRegis = true;
                userName = textUsername.getText().toString();
                email = textEmail.getText().toString();
                password = textPass.getText().toString();
                passConfirm = textPassConfirm.getText().toString();
                if(userName.isEmpty()){
                    textErrorName.setText("Vui lòng không để trống tên đăng nhập");
                    canRegis=false;
                }
                else {
                    textErrorName.setText("");
                }
                if(!isValidEmail(email)){
                    textErrorEmail.setText("Email sai cú pháp");
                    canRegis=false;
                }
                else{
                    textErrorEmail.setText("");
                }
                if(password.length()<=6){
                    textErrorPass.setText("Mật khẩu phải hơn 6 kí tự");
                    canRegis=false;
                }
                else{
                    textErrorPass.setText("");
                }
                if (!passConfirm.equals(password) ){
                    Log.d("SO SANH", passConfirm + " và " + password);
                    textErrorPassConfirm.setText("Mật khẩu xác nhận không khớp");
                    canRegis=false;
                }
                else {
                    textErrorPassConfirm.setText("");
                }

                if(canRegis){
                    RegisterRequest registerRequest = new RegisterRequest(userName, password, email);
                    authAPI = RetrofitClient.getClient().create(AuthAPI.class);
                    authAPI.register(registerRequest).enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            showResultDialog(response.isSuccessful());
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d("API ERROR", t.getMessage());
                        }
                    });
                }
            }
        });
    }
    public static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(regex);
    }

    private void showResultDialog(boolean isSuccess) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);

        if (isSuccess) {
            builder.setTitle("Đăng ký thành công");
            builder.setMessage("Bạn đã đăng ký thành công chúc bạn có trãi nghiệm vui");
        } else {
            builder.setTitle("Thất bại ❌");
            builder.setMessage("Email và tên đăng nhập đã được dùng");
        }

        builder.setPositiveButton("OK", (dialog, which) -> {
            if(isSuccess) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            dialog.dismiss(); // Đóng dialog
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}