package com.example.deleever;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


public class Order_summery extends AppCompatActivity {




    ListView Order_details_list,Customer_details_list,Product_details_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summery);

        String order_status_data = getIntent().getStringExtra("status");

        String customer_name_data = getIntent().getStringExtra("name");
        String customer_contact_data = getIntent().getStringExtra("address");
        String customer_address_data = getIntent().getStringExtra("contact");
//
//        int Order_id_data = getIntent().getIntExtra("orderId",0);
//        String Order_code_data = getIntent().getStringExtra("productCode");
//        String Order_description_data = getIntent().getStringExtra("description");
//
//        String payment_method_data = getIntent().getStringExtra("paymentMethod");
//
//        TextView payment_method = findViewById(R.id.payment_method_data);
//        payment_method.setText(payment_method_data);


        TextView customer_name,customer_contact,customer_address,order_status;
        order_status = findViewById(R.id.status);

        customer_name = findViewById(R.id.customer_name_data);
        customer_address = findViewById(R.id.customer_address_data);
        customer_contact = findViewById(R.id.customer_contact_data);
        order_status.setText(order_status_data);

        customer_name.setText(customer_name_data);
        customer_address.setText(customer_address_data);
        customer_contact.setText(customer_contact_data);
//
//        TextView Order_description = findViewById(R.id.Order_description_data);
//        Order_description.setText(Order_description_data);
//
//
//        TextView Order_code = findViewById(R.id.Order_code_data);
//        Order_code.setText(Order_code_data);
//
//
//        TextView Order_id = findViewById(R.id.Order_id_data);
//        Order_id.setText(Order_id_data);



        TextView Customer_details = findViewById(R.id.Customer_details);
        Customer_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent click_Customer_details = new Intent(getApplicationContext(),Customer_details_screen.class);
                click_Customer_details.putExtra("name",customer_name_data);
                click_Customer_details.putExtra("address",customer_address_data);
                click_Customer_details.putExtra("contact",customer_contact_data);
                startActivity(click_Customer_details);
            }
        });
    }
}


