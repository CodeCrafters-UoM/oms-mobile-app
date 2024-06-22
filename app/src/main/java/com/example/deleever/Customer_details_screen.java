package com.example.deleever;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class Customer_details_screen extends AppCompatActivity {
    ListView customer_details_list;
    private String jwtToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details_screen);
        jwtToken = getIntent().getStringExtra("jwtToken");
        TextView ordersAll,returnOrdersAll,ordersMy,returnOrdersMy,customer_details_screen_name_data,customer_details_address_data,customer_details_contact_data,customer_details_to_home;
        int orders_All,return_orders_All,orders_My,return_orders_My;
        String customer_name_data,customer_address_data,customer_contact_data;

        orders_All =getIntent().getIntExtra("orders(All)",0);
        ordersAll = findViewById(R.id.orders_All);
        ordersAll.setText(String.valueOf(orders_All));

        return_orders_All = getIntent().getIntExtra("returns(All)",0);
        returnOrdersAll = findViewById(R.id.return_orders_All);
        returnOrdersAll.setText(String.valueOf(return_orders_All));
        Log.d(TAG, "999return_orders_All: " + return_orders_All);

        orders_My = getIntent().getIntExtra("orders(my)",0);
        ordersMy = findViewById(R.id.orders_My);
        ordersMy.setText(String.valueOf(orders_My));

        return_orders_My = getIntent().getIntExtra("return(my)",0);
        returnOrdersMy = findViewById(R.id.return_orders_My);
        returnOrdersMy.setText(String.valueOf(return_orders_My));


        customer_name_data = getIntent().getStringExtra("name");
        customer_details_screen_name_data = findViewById(R.id.customer_details_screen_name_data);
        customer_details_screen_name_data.setText(customer_name_data);


        customer_address_data = getIntent().getStringExtra("contact");
        customer_details_address_data = findViewById(R.id.customer_details_address_data);
        customer_details_address_data.setText(customer_address_data);

        customer_contact_data = getIntent().getStringExtra("address");
        customer_details_contact_data = findViewById(R.id.customer_details_contact_data);
        customer_details_contact_data.setText(customer_contact_data);


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
