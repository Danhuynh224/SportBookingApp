package com.example.pjbookingsport;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pjbookingsport.API.RetrofitClient;
import com.example.pjbookingsport.API.ServiceAPI;
import com.example.pjbookingsport.model.Booking;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PaymentVNPayActivity extends AppCompatActivity {

    private WebView webView;
    Booking booking;
    ServiceAPI serviceAPI;
    private static final String BASE_URL = "http://10.0.2.2:8080/"; // Nếu dùng Emulator

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_vnpay);

        booking  = (Booking) getIntent().getSerializableExtra("booking");


        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Thêm logic xử lý sau khi trang web tải xong
            }
        });

        // Gọi API để tạo thanh toán và lấy URL thanh toán
        createPayment();
    }

    private void createPayment() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create()) // Nhận String response
                .build();

        ServiceAPI paymentService = retrofit.create(ServiceAPI.class);

        Call<String> call = paymentService.createPayment(booking.getTotalPrice().longValue());
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    loadPaymentPage(response.body());
                } else {
                    Toast.makeText(PaymentVNPayActivity.this, "Lỗi nhận URL thanh toán", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(PaymentVNPayActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("lỗi API", t.getMessage());
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("IntentData", "onNewIntent called");

        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            Log.d("IntentData", "URI received: " + uri);

            if (uri != null && "myapp".equals(uri.getScheme()) && "payment".equals(uri.getHost())) {
                String transactionStatus = uri.getQueryParameter("vnp_TransactionStatus");
                Log.d("IntentData", "Transaction Status: " + transactionStatus);

                if ("00".equals(transactionStatus)) {
                    Log.d("PaymentStatus", "Thanh toán thành công");
                    Toast.makeText(this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                    completeBooking();
                    // gửi object
                    Intent successIntent = new Intent(this, SuccessActivity.class);
                    successIntent.putExtra("booking", booking);
                    startActivity(successIntent);
                    finish();
                } else {
                    serviceAPI = RetrofitClient.getClient().create(ServiceAPI.class);
                    serviceAPI.deleteBooking(booking.getBookingId()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                // Xóa thành công
                                Log.d("Booking", "Delete thành công!");

                            } else {
                                // Booking không tồn tại hoặc lỗi khác
                                Log.d("Booking", "Không tìm thấy booking hoặc lỗi: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            // Lỗi mạng, server chết,...
                            Log.e("Booking", "Lỗi gọi API", t);
                        }
                    });
                    // Quay về activity trước đó (PreviousActivity) và giữ nguyên fragment và dữ liệu
                    Intent previousIntent = new Intent(this, MainActivity.class);
                    previousIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); // Quay về mà không tạo mới activity
                    startActivity(previousIntent);
                    finish(); // Đóng FailedActivity


                }
            }

        }
    }


    private void loadPaymentPage(String url) {
        Log.d("URL", url);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Đặt WebViewClient để xử lý các URL từ WebView
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("myapp://")) {
                    // Nếu URL bắt đầu với custom scheme, xử lý nó
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url)).setClassName(/* TODO: provide the application ID. For example: */ getPackageName(), "com.example.pjbookingsport.PaymentVNPayActivity");
                    startActivity(intent);  // Chuyển sang màn hình xử lý custom scheme
                    return true;  // Tránh việc mở URL trong WebView
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        webView.loadUrl(url);
    }

    private void completeBooking() {
        serviceAPI = RetrofitClient.getClient().create(ServiceAPI.class);
        serviceAPI.updateBookingStatus(booking.getBookingId()).enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if(response.isSuccessful()){
                }
                else
                {
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
            public void onFailure(Call<Booking> call, Throwable t) {
                Log.d("API ERROR", "Không thể đặt: " + t.getMessage());
            }
        });
    }

}
