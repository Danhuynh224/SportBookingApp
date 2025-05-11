package com.example.pjbookingsport.API;

import com.example.pjbookingsport.model.DistrictResponse;
import com.example.pjbookingsport.model.ProvinceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AddressAPI {
    @GET("1/0.htm")
    Call<ProvinceResponse> getProvinces();
    @GET("2/{province_id}.htm")
    Call<DistrictResponse> getDistricts(@Path("province_id") String provinceId);
}
