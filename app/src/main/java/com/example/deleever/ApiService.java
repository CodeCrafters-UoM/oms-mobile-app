package com.example.deleever;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Path;



public interface ApiService {
    @GET("orders")
    Call<List<Order_card>> getItems();

    @GET("orders/{orderId}")
    Call<List<Order_card>> getOrderDetails(@Path("orderId") int orderId);

}
