package com.example.pjbookingsport.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitAddress {
    private static Retrofit retrofit;
    public static Retrofit getClient(){
        if (retrofit == null){
            // Tạo interceptor để log request và response
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // Log cả body

            // Tạo OkHttpClient với interceptor
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)// Thêm interceptor vào client
                    .build();

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss") // dùng cho java.util.Date
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://esgoo.net/api-tinhthanh/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client) // Thêm OkHttpClient vào Retrofit
                    .build();
        }
        return retrofit;
    }
}
