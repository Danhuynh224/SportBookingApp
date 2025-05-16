package com.example.pjbookingsport.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pjbookingsport.API.AuthAPI;
import com.example.pjbookingsport.API.RetrofitClient;
import com.example.pjbookingsport.API.ServiceAPI;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.model.Account;
import com.example.pjbookingsport.model.AuthRequest;
import com.example.pjbookingsport.model.JWT;
import com.example.pjbookingsport.model.User;
import com.example.pjbookingsport.sharedPreferences.SharedPreferencesHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText textUsername, textPassword;
    TextView  textViewSignUp;
    CheckBox checkRemember;
    Button btnSignIn;
    String username, password;
    User saveUser;
    AuthAPI authAPI;
    ServiceAPI apiService;
    JWT jwt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        textUsername = findViewById(R.id.textUsername);
        textPassword = findViewById(R.id.textPassword);
        textViewSignUp = findViewById(R.id.textViewSignUp);
        checkRemember = findViewById(R.id.checkRemember);
        btnSignIn = findViewById(R.id.btnSignIn);
        saveUser = SharedPreferencesHelper.getUser(this);
        Account acc = SharedPreferencesHelper.getAccount(this);
        if(saveUser!=null )
        {
            checkRemember.setChecked(saveUser.isSave());
            textUsername.setText(acc.getUsername());
            textPassword.setText(acc.getPassword());
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = String.valueOf(textUsername.getText());
                password = String.valueOf(textPassword.getText());
                if(username.isEmpty() || password.isEmpty()){
                    showResultDialog("Vui lòng nhập đầy đủ thông tin");
                }
                else{
                    AuthRequest authRequest = new AuthRequest(username, password);
                    authAPI = RetrofitClient.getClient().create(AuthAPI.class);
                    authAPI.login(authRequest).enqueue(new Callback<JWT>() {
                        @Override
                        public void onResponse(Call<JWT> call, Response<JWT> response) {
                            if(response.isSuccessful()){
                                jwt = response.body();
                                SharedPreferencesHelper.saveJWT(LoginActivity.this, jwt);
                                SharedPreferencesHelper.saveAccount(LoginActivity.this,new Account(username,password));
                                getInfoUser(checkRemember.isChecked());
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else {
                                showResultDialog("Tài khoản hoặc mật khẩu không đúng");
                            }
                        }

                        @Override
                        public void onFailure(Call<JWT> call, Throwable t) {
                            Log.d("API ERROR", "Không kết nối API");
                        }
                    });

                }

            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        textPassword.setOnTouchListener(new View.OnTouchListener() {
            private long touchStartTime;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_END = 2; // drawableEnd = index 2

                if (textPassword.getCompoundDrawables()[DRAWABLE_END] != null) {
                    int drawableWidth = textPassword.getCompoundDrawables()[DRAWABLE_END].getBounds().width();
                    int clickAreaStart = textPassword.getWidth() - textPassword.getPaddingEnd() - drawableWidth;

                    if (event.getX() >= clickAreaStart) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                // Bắt đầu giữ → hiện mật khẩu
                                textPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                                // Di chuyển con trỏ về cuối
                                textPassword.setSelection(textPassword.getText().length());
                                return true;

                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // Thả ra → ẩn mật khẩu lại
                                textPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                textPassword.setSelection(textPassword.getText().length());
                                return true;
                        }
                    }
                }
                return false;
            }
        });


    }

    private void getInfoUser(boolean checked) {
        apiService = RetrofitClient.getClient().create(ServiceAPI.class);
        apiService.getUserByMail(jwt.getToken(), jwt.getEmail()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                user.setSave(checked);
                SharedPreferencesHelper.saveUser(LoginActivity.this,user);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("Không lấy đc mail", "onFailure: lỗi k lấy được user");
            }
        });
    }
    private void showResultDialog(String string) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(string);
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss(); // Đóng dialog
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}