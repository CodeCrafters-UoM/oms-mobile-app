package com.example.deleever;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Product_details extends AppCompatActivity {

    private ListView listView;
    private TextView txtProductCode, txtProductName, txtProductDescription, txtProductPrice;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        txtProductCode = findViewById(R.id.txt_productCodeHint);
        txtProductName = findViewById(R.id.txt_productNameHint);
        txtProductDescription = findViewById(R.id.txt_productDescriptionHint);
        txtProductPrice = findViewById(R.id.txt_productPriceHint);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("productCode")) {
            String productCode = intent.getStringExtra("productCode");
            fetchProductDetails(productCode);
        } else {
            Toast.makeText(this, "No product code received", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void fetchProductDetails(String productCode) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.249:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIservice2 apiService = retrofit.create(APIservice2.class);

        Call<Product> call = apiService.getProductDetails(productCode);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Product product = response.body();
                    displayProductDetails(product);
                } else {
                    // Handle API error
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                // Handle network or API error
            }
        });
    }

    private void displayProductDetails(Product product) {
        txtProductCode.setText(product.getProductCode());
        txtProductName.setText(product.getName());
        txtProductDescription.setText(product.getDescription());
        txtProductPrice.setText(product.getPrice());

        Button btn_update = findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Product_details.this, updateProduct.class);
                intent.putExtra("productCode", txtProductCode.getText().toString());
                intent.putExtra("productName", txtProductName.getText().toString());
                intent.putExtra("productDescription", txtProductDescription.getText().toString());
                intent.putExtra("productPrice", txtProductPrice.getText().toString());
                startActivity(intent);
            }
        });


        Button btn_remove = findViewById(R.id.btn_remove);
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct(product.getProductCode());
            }
        });
    }

    private void deleteProduct(String productCode) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.249:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIservice2 apiService = retrofit.create(APIservice2.class);
        Call<Void> call = apiService.deleteProduct(productCode);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Product_details.this, "Product removed successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Product_details.this, product_list.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Product_details.this, "Failed to remove product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(Product_details.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}