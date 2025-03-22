//Nguyễn Phan Minh Trí - 22110443
package com.example.pjbookingsport.API;


import com.example.pjbookingsport.model.SportFacility;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ServiceAPI {
    @GET("/sportsfacilities")
    Call<List<SportFacility>> getAllSportFacility();
//    @GET("/api/category/all-categories")
//    Call<List<Category>> getCategoriesAll();
//    @GET("/api/product/last-products")
//    Call<List<Product>> getLastProducts();
//    @GET("/api/product/get-products-by-category/{categoryId}") // Sửa lỗi API
//    Call<List<Product>> getProductByCategory(@Path("categoryId") Long categoryId); // Sửa từ @Query thành @Path
}
