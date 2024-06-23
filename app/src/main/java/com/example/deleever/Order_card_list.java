package com.example.deleever;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView; // Correct import for SearchView

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.example.deleever.constant.Constant.*;


public class Order_card_list extends AppCompatActivity implements Order_card_list_interface {

    private RecyclerView recyclerView;
    private Order_cards_adapter orderCardAdapter;
    private List<Order_card> orderCards = new ArrayList<>(); // Initialize the list
    private SearchView search_order_list;
    private String jwtToken;
    private String sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_card_list);
        jwtToken = getIntent().getStringExtra("jwtToken");
        sellerId = getIntent().getStringExtra("sellerid");
        setValues();
        fetchOrderDetails();

        search_order_list = findViewById(R.id.search_order_list);

        search_order_list.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void fetchOrderDetails() {
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
                        orderCards = order_cards; // Update the orderCards list
                        orderCardAdapter.setItems(orderCards);
                    } else {
                        Log.d(TAG, "Order list is empty");
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
        i.putExtra("status", orderCardAdapter.order_cards.get(position).getStatus());
        i.putExtra("orderId", String.valueOf(orderCardAdapter.order_cards.get(position).getOrderId()));
        i.putExtra("productCode", orderCardAdapter.order_cards.get(position).getProduct().getProductCode());
        i.putExtra("description", orderCardAdapter.order_cards.get(position).getProduct().getDescription());
        i.putExtra("quantity", String.valueOf(orderCardAdapter.order_cards.get(position).getQuantity()));
        i.putExtra("name", orderCardAdapter.order_cards.get(position).getCustomer().getFirstName() + " " + orderCardAdapter.order_cards.get(position).getCustomer().getLastName());
        i.putExtra("address", orderCardAdapter.order_cards.get(position).getDeliveryAddress());
        i.putExtra("contact", orderCardAdapter.order_cards.get(position).getCustomer().getContactNumber());
        i.putExtra("paymentMethod", orderCardAdapter.order_cards.get(position).getPaymentMethod());
        i.putExtra("price", String.valueOf(orderCardAdapter.order_cards.get(position).getProduct().getPrice()));
        i.putExtra("orders(All)",String.valueOf(orderCardAdapter.order_cards.get(position).getTotalOrdersForCustomer()));
        i.putExtra("return(All)",String.valueOf(orderCardAdapter.order_cards.get(position).getTotalReturnOrdersForCustomer()));
        i.putExtra("orders(my)",String.valueOf(orderCardAdapter.order_cards.get(position).getTotalOrdersForCustomerForSeller()));
        i.putExtra("return(my)",String.valueOf(orderCardAdapter.order_cards.get(position).getTotalReturnOrdersForCustomerForSeller()));


        i.putExtra("jwtToken", jwtToken);
        startActivity(i);
    }

    void filter(String newText) {
        List<Order_card> filteredList = new ArrayList<>();
        for (Order_card item : orderCards) {
            if (item.getCustomer().getFirstName().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }
            if (item.getCustomer().getLastName().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }
        }
        orderCardAdapter.filterList(filteredList);
}
}
