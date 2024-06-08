package com.example.deleever;

import static androidx.core.content.ContextCompat.getSystemService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DisplayOrderLinks extends AppCompatActivity {
    RecyclerView order_link_list;
    List<OrderLinkModel> orderLinks;
    OrderLinksAdapter orderLinksAdapter;
    private static final String IP_ADDRESS = "192.168.172.146";
    private static final String BASE_URL = "http://" + IP_ADDRESS + ":8000/";
    private static final String TAG = "MainActivity";
    private String jwtToken;
    private String sellerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_order_links);

        Intent intent = getIntent();
        jwtToken = intent.getStringExtra("jwtToken");
        sellerId = intent.getStringExtra("sellerid");

        order_link_list = findViewById(R.id.order_link_list);
        order_link_list.setLayoutManager(new LinearLayoutManager(this));
        orderLinksAdapter = new OrderLinksAdapter(DisplayOrderLinks.this,orderLinks);
        order_link_list.setAdapter(orderLinksAdapter);

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
                    if(orderLinks!=null){
                        orderLinksAdapter.setItems(orderLinks);
                        Log.d(TAG, "Items received: " + orderLinks.size());
                    } else {
                    Log.d(TAG, "Response body is null");
                    Toast.makeText(DisplayOrderLinks.this, "Order list is empty", Toast.LENGTH_SHORT).show();
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
    Context context;
    List<OrderLinkModel> orderLinks = new ArrayList<>();


    public OrderLinksAdapter(Context context, List<OrderLinkModel> orderLinks) {
        this.context = context;
        this.orderLinks = orderLinks;
    }
    public void setItems(List<OrderLinkModel> orderLinks){
        this.orderLinks = orderLinks;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public OrderLinksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.display_order_links_card,parent,false);
        return new OrderLinksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderLinksViewHolder holder, int position) {
        OrderLinkModel orderLinkModel = orderLinks.get(position);
        holder.orderLink.setText(orderLinks.get(position).getOrderLink());
        holder.ol_copy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Copy",orderLinkModel.getLinkValue());;
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(context,"copied",Toast.LENGTH_SHORT).show();
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
        public ImageButton ol_copy_btn;
        public OrderLinksViewHolder(@NonNull View itemView) {
            super(itemView);
            orderLink = itemView.findViewById(R.id.OrderLinkData);
            ol_copy_btn = itemView.findViewById(R.id.ol_copy_btn);
        }
    }
}

class OrderLinkModel{
    @SerializedName("name")
    String orderLink ;

    @SerializedName("link")
    String linkValue;

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
    public OrderLinkModel(String orderLink,String linkValue){
        this.orderLink=orderLink;
        this.linkValue=linkValue;
    }
}
