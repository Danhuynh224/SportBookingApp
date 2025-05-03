package com.example.pjbookingsport.API;

import com.example.pjbookingsport.model.DistrictResponse;
import com.example.pjbookingsport.model.ProvinceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AddressAPI {
    @GET("api/v2/province")
    Call<ProvinceResponse> getProvinces();
    @GET("api/v2/province/district/{province_id}")
    Call<DistrictResponse> getDistricts(@Path("province_id") String provinceId);
}
