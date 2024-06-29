package com.example.deleever;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.deleever.constant.Constant.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Customer_details_screen extends AppCompatActivity {
    ListView customer_details_list;
    private String jwtToken,updateStatus;
    TextView ordersAll,returnOrdersAll,ordersMy,returnOrdersMy,customer_details_screen_name_data,customer_details_address_data,customer_details_contact_data,customer_details_to_home;
    String orders_All,return_orders_All,orders_My,return_orders_My;
    String customer_name_data,customer_address_data,customer_contact_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details_screen);
        jwtToken = getIntent().getStringExtra("jwtToken");

        int id = Integer.parseInt(getIntent().getStringExtra("orderId"));
        Log.d(TAG, "update status:id  " + id);



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create ApiService instance
        ApiService apiService = retrofit.create(ApiService.class);

        // Fetch items from the backend
        Call<List<Order_card>> call = apiService.getItems("Bearer " + jwtToken);


//


//
//        orders_All =getIntent().getStringExtra("orders(All)");
        ordersAll = findViewById(R.id.orders_All);
//        ordersAll.setText(String.valueOf(orders_All));
//
//        return_orders_All = getIntent().getStringExtra("return(All)");
        returnOrdersAll = findViewById(R.id.return_orders_All);
//        returnOrdersAll.setText(String.valueOf(return_orders_All));
//        Log.d(TAG, "return_orders_All: " + return_orders_All);
//
//        orders_My = getIntent().getStringExtra("orders(my)");
        ordersMy = findViewById(R.id.orders_My);
//        ordersMy.setText(String.valueOf(orders_My));
//
//        return_orders_My = getIntent().getStringExtra("return(my)");
        returnOrdersMy = findViewById(R.id.return_orders_My);
//        returnOrdersMy.setText(String.valueOf(return_orders_My));
//
//        customer_name_data = getIntent().getStringExtra("name");
        customer_details_screen_name_data = findViewById(R.id.customer_details_screen_name_data);
//        customer_details_screen_name_data.setText(customer_name_data);
//
//
//        customer_address_data = getIntent().getStringExtra("contact");
        customer_details_address_data = findViewById(R.id.customer_details_address_data);
//        customer_details_address_data.setText(customer_address_data);
//
        customer_details_contact_data = findViewById(R.id.customer_details_contact_data);
        call.enqueue(new Callback<List<Order_card>>() {
            @Override
            public void onResponse(Call<List<Order_card>> call, Response<List<Order_card>> response) {
                if (response.isSuccessful()) {
                    List<Order_card> order_cards = response.body();
                    if(order_cards != null){
                        for(Order_card orderCard :order_cards ){
                            if(orderCard.getOrderId()==id){
                                Log.d(TAG, "update status:id2  " + id);
                                customer_name_data  =orderCard.getCustomer().getFirstName()   ;
                                customer_details_screen_name_data.setText(customer_name_data);

                                customer_contact_data  =orderCard.getCustomer().getContactNumber();
                                customer_details_contact_data.setText(customer_contact_data);
                                customer_address_data = orderCard.getDeliveryAddress();
                                customer_details_address_data.setText(customer_address_data);

                                orders_All = String.valueOf(orderCard.getTotalOrdersForCustomer());
                                ordersAll.setText(orders_All);

                                return_orders_All =String.valueOf(orderCard.getTotalReturnOrdersForCustomer());
                                returnOrdersAll.setText(return_orders_All);

                                return_orders_My=String.valueOf(orderCard.getTotalReturnOrdersForCustomerForSeller());
                                returnOrdersMy.setText(return_orders_My);

                                orders_My = String.valueOf(orderCard.getTotalOrdersForCustomerForSeller());
                                ordersMy.setText(orders_My);




                            }
                        }

                    }

                } else {
                    Log.d(TAG, "Response unsuccessful. Code: " + response.code());
                    // Add more logging for debugging
                    try {
                        Log.d(TAG, "Error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(Customer_details_screen.this, "Response unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order_card>> call, Throwable t) {
                Log.e(TAG, "Error fetching orders", t);
                Toast.makeText(Customer_details_screen.this, "Error fetching orders", Toast.LENGTH_SHORT).show();
            }
        });


        customer_details_to_home = findViewById(R.id.custmer_details_to_home);
        customer_details_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent custmer_details_to_home = new Intent(getApplicationContext(), MainActivity.class);
                custmer_details_to_home.putExtra("jwtToken", jwtToken);
                startActivity(custmer_details_to_home);}
        });

    }
}
