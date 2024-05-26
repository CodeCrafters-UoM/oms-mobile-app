package com.example.deleever;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.deleever.R;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class product_list extends AppCompatActivity {


    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<Product> productList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        listView = findViewById(R.id.list_view);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.8.144:8000/") // Replace with your actual API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIservice2 apiService = retrofit.create(APIservice2.class);

        Call<List<Product>> call = apiService.getProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    productList = response.body();
                    System.out.println("PL : " + productList.get(1));
                    displayProductList(productList);
                } else {
                    System.out.println("error");
                    // Handle API error
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                // Handle network or API error
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id ) {
                if (productList != null && position <= productList.size()) {
                    Product selectedProduct = productList.get(position);
                    String productCode = selectedProduct.getProductCode();
                    Intent intent = new Intent(product_list.this, Product_details.class);
                    intent.putExtra("productCode", productCode);
                    startActivity(intent);
                } else {
                    Log.e(TAG, "Invalid position or productList is null");
                }
            }
        });


    }

    public void displayProductList(List<Product> productList) {
        String[] productDetails = new String[productList.size()];
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            productDetails[i] = product.getProductCode() + product.getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, productDetails);
        listView.setAdapter(adapter);
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
    private String price;

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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}


