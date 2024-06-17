package com.example.deleever;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Order_card_list extends AppCompatActivity implements Order_card_list_interface {
    private static final String IP_ADDRESS = "192.168.91.146";
    private static final String BASE_URL = "http://" + IP_ADDRESS + ":8000/";
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private Order_cards_adapter orderCardAdapter;
    List<Order_card> orderCards;
    SearchView search_order_list;
    private String jwtToken;
    private String sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_card_list);
        jwtToken = getIntent().getStringExtra("jwtToken");
        sellerId = getIntent().getStringExtra("sellerid");
        setValues();

        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create ApiService instance
        ApiService apiService = retrofit.create(ApiService.class);

        // Fetch items from the backend
        Call<List<Order_card>> call = apiService.getItems("Bearer " + jwtToken);
        call.enqueue(new Callback<List<Order_card>>() {
            @Override
            public void onResponse(Call<List<Order_card>> call, Response<List<Order_card>> response) {
                if (response.isSuccessful()) {
                    List<Order_card> order_cards = response.body();
                    if (order_cards != null) {
                        orderCardAdapter.setItems(order_cards);
                        Log.d(TAG, "Items received: " + order_cards.size());
                        for (Order_card orderCard : order_cards) {
                            Log.d(TAG, "Order ID: " + orderCard.getOrderId());
                            Log.d(TAG, "Status: " + orderCard.getStatus());
                            Log.d(TAG, "Delivery Address: " + orderCard.getDeliveryAddress());
                            Log.d(TAG, "Description: " + orderCard.getProduct().getDescription());
                            Log.d(TAG, "Quantity: " + orderCard.getQuantity());
                            Log.d(TAG, "Payment Method: " + orderCard.getPaymentMethod());
                            Log.d(TAG, "Customer Name: " + orderCard.getCustomer().getFirstName() + " " + orderCard.getCustomer().getLastName());
                            Log.d(TAG, "Customer Contact: " + orderCard.getCustomer().getContactNumber());
                            Log.d(TAG, "Product Code: " + orderCard.getProduct().getProductCode());
                            Log.d(TAG, "Product Name: " + orderCard.getProduct().getName());
                            Log.d(TAG, "Product Price: " + orderCard.getProduct().getPrice());
                            Log.d(TAG, "Order Date: " + orderCard.getDateAndTime());
                        }

                    } else {
                        Log.d(TAG, "Response body is null");
                        Toast.makeText(Order_card_list.this, "Order list is empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "Response unsuccessful. Code: " + response.code());
                    // Add more logging for debugging
                    try {
                        Log.d(TAG, "Error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(Order_card_list.this, "Response unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order_card>> call, Throwable t) {
                System.out.println(t);
                Log.e(TAG, "Error fetching orders", t);
                Toast.makeText(Order_card_list.this, "Error fetching orders", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setValues() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderCardAdapter = new Order_cards_adapter(this);
        recyclerView.setAdapter(orderCardAdapter);
    }

    @Override
    public void OnItemClick(int position) {
        Intent i = new Intent(Order_card_list.this, Order_summery.class);
//        Order_card currentOrder_card = orderCardAdapter.order_cards.get(position);


        i.putExtra("status", orderCardAdapter.order_cards.get(position).getStatus());

        i.putExtra("orderId", String.valueOf(orderCardAdapter.order_cards.get(position).getOrderId()));
        i.putExtra("productCode", orderCardAdapter.order_cards.get(position).getProduct().getProductCode());
        i.putExtra("description", orderCardAdapter.order_cards.get(position).getProduct().getDescription());
        i.putExtra("quantity",String.valueOf(orderCardAdapter.order_cards.get(position).getQuantity()));

        i.putExtra("name", orderCardAdapter.order_cards.get(position).getCustomer().getFirstName());
        i.putExtra("address", orderCardAdapter.order_cards.get(position).getDeliveryAddress());
        i.putExtra("contact", orderCardAdapter.order_cards.get(position).getCustomer().getContactNumber());

        i.putExtra("paymentMethod",orderCardAdapter.order_cards.get(position).getPaymentMethod());
        i.putExtra("price",String.valueOf(orderCardAdapter.order_cards.get(position).getProduct().getPrice()));
        i.putExtra("jwtToken", jwtToken);

        startActivity(i);
    }
}