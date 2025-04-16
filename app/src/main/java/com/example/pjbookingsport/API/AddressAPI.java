package com.example.pjbookingsport.API;

import com.example.pjbookingsport.model.DistrictResponse;
import com.example.pjbookingsport.model.ProvinceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AddressAPI {
    @GET("provinces")
    Call<ProvinceResponse> getProvinces();
    @GET("districts/{provinceId}")
    Call<DistrictResponse> getDistricts(@Path("provinceId") String provinceId);
}
