package com.example.deleever;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class Customer_details_screen extends AppCompatActivity {
    ListView customer_details_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details_screen);

        String customer_name_data = getIntent().getStringExtra("name");
        TextView customer_details_screen_name_data = findViewById(R.id.customer_details_screen_name_data);
        customer_details_screen_name_data.setText(customer_name_data);


        String customer_address_data = getIntent().getStringExtra("contact");
        TextView customer_details_address_data = findViewById(R.id.customer_details_address_data);
        customer_details_address_data.setText(customer_address_data);

        String customer_contact_data = getIntent().getStringExtra("address");
        TextView customer_details_contact_data = findViewById(R.id.customer_details_contact_data);
        customer_details_contact_data.setText(customer_contact_data);


        TextView customer_details_to_home = findViewById(R.id.custmer_details_to_home);
        customer_details_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent custmer_details_to_home = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(custmer_details_to_home);}
        });
    }
}
