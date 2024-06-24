package com.example.deleever;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.annotations.SerializedName;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.example.deleever.constant.Constant.*;


public class Order_summery extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String jwtToken;

    String orderId ;

    TextView order_id,order_description,order_code,quantity,customer_name,customer_address,customer_contact,payment_method,price;

    UpdateStatus updateStatus;
    String[] new_order_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summery);
        String[] order_status = {" ","NEW","ACCEPT","REJECT", "DELIVER", "RETURN"};
        Log.d(TAG, "array old: " + order_status[0]);

        setValues();
        String status =getIntent().getStringExtra("status");
        order_status[0] = status;
        String current_status = status;
        new_order_status = updateCurrentStatus(current_status,order_status);
        dropDown(new_order_status);

//        TextView ordersummery_to_home =findViewById(R.id.ordersummery_to_home);
//        ordersummery_to_home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), Order_list.class);
//                intent.putExtra("jwtToken", jwtToken);
//                startActivity(intent);
//            }
//        });




    }

    private void dropDown(String[] new_order_status) {

        Spinner dropDown = findViewById(R.id.status_dropdown);
        dropDown.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new_order_status);
        Log.d(TAG, "adapter: " + adapter);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setAdapter(adapter);


    }

    private String[] updateCurrentStatus(String currentStatus,String[] order_status) {
        order_status[0] = currentStatus;
        int y = findCurrentStatus(order_status, currentStatus);
        String[] new_order_status =  delete(y, order_status);
        return new_order_status;
    }

    public int findCurrentStatus(String[] order_status ,String currentStatus){
        for(int i=1;i<order_status.length;i++){
            if(currentStatus.equals(order_status[i])){
                return i;
            }
        }
        return -1;
    }

    public String[] delete(int y, String[] order_status) {
        String[] new_order_status = new String[order_status.length - 1];

        int j = 0;
        for (int k = 0; k < order_status.length; k++) {
            if (k == y) {
                continue;
            }
            new_order_status[j] = order_status[k];
            j++;
        }
        return new_order_status;
    }



    private void setValues() {
//        current_status = getIntent().getStringExtra("status");

        jwtToken = getIntent().getStringExtra("jwtToken");

        order_id = findViewById(R.id.Order_id_data);
        order_description = findViewById(R.id.Order_description_data);
        order_code = findViewById(R.id.Order_code_data);
        quantity = findViewById(R.id.quantity_data);
        customer_name = findViewById(R.id.customer_name_data);
        customer_address = findViewById(R.id.customer_address_data);
        customer_contact = findViewById(R.id.customer_contact_data);
        payment_method = findViewById(R.id.payment_method_data);
        price = findViewById(R.id.unit_amount_data);

        orderId = getIntent().getStringExtra("orderId");
        order_id.setText(orderId);
        order_description.setText(getIntent().getStringExtra("description"));
        order_code.setText(getIntent().getStringExtra("productCode"));
        quantity.setText(getIntent().getStringExtra("quantity"));
        customer_name.setText(getIntent().getStringExtra("name"));
        customer_address.setText(getIntent().getStringExtra("address"));
        customer_contact.setText(getIntent().getStringExtra("contact"));
        payment_method.setText(getIntent().getStringExtra("paymentMethod"));
        price.setText(getIntent().getStringExtra("price"));
        int id = Integer.valueOf(orderId);
        TextView Customer_details = findViewById(R.id.Customer_details);

        Customer_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Customer_details_screen.class);
//                fetchOrderDetails(id,intent);
                intent.putExtra("name", getIntent().getStringExtra("name"));
                intent.putExtra("address", getIntent().getStringExtra("address"));
                intent.putExtra("contact", getIntent().getStringExtra("contact"));
                intent.putExtra("jwtToken", jwtToken);
                intent.putExtra("orders(All)",getIntent().getStringExtra("orders(All)"));
                intent.putExtra("orders(my)",getIntent().getStringExtra("orders(my)"));
                intent.putExtra("return(my)",getIntent().getStringExtra("return(my)"));
                intent.putExtra("return(All)",getIntent().getStringExtra("return(All)"));
//

                startActivity(intent);
            }
        });


    }

    private void fetchOrderDetails(int orderId,Intent intent) {
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
                    if(order_cards != null){
                        for(Order_card orderCard :order_cards ){
                            if(orderCard.getOrderId()==orderId){
                                intent.putExtra("name", getIntent().getStringExtra("name"));
                                intent.putExtra("address", getIntent().getStringExtra("address"));
                                intent.putExtra("contact", getIntent().getStringExtra("contact"));
                                intent.putExtra("jwtToken", jwtToken);
                                intent.putExtra("orders(All)",getIntent().getStringExtra("orders(All)"));
                                intent.putExtra("orders(my)",getIntent().getStringExtra("orders(my)"));
                                intent.putExtra("return(my)",getIntent().getStringExtra("return(my)"));
                                intent.putExtra("return(All)",getIntent().getStringExtra("return(All)"));
                                System.out.println(getIntent().getStringExtra("return(All)"));
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
                    Toast.makeText(Order_summery.this, "Response unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order_card>> call, Throwable t) {
                Log.e(TAG, "Error fetching orders", t);
                Toast.makeText(Order_summery.this, "Error fetching orders", Toast.LENGTH_SHORT).show();
            }
        });
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String currentStatus = (String) parent.getItemAtPosition(position);

        if(!currentStatus.equals(new_order_status[0])){

            Log.d(TAG," status "+currentStatus+" id "+orderId);

            updateStatus = new UpdateStatus(orderId, currentStatus);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiService apiService = retrofit.create(ApiService.class);
            Log.d(TAG, "update status: " + updateStatus.orderStatus+" "+updateStatus.orderId);


            Call<Void> call = apiService.updateStatus(updateStatus, "Bearer " + jwtToken);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(Order_summery.this, "Status updated successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), Customer_details_screen.class);
                        i.putExtra("jwtToken",jwtToken);

                        Log.d(TAG, "update body: " + response.body());
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Failed to update status: " + errorBody);
                            Toast.makeText(Order_summery.this, "Failed to update status: " + errorBody, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(Order_summery.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(Order_summery.this, "Please update the order status", Toast.LENGTH_SHORT).show();
    }
}



class UpdateStatus {
    @SerializedName("id")
    String orderId;

    @SerializedName("status")
    String orderStatus;

    public UpdateStatus(String orderId, String orderStatus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
}
}
