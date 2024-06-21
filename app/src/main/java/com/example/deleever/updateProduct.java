package com.example.deleever;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class updateProduct extends AppCompatActivity {
    private EditText edtProductName, edtProductDescription, edtProductPrice, edtProductCode, edtProductOrderLink;
    private Button btnSave;
    private String productCode;
    private String jwtToken;

    private List<String> existingProductCodes = new ArrayList<>(); // List to store existing product codes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        edtProductName = findViewById(R.id.edt_product_name);
        edtProductDescription = findViewById(R.id.edt_product_description);
        edtProductPrice = findViewById(R.id.edt_product_price);
        edtProductCode = findViewById(R.id.edt_product_code);
        edtProductOrderLink = findViewById(R.id.txt_productLinkHint);
        btnSave = findViewById(R.id.btn_save);

        edtProductOrderLink.setEnabled(false);

        TextView txt_back = findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToBackProductDetails();
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("productCode")) {
            productCode = intent.getStringExtra("productCode");
            String productName = intent.getStringExtra("productName");
            String productDescription = intent.getStringExtra("productDescription");
            String productPrice = intent.getStringExtra("productPrice");
            String productOrderLink = intent.getStringExtra("productOrderLink");
            jwtToken = intent.getStringExtra("jwtToken");

            edtProductName.setText(productName);
            edtProductDescription.setText(productDescription);
            edtProductPrice.setText(productPrice);
            edtProductCode.setText(productCode);
            edtProductOrderLink.setText(productOrderLink);


        } else {
            Toast.makeText(this, "No product details received", Toast.LENGTH_SHORT).show();
            finish();
        }

        fetchExistingProductCodes(); // Fetch existing product codes on create

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProductDetails();
            }
        });
    }

    private void fetchExistingProductCodes() {
        Retrofit retrofit = RetrofitClient.getClient("http://192.168.8.144:8000"); // Replace with your actual API base URL
        APIservice2 apiService = retrofit.create(APIservice2.class);
        Call<List<Product>> call = apiService.getProducts("Bearer " + jwtToken); // Adjust this to your actual API endpoint
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> products = response.body();
                    if (products != null) {
                        existingProductCodes.clear();
                        for (Product product : products) {
                            existingProductCodes.add(product.getProductCode());
                        }
                        Log.d("FetchProductCodes", "Product codes fetched successfully");
                    } else {
                        Log.e("FetchProductCodesError", "Response body is null");
                    }
                } else {
                    Log.e("FetchProductCodesError", "API error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("FetchProductCodesError", "Network error: " + t.getMessage());
            }
        });
    }

    private void updateProductDetails() {
        String updatedProductCode = edtProductCode.getText().toString().trim();
        String updatedName = edtProductName.getText().toString().trim();
        String updatedDescription = edtProductDescription.getText().toString().trim();
        Double updatedPrice = Double.valueOf(edtProductPrice.getText().toString().trim());

        if (updatedName.isEmpty() || updatedDescription.isEmpty() || updatedProductCode.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!updatedProductCode.equals(productCode) && existingProductCodes.contains(updatedProductCode)) {
            Toast.makeText(this, "Product code already exists. Please enter another product code.", Toast.LENGTH_SHORT).show();
            return;
        }

        Product updatedProduct = new Product();
        updatedProduct.setProductCode(updatedProductCode);
        updatedProduct.setName(updatedName);
        updatedProduct.setDescription(updatedDescription);
        updatedProduct.setPrice(updatedPrice);

        updateProductMethod(productCode, updatedProduct);
    }

    private void updateProductMethod(String previousProductCode, Product updatedProduct) {
        Retrofit retrofit = RetrofitClient.getClient("http://192.168.84.10:8000"); // Replace with your actual API base URL
        APIservice2 apiService = retrofit.create(APIservice2.class);
        Call<Product> call = apiService.updateProduct("Bearer " + jwtToken, previousProductCode, updatedProduct); // Pass JWT token to API
        call.enqueue(new Callback<Product>() {

            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(updateProduct.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                    navigateToProductList();
                } else {
                    Log.e("UpdateProductError", "Failed to update product: " + response.message());
                    Toast.makeText(updateProduct.this, "Failed to update product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(updateProduct.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToBackProductDetails() {
        Intent intent = new Intent(getApplicationContext(), Product_details.class);
        intent.putExtra("jwtToken", jwtToken);
        intent.putExtra("productCode", edtProductCode.getText().toString());
        intent.putExtra("productName", edtProductName.getText().toString());
        intent.putExtra("productDescription", edtProductDescription.getText().toString());
        intent.putExtra("productPrice", edtProductPrice.getText().toString());
        intent.putExtra("productOrderLink", edtProductOrderLink.getText().toString());
        startActivity(intent);
        finish(); // Close the current activity to prevent users from returning to it
    }

    private void navigateToProductList() {

        Intent intent = new Intent(getApplicationContext(), product_list.class);
        intent.putExtra("jwtToken", jwtToken);
        startActivity(intent);
        finish(); // Close the current activity to prevent users from returning to it
    }

    public static class RetrofitClient {
        private static Retrofit retrofit = null;

        public static Retrofit getClient(String baseUrl) {
            if (retrofit == null) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                OkHttpClient client = new OkHttpClient.Builder()
                        .addInterceptor(logging)
                        .build();

                retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build();
            }
            return retrofit;
        }
    }
}
