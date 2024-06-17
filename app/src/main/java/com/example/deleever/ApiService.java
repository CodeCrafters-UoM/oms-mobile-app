package com.example.deleever;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Path;

public interface ApiService {
    @GET("orders")
    Call<List<Order_card>> getItems(@Header("Authorization") String authToken);

    @GET("orders/{orderId}")
    Call<List<Order_card>> getOrderDetails(@Path("orderId") int orderId);

    @GET("orderlinks")
    Call<List<OrderLinkModel>> getOrderLinks(@Header("Authorization") String authToken);

    @GET("reports")
    Call<List<ReportCard>> getReportDetails(@Header("Authorization") String authToken);


    @POST("api/v1/register")
    Call<Signup.RegisterResponse> createPost(@Body Signup.DataModal dataModal);

    @PUT("orders")
    Call<Void> updateStatus(@Body UpdateStatus updateStatus, @Header("Authorization") String authToken);

    @DELETE("orderlinks/delete/{id}")
    Call<Void> deleteOrderLink(@Header("Authorization") String authToken, @Path("id") String id);
}



