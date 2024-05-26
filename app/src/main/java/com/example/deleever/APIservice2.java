package com.example.deleever;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIservice2 {

    @POST("login")
    Call<LoginResponse> LogIn(
            @Body LoginRequest loginRequest
    );

    @GET("products")
    Call<List<com.example.deleever.Product>> getProducts();


    @GET("products/{productCode}")
    Call<com.example.deleever.Product> getProductDetails(@Path("productCode") String productCode);

    @DELETE("product/{productCode}")
    Call<Void> deleteProduct(@Path("productCode") String productCode);

    @PUT("products/{productCode}")
    Call<Void> updateProduct(@Path("productCode") String productCode, @Body com.example.deleever.Product product);
}