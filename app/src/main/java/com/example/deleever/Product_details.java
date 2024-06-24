package com.example.deleever;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.google.gson.Gson;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.deleever.constant.Constant.*;

public class Product_details extends AppCompatActivity {

    private TextView txtProductCode, txtProductName, txtProductDescription, txtProductPrice, txtProductOrderLink;
    private String jwtToken, copyLink;
    private static final String TAG = "Product_details";

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
            txtProductCode.setText(intent.getStringExtra("productCode"));
            txtProductName.setText(intent.getStringExtra("productName"));
            txtProductDescription.setText(intent.getStringExtra("productDescription"));
            txtProductPrice.setText(String.valueOf(intent.getDoubleExtra("productPrice", 0.0)));

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
                Log.d(TAG, "Update button clicked");
                Intent updateIntent = new Intent(Product_details.this, updateProduct.class);
                updateIntent.putExtra("jwtToken", jwtToken);
                updateIntent.putExtra("productCode", txtProductCode.getText().toString());
                updateIntent.putExtra("productName", txtProductName.getText().toString());
                updateIntent.putExtra("productDescription", txtProductDescription.getText().toString());
                updateIntent.putExtra("productPrice", txtProductPrice.getText().toString());
                updateIntent.putExtra("productOrderLink", txtProductOrderLink.getText().toString());
                startActivity(updateIntent);
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
                        String productCode = txtProductCode.getText().toString();
                        Log.d(TAG, "Attempting to delete product with code: " + productCode);
                        Log.d(TAG, "Using JWT token: " + jwtToken);
                        deleteProduct(productCode);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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
        Call<List<OrderLink>> call = apiService.getAllOrderlinks("Bearer " + jwtToken);
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
                                    Log.d(TAG, "Order Link set: " + txtProductOrderLink.getText());
                                    copyLink = orderLink.getLink();
                                    break;
                                }
                            } else {
                                Log.e(TAG, "OrderLink or Product or ProductCode is null");
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
                    Log.d(TAG, "Product deleted successfully. Response code: " + response.code());
                    Toast.makeText(Product_details.this, "Product removed successfully", Toast.LENGTH_SHORT).show();
                    navigateToProductList();
                } else {
                    Log.e(TAG, "Failed to delete product. Response code: " + response.code());
                    Toast.makeText(Product_details.this, "Failed to remove product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage());
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
