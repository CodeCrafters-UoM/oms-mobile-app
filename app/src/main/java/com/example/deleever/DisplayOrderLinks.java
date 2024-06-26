package com.example.deleever;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.core.content.ContextCompat.getDrawable;
import static androidx.core.content.ContextCompat.startActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.example.deleever.constant.Constant.*;

public class DisplayOrderLinks extends AppCompatActivity {

    RecyclerView order_link_list;
    List<OrderLinkModel> orderLinks;
    OrderLinksAdapter orderLinksAdapter;
    private static final String TAG = "MainActivity";
    public String jwtToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_order_links);

        Intent intent = getIntent();
        jwtToken = intent.getStringExtra("jwtToken");

        order_link_list = findViewById(R.id.order_link_list);
        order_link_list.setLayoutManager(new LinearLayoutManager(this));
        orderLinksAdapter = new OrderLinksAdapter(DisplayOrderLinks.this,orderLinks,jwtToken);
        order_link_list.setAdapter(orderLinksAdapter);

        displayLinks();
    }

    private void displayLinks() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<OrderLinkModel>> call = apiService.getOrderLinks("Bearer " + jwtToken);
        call.enqueue(new Callback<List<OrderLinkModel>>() {
            @Override
            public void onResponse(Call<List<OrderLinkModel>> call, Response<List<OrderLinkModel>> response) {
                if (response.isSuccessful()){
                    List<OrderLinkModel> orderLinks =response.body();
                    if(orderLinks!=null && !orderLinks.isEmpty()){
                        orderLinksAdapter.setItems(orderLinks);
                        Log.d(TAG, "Items received: " + orderLinks.size());
                    } else {
                        Log.d(TAG, "Response body is null");
                        Toast.makeText(DisplayOrderLinks.this, "Order links list is empty", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.d(TAG, "Response unsuccessful. Code: " + response.code());
                    try {
                        Log.d(TAG, "Error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(DisplayOrderLinks.this, "Response unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<OrderLinkModel>> call, Throwable t) {
                Log.e(TAG, "Error fetching order links", t);
                Toast.makeText(DisplayOrderLinks.this, "Error fetching order links"+t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}



class OrderLinksAdapter extends RecyclerView.Adapter<OrderLinksAdapter.OrderLinksViewHolder> {
    Dialog dialog;
    Button yes_btn,no_btn;

    Context context;
    List<OrderLinkModel> orderLinks = new ArrayList<>();
    String jwtToken;


    public OrderLinksAdapter(Context context, List<OrderLinkModel> orderLinks,String jwtToken) {
        this.context = context;
        this.orderLinks = orderLinks;
        this.jwtToken =jwtToken;
    }
    public void setItems(List<OrderLinkModel> orderLinks){
        this.orderLinks = orderLinks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderLinksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {//represent an item
        View view = LayoutInflater.from(context).inflate(R.layout.display_order_links_card,parent,false);
        return new OrderLinksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderLinksViewHolder holder, int position) {

        OrderLinkModel orderLinkModel = orderLinks.get(position);
        holder.orderLink.setText(orderLinkModel.getOrderLink());
        String name = orderLinkModel.getOrderLink();
        String linkValue = orderLinkModel.getLinkValue();
        holder.ol_copy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyOrderLink(orderLinkModel,name,linkValue);
            }
        });

        holder.ol_dlt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String productCode = orderLinkModel.getProduct().getProductCode();
                    Toast.makeText(context,"product is assigned",Toast.LENGTH_SHORT).show();
                }catch(NullPointerException e){
                    confirmation(orderLinkModel,name);

                    dialog.show();
                }
            }
        });
    }

    private void confirmation(OrderLinkModel orderLinkModel, String name) {

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.confirmation_box);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);//WIDTH,HEIGHT
        dialog.getWindow().setBackgroundDrawable(getDrawable(context,R.drawable.edit_background));
        dialog.setCancelable(false);
        no_btn = dialog.findViewById(R.id.no_btn);
        yes_btn = dialog.findViewById(R.id.yes_btn);
        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();//close dialog box
            }
        });

        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOrderLink(orderLinkModel,name,jwtToken);
                dialog.dismiss();
            }
        });
    }

    private void copyOrderLink(OrderLinkModel orderLinkModel, String name, String linkValue) {
        try{
            String productCode = orderLinkModel.getProduct().getProductCode();
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Copy",linkValue);;
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(context,name +" copied",Toast.LENGTH_SHORT).show();
        }catch (NullPointerException e){
            Toast.makeText(context,"product is not assigned",Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteOrderLink(OrderLinkModel orderLinkModel,String name,String jwtToken) {

        String id = orderLinkModel.getId();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<Void> callForDelete = apiService.deleteOrderLink("Bearer " + jwtToken,id);
        callForDelete.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    int position = orderLinks.indexOf(orderLinkModel);
                    if (position != -1) {
                        orderLinks.remove(position);
                        notifyItemRemoved(position);
                    }
                    Toast.makeText(context, name + " link delete successfully", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "mas "+response.message());
                    Log.d(TAG, "mas "+response.body());

                } else {
                    Toast.makeText(context, name + " link  not delete successfully" + "\n\n"+response.message()+"\n"+response.body(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, name + response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "mas "+response.message());

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "error " + t, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        if(orderLinks == null){
            return 0;
        }
        return orderLinks.size();
    }



    class OrderLinksViewHolder extends RecyclerView.ViewHolder{
        public TextView orderLink;
        public ImageButton ol_copy_btn,ol_dlt_btn;
        public OrderLinksViewHolder(@NonNull View itemView) {
            super(itemView);
            orderLink = itemView.findViewById(R.id.OrderLinkData);
            ol_copy_btn = itemView.findViewById(R.id.ol_copy_btn);
            ol_dlt_btn = itemView.findViewById(R.id.ol_dlt_btn);

        }
    }
}

class OrderLinkModel{
    @SerializedName("name")
    String orderLink ;

    @SerializedName("id")
    String id;
    @SerializedName("link")
    String linkValue;

    @SerializedName("product")
    Product product;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getLinkValue() {
        return linkValue;
    }

    public void setLinkValue(String linkValue) {
        this.linkValue = linkValue;
    }

    public String getOrderLink() {
        return orderLink;
    }

    public void setOrderLink(String orderLink) {
        this.orderLink = orderLink;
    }

    public OrderLinkModel(String orderLink, String linkValue, Product product) {
        this.orderLink = orderLink;
        this.linkValue = linkValue;
        this.product = product;
    }

    static class Product{
        @SerializedName("productCode")
        private String productCode;

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
  }

}
}
