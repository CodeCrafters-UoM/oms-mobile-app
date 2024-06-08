package com.example.deleever;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Order_summery extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{
    private String jwtToken;

    String[] order_status = {"","New","Accept","Reject","Delivered","Return","Close"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summery);
        jwtToken = getIntent().getStringExtra("jwtToken");

        Spinner dropDown =(Spinner) findViewById(R.id.status_dropdown);
        dropDown.setOnItemSelectedListener(this);

        String order_status_data = getIntent().getStringExtra("status");
        order_status[0] = order_status_data;


//        for(int i = 0 ;i<order_status.length ; i++){
//            order_status[0] = order_status_data;
//            if(order_status[i+1] == order_status_data){
//                String swap = order_status[0];
//                order_status[0] = order_status[i];
//                order_status[i] = swap;
//
//                order_status[0] = order_status_data;
//            }
//        }




        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item ,order_status);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setAdapter(adapter);


        String customer_name_data = getIntent().getStringExtra("name");
        String customer_contact_data = getIntent().getStringExtra("contact");
        String customer_address_data = getIntent().getStringExtra("address");

        String Order_id_data = getIntent().getStringExtra("orderId");
        String Order_code_data = getIntent().getStringExtra("productCode");
        String Order_description_data = getIntent().getStringExtra("description");
        String quantity_data = getIntent().getStringExtra("quantity");

        String payment_method_data = getIntent().getStringExtra("paymentMethod");
        String unit_amount_data = getIntent().getStringExtra("price");

        TextView Order_id,quantity,customer_name,customer_contact,customer_address,order_status,Order_description,Order_code,payment_method,price;

        Order_id = findViewById(R.id.Order_id_data);
        Order_description  = findViewById(R.id.Order_description_data);
        Order_code = findViewById(R.id.Order_code_data);
        quantity = findViewById(R.id.quantity_data);

        customer_name = findViewById(R.id.customer_name_data);
        customer_address = findViewById(R.id.customer_address_data);
        customer_contact = findViewById(R.id.customer_contact_data);

        payment_method = findViewById(R.id.payment_method_data);
        price = findViewById(R.id.unit_amount_data);

//        order_status.setText(order_status_data);

        Order_id.setText(Order_id_data);
        Order_description.setText(Order_description_data);
        Order_code.setText(Order_code_data);
        quantity.setText(quantity_data);

        customer_name.setText(customer_name_data);
        customer_address.setText(customer_address_data);
        customer_contact.setText(customer_contact_data);

        payment_method.setText(payment_method_data);
        price.setText(unit_amount_data);

        TextView Customer_details = findViewById(R.id.Customer_details);
        Customer_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent click_Customer_details = new Intent(getApplicationContext(),Customer_details_screen.class);
                click_Customer_details.putExtra("name",customer_name_data);
                click_Customer_details.putExtra("address",customer_address_data);
                click_Customer_details.putExtra("contact",customer_contact_data);
                click_Customer_details.putExtra("jwtToken", jwtToken);
                startActivity(click_Customer_details);
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(),order_status[position] , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


