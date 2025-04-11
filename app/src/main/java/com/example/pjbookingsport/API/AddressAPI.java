package com.example.pjbookingsport.API;

import com.example.pjbookingsport.model.ProvinceResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AddressAPI {
    @GET("provinces")
    Call<ProvinceResponse> getProvinces();
}
