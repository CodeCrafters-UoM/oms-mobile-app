package com.example.deleever;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class Order_card_list extends AppCompatActivity {

     private static final String IP_ADDRESS = "10.10.29.55";

    private static final String BASE_URL = "http://"+IP_ADDRESS+":8000/";

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private Order_cards_adapter orderCardAdapter;
    List<Order_card> orderCards = new ArrayList<>();

    SearchView search_order_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_card_list);

        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create ApiService instance
        ApiService apiService = retrofit.create(ApiService.class);

        // Fetch items from the backend
        Call<List<Order_card>> call = apiService.getItems();
        call.enqueue(new Callback<List<Order_card>>() {
            @Override
            public void onResponse(Call<List<Order_card>> call, Response<List<Order_card>> response) {
                if (response.isSuccessful()) {
                    List<Order_card> order_cards = response.body();
                    if (order_cards != null) {
                        orderCardAdapter.setItems(order_cards);
                        Log.d(TAG, "Items received: " + order_cards.size());
                    } else {
                        Log.d(TAG, "Response body is null");
                    }
                } else {
                    Log.d(TAG, "Response unsuccessful. Code: " + response.code());
                    // Add more logging for debugging
                    try {
                        Log.d(TAG, "Error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(Order_card_list.this, "Failed to fetch items", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Order_card>> call, Throwable t) {
                System.out.println(t);
                Log.e(TAG, "Error fetching items", t);
                Toast.makeText(Order_card_list.this, "Error fetching items", Toast.LENGTH_SHORT).show();
            }
        });



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderCardAdapter = new Order_cards_adapter(new ArrayList<>());
        recyclerView.setAdapter(orderCardAdapter);
        orderCardAdapter.setOnItemClickListener(new Order_cards_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(Order_card order_cards) {

                Intent intent = new Intent(Order_card_list.this, Order_summery.class);
                intent.putExtra("orderCardId",getText(p) ); // Pass any necessary data
                startActivity(intent);
            }
        });

    }
}