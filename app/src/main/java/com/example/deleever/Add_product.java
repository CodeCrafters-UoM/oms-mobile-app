package com.example.deleever;

import static com.example.deleever.constant.Constant.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class OrderLink {
    private String id;
    private String link;
    private Product product;
    private String name;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

public class Add_product extends AppCompatActivity {

    private EditText edtCode, edtName, edtDescription, edtPrice;
    private Spinner spinnerOrderLink;
    private Button btnAddProduct;
    private APIservice2 apiService;

    private String jwtToken, sellerId;

    private Map<String, OrderLink> orderLinksMap = new HashMap<>();
    private List<String> orderLinks = new ArrayList<>();
    private List<String> existingProductCodes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        edtCode = findViewById(R.id.txt_productCode);
        edtName = findViewById(R.id.txt_productName);
        edtDescription = findViewById(R.id.txt_productDescription);
        edtPrice = findViewById(R.id.txt_productPrice);
        spinnerOrderLink = findViewById(R.id.spinner_orderLink);
        btnAddProduct = findViewById(R.id.btn_productSave);

        Intent i = getIntent();
        if (i != null) {
            jwtToken = i.getStringExtra("jwtToken");
            sellerId = i.getStringExtra("sellerid");
        } else {
            Toast.makeText(this, "No JWT token received", Toast.LENGTH_SHORT).show();
            finish();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIservice2.class);

        spinnerOrderLink.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(Add_product.this, "Selected item: " + item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        TextView txt_back = findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProductList();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, orderLinks);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrderLink.setAdapter(adapter);

        fetchOrderLinks();

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewProduct();
            }
        });
    }

    private void fetchOrderLinks() {
        Call<List<OrderLink>> call = apiService.getAvailableOrderLinks("Bearer " + jwtToken);
        call.enqueue(new Callback<List<OrderLink>>() {
            @Override
            public void onResponse(Call<List<OrderLink>> call, Response<List<OrderLink>> response) {
                if (response.isSuccessful()) {
                    List<OrderLink> links = response.body();
                    if (links != null) {
                        orderLinks.clear();
                        orderLinksMap.clear();
                        for (OrderLink orderLink : links) {
                            orderLinks.add(orderLink.getName()); // Add link name instead of link
                            orderLinksMap.put(orderLink.getName(), orderLink); // Use name as key in map
                        }
                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerOrderLink.getAdapter();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        } else {
                            adapter = new ArrayAdapter<>(Add_product.this, android.R.layout.simple_spinner_item, orderLinks);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerOrderLink.setAdapter(adapter);
                        }
                        fetchExistingProductCodes();
                    } else {
                        Toast.makeText(Add_product.this, "Failed to fetch order links", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Add_product.this, "Failed to fetch order links", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderLink>> call, Throwable t) {
                Toast.makeText(Add_product.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchExistingProductCodes() {
        Call<List<Product>> call = apiService.getProducts("Bearer " + jwtToken);
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
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
            }
        });
    }

    private void addNewProduct() {
        if (orderLinks.isEmpty()) {
            Toast.makeText(Add_product.this, "Please create an order link first", Toast.LENGTH_LONG).show();
            navigateToProductList();
            return;
        }
        String code = edtCode.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String priceString = edtPrice.getText().toString().trim();
        String selectedOrderLink = spinnerOrderLink.getSelectedItem().toString();

        if (code.isEmpty() || name.isEmpty() || description.isEmpty() || priceString.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (existingProductCodes.contains(code)) {
            Toast.makeText(this, "Product code already exists. Please enter another product code.", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        OrderLink selectedOrderLinkObj = orderLinksMap.get(selectedOrderLink);
        if (selectedOrderLinkObj != null && selectedOrderLinkObj.getProduct() != null) {
            Toast.makeText(this, "Order link is already associated with another product", Toast.LENGTH_SHORT).show();
            return;
        }

        Product newProduct = new Product();
        newProduct.setProductCode(code);
        newProduct.setName(name);
        newProduct.setDescription(description);
        newProduct.setPrice(price);
        newProduct.setSellerId(sellerId);
        newProduct.setOrderLink(selectedOrderLinkObj.getId());

        new AlertDialog.Builder(this)
                .setTitle("Confirm Product Creation")
                .setMessage("Are you sure you want to create this product?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Call<Product> call = apiService.addProduct("Bearer " + jwtToken, newProduct);
                        call.enqueue(new Callback<Product>() {
                            @Override
                            public void onResponse(Call<Product> call, Response<Product> response) {
                                if (response.isSuccessful()) {
                                    Product addedProduct = response.body();
                                    if (addedProduct != null) {
                                        Toast.makeText(Add_product.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                                        navigateToProductList();
                                        clearInputFields();
                                    } else {
                                        Toast.makeText(Add_product.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Add_product.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Product> call, Throwable t) {
                                Toast.makeText(Add_product.this, "Network error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void clearInputFields() {
        edtCode.setText("");
        edtName.setText("");
        edtDescription.setText("");
        edtPrice.setText("");
    }

    private void navigateToProductList() {
        Intent intent = new Intent(Add_product.this, product_list.class);
        intent.putExtra("jwtToken", jwtToken);
        intent.putExtra("sellerid", sellerId);
        startActivity(intent);
        finish();
    }
}
