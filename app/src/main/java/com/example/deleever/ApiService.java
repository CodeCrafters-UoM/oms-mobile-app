package com.example.deleever;

import android.telecom.Call;

import java.util.List;
import retrofit.http.GET;
public interface ApiService {
    @GET("orders")
    Call<List<Order_card>> getItems();

    @GET("orders/{orderId}")
    Call<List<Order_card>> getOrderDetails(@Path("orderId") int orderId);
}
