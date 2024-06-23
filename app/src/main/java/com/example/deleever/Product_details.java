package com.example.deleever;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.example.deleever.constant.Constant.*;


public class Product_details extends AppCompatActivity {

    private TextView txtProductCode, txtProductName, txtProductDescription, txtProductPrice, txtProductOrderLink, txt_back;
    private String jwtToken,copyLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        txtProductCode = findViewById(R.id.txt_productCodeHint);
        txtProductName = findViewById(R.id.txt_productNameHint);
        txtProductDescription = findViewById(R.id.txt_productDescriptionHint);
        txtProductPrice = findViewById(R.id.txt_productPriceHint);
        txtProductOrderLink = findViewById(R.id.txt_productLinkHint);
        ImageView imgCopy = findViewById(R.id.copy_icon);

        Intent intent = getIntent();
        if (intent != null) {
            jwtToken = intent.getStringExtra("jwtToken");
            if (intent.hasExtra("productCode")) {
                String productCode = intent.getStringExtra("productCode");
                txtProductCode.setText(productCode);
            }
            if (intent.hasExtra("productName")) {
                String productName = intent.getStringExtra("productName");
                txtProductName.setText(productName);
            }
            if (intent.hasExtra("productDescription")) {
                String productDescription = intent.getStringExtra("productDescription");
                txtProductDescription.setText(productDescription);

            }
            if (intent.hasExtra("productPrice")) {
                double productPrice = intent.getDoubleExtra("productPrice", 0.0);
                txtProductPrice.setText(String.valueOf(productPrice));
            }

            fetchOrderLinks();
        } else {
            Toast.makeText(this, "No intent data received", Toast.LENGTH_SHORT).show();
            finish();
        }

        TextView txt_back = findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProductList();
            }
        });

        Button btn_update = findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("productDetails", "Update button clicked");
                Intent updateIntent = new Intent(Product_details.this, updateProduct.class);
                updateIntent.putExtra("jwtToken", jwtToken);
                updateIntent.putExtra("productCode", txtProductCode.getText().toString());
                updateIntent.putExtra("productName", txtProductName.getText().toString());
                updateIntent.putExtra("productDescription", txtProductDescription.getText().toString());
                updateIntent.putExtra("productPrice", txtProductPrice.getText().toString());
                updateIntent.putExtra("productOrderLink", txtProductOrderLink.getText().toString());
                startActivity(updateIntent);
                Log.d("productDetails", "Update intent started");
            }
        });

        Button btn_remove = findViewById(R.id.btn_remove);
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Product_details.this);
                builder.setMessage("Are you sure you want to remove this product?")
                        .setTitle("Confirm Removal");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        deleteProduct(txtProductCode.getText().toString());
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked No button, do nothing or dismiss the dialog
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        imgCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyOrderLinkToClipboard();
            }
        });

    }


    private void fetchOrderLinks() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIservice2 apiService = retrofit.create(APIservice2.class);
        Call<List<OrderLink>> call = apiService.getAllOrderlinks("Bearer " + jwtToken); // Adjust this to your actual API endpoint
        call.enqueue(new Callback<List<OrderLink>>() {
            @Override
            public void onResponse(Call<List<OrderLink>> call, Response<List<OrderLink>> response) {
                if (response.isSuccessful()) {
                    List<OrderLink> orderLinks = response.body();
                    Log.d(TAG, "Response Body: " + new Gson().toJson(response.body()));
                    if (orderLinks != null) {
                        for (OrderLink orderLink : orderLinks) {
                            if (orderLink.getProduct() != null && orderLink.getProduct().getProductCode() != null) {
                                if (orderLink.getProduct().getProductCode().equals(txtProductCode.getText().toString())) {
                                    txtProductOrderLink.setText(orderLink.getName());
                                    Log.d(TAG, "Order Link set:" + txtProductOrderLink.getText());
                                    copyLink = orderLink.getLink();
                                    break;
                                }
                            } else {
                                Log.e(TAG, "OrderLink or Product or ProductCode is null gggggggggggg");
                            }
                        }
                    } else {
                        Log.e("FetchOrderLinksError", "Response body is null");
                    }
                } else {
                    Log.e("FetchOrderLinksError", "API error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<OrderLink>> call, Throwable t) {
                Log.e("FetchOrderLinksError", "Network error: " + t.getMessage());
            }
        });
    }

    private void deleteProduct(String productCode) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIservice2 apiService = retrofit.create(APIservice2.class);
        Call<Void> call = apiService.deleteProduct("Bearer " + jwtToken, productCode);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Product_details.this, "Product removed successfully", Toast.LENGTH_SHORT).show();
                    navigateToProductList();
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

    private void navigateToProductList() {
        Intent intent = new Intent(getApplicationContext(), product_list.class);
        intent.putExtra("jwtToken", jwtToken);
        startActivity(intent);
        finish(); // Close the current activity to prevent users from returning to it
    }

    private void copyOrderLinkToClipboard() {
        if (!copyLink.isEmpty()) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Order Link", copyLink);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Order link copied to clipboard", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No order link to copy", Toast.LENGTH_SHORT).show();
        }
    }
}
