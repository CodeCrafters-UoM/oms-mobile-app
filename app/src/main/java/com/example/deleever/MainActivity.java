package com.example.deleever;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btn_home_product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_home_product = findViewById(R.id.btn_home_report);

        btn_home_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clicked_product_btn = new Intent(getApplicationContext(), product_list.class);
                startActivity(clicked_product_btn);
            }
        });
    }
}