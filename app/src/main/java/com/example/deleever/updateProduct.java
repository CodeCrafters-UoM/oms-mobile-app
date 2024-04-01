package com.example.deleever;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class updateProduct extends AppCompatActivity {

    private EditText edtProductName, edtProductDescription, edtProductPrice, edtProductCode;
    private Button btnSave;
    private String productCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        edtProductName = findViewById(R.id.edt_product_name);
        edtProductDescription = findViewById(R.id.edt_product_description);
        edtProductPrice = findViewById(R.id.edt_product_price);
        edtProductCode = findViewById(R.id.edt_product_code);
        btnSave = findViewById(R.id.btn_save);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("productCode")) {
            productCode = intent.getStringExtra("productCode");
            String productName = intent.getStringExtra("productName");
            String productDescription = intent.getStringExtra("productDescription");
            String productPrice = intent.getStringExtra("productPrice");


            edtProductName.setText(productName);
            edtProductDescription.setText(productDescription);
            edtProductPrice.setText(productPrice);
            edtProductCode.setText(productCode);
        } else {
            Toast.makeText(this, "No product details received", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), product_list.class);
                startActivity(intent);
                updateProductDetails();
            }
        });
    }

    private void updateProductDetails() {
        String updatedName = edtProductName.getText().toString().trim();
        String updatedDescription = edtProductDescription.getText().toString().trim();
        String updatedPrice = edtProductPrice.getText().toString().trim();

        if (updatedName.isEmpty() || updatedDescription.isEmpty() || updatedPrice.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Product updatedProduct = new Product();
        updatedProduct.setProductCode(productCode);
        updatedProduct.setName(updatedName);
        updatedProduct.setDescription(updatedDescription);
        updatedProduct.setPrice(updatedPrice);

        updateProductMethod(updatedProduct);
    }

    private void updateProductMethod(Product updatedProduct) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.8.144:8000/") // Replace with your actual API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIservice2 apiService = retrofit.create(APIservice2.class);
        Call<Void> call = apiService.updateProduct(updatedProduct.getProductCode(), updatedProduct);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(updateProduct.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                    // You can update UI or perform other actions after successful update
                } else {
                    // Log the server response
//                    Log.e("UpdateProductError", "Failed to update product: " + response.message());
//                    Toast.makeText(updateProduct.this, "Failed to update product", Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(updateProduct.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
