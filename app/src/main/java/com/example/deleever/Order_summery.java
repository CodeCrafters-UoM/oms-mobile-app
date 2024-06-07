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


        String customer_name_data = getIntent().getStringExtra("name");
        String Order_id_data = getIntent().getStringExtra("orderId");
        String Order_code_data = getIntent().getStringExtra("productCode");
        String Order_description_data = getIntent().getStringExtra("description");
        String customer_address_data = getIntent().getStringExtra("contact");
        String customer_contact_data = getIntent().getStringExtra("address");
        String order_status_data = getIntent().getStringExtra("status");

        TextView customer_name = findViewById(R.id.customer_name_data);
        customer_name.setText(customer_name_data);

        TextView customer_address = findViewById(R.id.customer_address_data);
        customer_address.setText(customer_address_data);


        TextView customer_contact = findViewById(R.id.customer_contact_data);
        customer_contact.setText(customer_contact_data);

        TextView Order_description = findViewById(R.id.Order_description_data);
        Order_description.setText(Order_description_data);


        TextView Order_code = findViewById(R.id.Order_code_data);
        Order_code.setText(Order_code_data);


        TextView Order_id = findViewById(R.id.Order_id_data);
        Order_id.setText(Order_id_data);


        TextView order_status = findViewById(R.id.status);
        order_status.setText(order_status_data);

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


