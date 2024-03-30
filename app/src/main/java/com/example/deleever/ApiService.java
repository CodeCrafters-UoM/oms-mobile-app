package com.example.deleever;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("orders")
    Call<List<Order_card>> getItems();
}
