package com.example.deleever;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIservice2 {

    @POST("login")
    Call<LoginResponse> LogIn(
            @Body LoginRequest loginRequest
    );

    @GET("products")
    Call<List<Product>> getProducts(@Header("Authorization") String authToken);

    @GET("orders")
    Call<Boolean> getOrders(@Header("Authorization") String authToken);

    @GET("orderlinks")
    Call<List<OrderLink>> getAllOrderlinks(@Header("Authorization") String authToken);

    @GET("orderlinks/available")
    Call<List<OrderLink>> getAvailableOrderLinks(@Header("Authorization") String authToken);

    @POST("product/addProduct")
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

    @GET("profile/viewprofile/{userId}") // Replace with your actual endpoint
    Call<ProfileResponse> getProfile(@Header("Authorization") String token, @Path("userId") String userId);


    @PUT("profile/updateprofile/{userId}")
    Call<ProfileResponse> updateProfile(@Header("Authorization") String token,
                                        @Path("userId") String userId,
                                        @Body ProfileResponse profile);

    @PUT("product/{previousProductCode}")
    Call<Product> updateProduct(
            @Header("Authorization") String token,
            @Path("previousProductCode") String previousProductCode,
            @Body Product product);

    @POST("contactus")
    Call<ContactUsRequest> sendContactUs(@Header("Authorization") String authToken, @Body ContactUsRequest request);

    @POST("forgotpassword")
    Call<Void> sendUserName(@Body forgot_password.ForgotPasswordRequest request);

    @POST("verifyotp")
    Call<Void> VerifyOTP(@Body VerifyOTP.verifyOtpRequest reqOTP);

    @POST("resetpassword") // Adjust to your actual endpoint
    Call<Void> resetPassword(@Body resetPassword.resetPasswordRequest resetPass);



}