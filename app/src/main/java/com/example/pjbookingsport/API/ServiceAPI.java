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
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceAPI {
    @GET("/sportsfacilities")
    Call<List<SportFacility>> getAllSportFacility();

    //Guest
    @GET("/sportsfacilities/nearby")
    Call<List<SportFacility>> getSportFacilityNearMe(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude
    );
    //Guest
    @GET("/sportsfacilities/search")
    Call<List<SportFacility>> getSportsFacilities(@Query("keyword") String keyword);
    //Guest
    @GET("/sportsfacilities/filter")
    Call<List<SportFacility>> filterSportsFacilities(
            @Query("types") List<String> types,
            @Query("address") String address,
            @Query("minRating") int minRating
    );
    //Guest
    @GET("/subfacilities")
    Call<List<SubFacility>> getSubFaByFaId(@Query("faId") Long faid);
    //Booking
    @POST("/booking/add")
    Call<Booking> addBooking(@Header("Authorization") String authHeader, @Body Booking booking);
    @GET("/booking/getByDateAndTypeAndSubFa")
    Call<List<Booking>> getByDateAndTypeAndSubFa(@Header("Authorization") String authHeader, @Query("bookingDate") LocalDate bookingDate, @Query("facilityTypeId") Long facilityTypeId, @Query("subFacilityId") Long subFacilityId);
    //Guest
    @GET("/post")
    Call<List<Post>> getAllPosts();
    @POST("/user/update")
    Call<ResponseBody> updateUser(@Header("Authorization") String authHeader,@Body User user);
    @POST("/account/update")
    Call<ResponseBody> updateAccount(@Header("Authorization") String authHeader,@Body Account account);
    @GET("/booking/user/{userId}")
    Call<List<Booking>> getBookingsByUserId(@Header("Authorization") String authHeader,@Path("userId") Long userId);
    @POST("review/save")
    Call<ResponseBody> saveReview(@Header("Authorization") String authHeader,@Body ReviewRequest reviewRequest);

    @GET("/payment/create")
    Call<String> createPayment(@Header("Authorization") String authHeader,@Query("price") Long price);

    @PUT("/booking/{bookingId}/updateStatus")
    Call<Booking> updateBookingStatus(@Header("Authorization") String authHeader,@Path("bookingId") Long bookingId);

    @DELETE("/booking/delete/{bookingId}")
    Call<Void> deleteBooking(@Header("Authorization") String authHeader,@Path("bookingId") Long bookingId);

    @GET("/user/getUserByMail")
    Call<User> getUserByMail(@Header("Authorization") String authHeader,@Query("mail") String mail);
}
