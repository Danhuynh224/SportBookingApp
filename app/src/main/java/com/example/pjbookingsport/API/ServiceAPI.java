//Nguyễn Phan Minh Trí - 22110443
package com.example.pjbookingsport.API;


import com.example.pjbookingsport.model.Post;
import com.example.pjbookingsport.model.SportFacility;
import com.example.pjbookingsport.model.SubFacility;

import java.math.BigDecimal;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceAPI {
    @GET("/sportsfacilities")
    Call<List<SportFacility>> getAllSportFacility();
    @GET("/sportsfacilities/search")
    Call<List<SportFacility>> getSportsFacilities(@Query("keyword") String keyword);

    @GET("/sportsfacilities/filter")
    Call<List<SportFacility>> filterSportsFacilities(
            @Query("types") List<String> types,
            @Query("addresses") String addresses,
            @Query("minPrice") BigDecimal minPrice,
            @Query("maxPrice") BigDecimal maxPrice
    );

    @GET("/subfacilities")
    Call<List<SubFacility>> getSubFaByFaId(@Query("faId") Long faid);

    @GET("/post")
    Call<List<Post>> getAllPosts();

    @GET("/images/post/{id}")
    Call<List<String>> getPostImageUrls(@Path("id") Long postId);
}
