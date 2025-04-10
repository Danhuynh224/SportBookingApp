// Huỳnh Việt Đan - 22110306
package com.example.pjbookingsport.API;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;

    // Lấy Retrofit client
    public static Retrofit getClient() {

        if (retrofit == null) {
            // Tạo interceptor để log request và response
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // Log cả body

            // Tạo OkHttpClient với interceptor
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor) // Thêm interceptor vào client
                    .build();

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss") // dùng cho java.util.Date
                    .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter()) // dùng cho java.time.LocalTime
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()) // nếu cần luôn LocalDate
                    .create();

            // Khởi tạo Retrofit client
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://localhost:8080/")  // Địa chỉ API
                    .addConverterFactory(GsonConverterFactory.create(gson)) // Chuyển đổi Gson
                    .client(client) // Thêm OkHttpClient vào Retrofit
                    .build();
        }

        return retrofit;
    }
}

