//Nguyễn Phan Minh Trí - 22110443
package com.example.pjbookingsport.API;


import com.example.pjbookingsport.model.Account;
import com.example.pjbookingsport.model.Booking;
import com.example.pjbookingsport.model.Post;
import com.example.pjbookingsport.model.Review;
import com.example.pjbookingsport.model.ReviewRequest;
import com.example.pjbookingsport.model.SportFacility;
import com.example.pjbookingsport.model.SubFacility;
import com.example.pjbookingsport.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceAPI {
    @GET("/sportsfacilities")
    Call<List<SportFacility>> getAllSportFacility();

    @GET("/sportsfacilities/nearby")
    Call<List<SportFacility>> getSportFacilityNearMe(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude
    );
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
    //Booking
    @POST("/booking/add")
    Call<ResponseBody> addBooking(@Body Booking booking);
    @GET("/booking/getByDateAndTypeAndSubFa")
    Call<List<Booking>> getByDateAndTypeAndSubFa(@Query("bookingDate") LocalDate bookingDate, @Query("facilityTypeId") Long facilityTypeId, @Query("subFacilityId") Long subFacilityId);
    @GET("/post")
    Call<List<Post>> getAllPosts();
    @POST("/user/update")
    Call<ResponseBody> updateUser(@Body User user);
    @POST("/account/update")
    Call<ResponseBody> updateAccount(@Body Account account);
    @GET("/booking/user/{userId}")
    Call<List<Booking>> getBookingsByUserId(@Path("userId") Long userId);
    @POST("review/save")
    Call<ResponseBody> saveReview(@Body ReviewRequest reviewRequest);

}
