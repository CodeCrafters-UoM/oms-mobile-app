package com.example.deleever;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
public class Order_questionnaire extends AppCompatActivity {

    Button save_order_details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_questionnaire);

        TextView order_details_to_home = findViewById(R.id.order_details_to_home);
        order_details_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order_details_to_home = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(order_details_to_home);}
        });
        save_order_details = findViewById(R.id.save_order_details);
        save_order_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Order_questionnaire.this,"Saved Successfully",Toast.LENGTH_SHORT).show();
            }
        });


    }
}