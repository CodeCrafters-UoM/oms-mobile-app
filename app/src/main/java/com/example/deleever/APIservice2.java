package com.example.deleever;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIservice2 {

    @POST("api/v1/login")
    Call<LoginResponse> LogIn(
            @Body LoginRequest loginRequest
    );

    @GET("products")
    Call<List<Product>> getProducts(@Header("Authorization") String authToken);

    @GET("orderlinks")
    Call<List<OrderLink>> getAllOrderlinks(@Header("Authorization") String authToken);

    @GET("orderlinks/available")
    Call<List<OrderLink>> getAvailableOrderLinks(@Header("Authorization") String authToken);

    @POST("product/Add_product")
    Call<Product> addProduct(
            @Header("Authorization") String token,
            @Body Product newProduct);


    @GET("products/{productCode}")
    Call<com.example.deleever.Product> getProductDetails(@Path("productCode") String productCode);

    @DELETE("product/{productCode}")
    Call<Void> deleteProduct(
            @Header("Authorization") String token,
            @Path("productCode") String productCode
    );

    @PUT("product/{previousProductCode}")
    Call<Product> updateProduct(
            @Header("Authorization") String token,
            @Path("previousProductCode") String previousProductCode,
            @Body Product product);



}