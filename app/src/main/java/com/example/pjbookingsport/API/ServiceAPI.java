//Nguyễn Phan Minh Trí - 22110443
package com.example.pjbookingsport.API;


import com.example.pjbookingsport.model.SportFacility;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceAPI {
    @GET("/sportsfacilities")
    Call<List<SportFacility>> getAllSportFacility();
    @GET("/sportsfacilities/search")
    Call<List<SportFacility>> getSportsFacilities(@Query("search") String query);
}
