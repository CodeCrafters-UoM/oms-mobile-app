package com.example.deleever;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.NumberFormatException;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


class product_list extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Product> adapter; // Use ArrayAdapter<Product> instead of ArrayAdapter<String>
    private List<Product> productList;
    private String jwtToken, sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        // Retrieve JWT token from intent extras
        jwtToken = getIntent().getStringExtra("jwtToken");
        sellerId = getIntent().getStringExtra("sellerid");

        listView = findViewById(R.id.list_view);

        ImageView plus_icon = findViewById(R.id.plus_icon);
        plus_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(product_list.this, Add_product.class);
                i.putExtra("jwtToken", jwtToken);
                i.putExtra("sellerid", sellerId);
                System.out.println("seler id  " + sellerId);
                startActivity(i);
            }
        });

        TextView txt_back = findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                navigateToProductList();
            }
        });

        // Fetch product list
        fetchProductList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh product list when activity resumes
        fetchProductList();
    }

    private void fetchProductList() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.8.144:8000/") // Replace with your actual API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIservice2 apiService = retrofit.create(APIservice2.class);

        Call<List<Product>> call = apiService.getProducts("Bearer " + jwtToken);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
//                    Log.d(TAG, "Response Body: " + new Gson().toJson(response.body()));
                    productList = response.body();
                    if (productList != null) {
                        Log.d(TAG, "Product list size: " + productList.size());
                        for (Product product : productList) {
                            Log.d(TAG, "Product Order Link: " + product.getProductOrderLink());
//                            Log.d(TAG, "Product: " + product.getName());
                        }
                        displayProductList(productList);
                    } else {
                        Log.e(TAG, "Product list is null");
                    }
                } else {
                    Log.e(TAG, "API error: " + response.code());
                    // Handle API error based on response code
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e(TAG, "API call failed: " + t.getMessage());
                t.printStackTrace();
                // Handle network or API error
            }
        });

    }

    public void displayProductList(List<Product> productList) {
        String[] productDetails = new String[productList.size()];
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            productDetails[i] = product.getProductCode() + " - " + product.getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, productDetails);
        listView.setAdapter(adapter);

        // Set item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Clicked position: " + position); // Add this line to log the position
                if (productList != null && !productList.isEmpty()) {
                    if (position < productList.size()) {
                        Product selectedProduct = productList.get(position);
                        Log.d(TAG, "Selected Product: " + selectedProduct.getName()); // Log the selected product
                        // Proceed with intent creation
                        Intent intent = new Intent(product_list.this, Product_details.class);
                        intent.putExtra("productCode", selectedProduct.getProductCode());
                        intent.putExtra("productName", selectedProduct.getName());
                        intent.putExtra("productDescription", selectedProduct.getDescription());
                        intent.putExtra("productPrice", selectedProduct.getPrice());
                        intent.putExtra("jwtToken", jwtToken);
                        startActivity(intent);

                        System.out.println("ppppppp" + selectedProduct.getProductCode());
                    } else {
                        Log.e(TAG, "Invalid position: " + position);
                    }
                } else {
                    Log.e(TAG, "ProductList is null or empty");
                }
            }
        });

    }

    private void navigateToProductList() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("jwtToken", jwtToken);
//        String userId = sellerID;
        intent.putExtra("sellerid", sellerId);
        startActivity(intent);
        finish(); // Close the current activity to prevent users from returning to it
    }




}


class Product {
    @SerializedName("productCode")
    private String productCode;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private Double price;

    @SerializedName("productOrderLink")
    private String productOrderLink;

    @SerializedName("sellerId")
    private String sellerId;

    @SerializedName("orderLink")
    private String orderLink;


    public String getOrderLink() {
        return orderLink;
    }
    public void setOrderLink(String orderLink) {
        this.orderLink = orderLink;
    }
    public String getSellerId() {
        return sellerId;
    }
    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
    public String getProductCode() {
        return productCode;
    }
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getProductOrderLink() {
        return productOrderLink;
    }

    public void setProductOrderLink(String productOrderLink) {
        this.productOrderLink = productOrderLink;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void putProductCode(String productCode) {
        this.productCode = productCode;
    }

    public void putName(String name) {
        this.name = name;
    }

    public void putDescription(String description) {
        this.description = description;
    }
    public void putPrice(Double price) {
        this.price = price;
    }


}
