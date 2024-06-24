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

public interface ApiService {
    @GET("orders")
    Call<List<Order_card>> getItems(@Header("Authorization") String authToken);

    @GET("orders/{orderId}")
    Call<List<Order_card>> getOrderDetails(@Path("orderId") int orderId);

    @GET("orderlinks")
    Call<List<OrderLinkModel>> getOrderLinks(@Header("Authorization") String authToken);

    @GET("reports")
    Call<ReportCard> getReportDetails(@Header("Authorization") String token);

    @POST("api/v1/register")
    Call<Signup.RegisterResponse> createPost(@Body Signup.DataModal dataModal);

    @PUT("orders")
    Call<Void> updateStatus(@Body UpdateStatus updateStatus, @Header("Authorization") String authToken);

    @DELETE("orderlinks/delete/{id}")
    Call<Void> deleteOrderLink(@Header("Authorization") String authToken, @Path("id") String id);

    @GET("/notifications/read")
    Call<Void> getNotifications(@Header("Authorization") String authToke);

    @POST("/notifications")
    Call<Void> createNotification(@Header("Authorization") String authToken,@Body );

}



