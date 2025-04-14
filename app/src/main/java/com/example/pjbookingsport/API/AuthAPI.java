//Huỳnh Việt Đan - 22110306
package com.example.pjbookingsport.API;


import com.example.pjbookingsport.model.AuthRequest;
import com.example.pjbookingsport.model.RegisterRequest;
import com.example.pjbookingsport.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthAPI {
    @POST("/auth/login")
    Call<User> login(@Body AuthRequest authRequest);

    @POST("/auth/register")
    Call<ResponseBody> register(@Body RegisterRequest authRequest);



}
