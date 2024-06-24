package com.example.deleever;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
 import android.annotation.SuppressLint;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.ListAdapter;
        import android.widget.ListView;
        import android.widget.TextView;

        import java.util.ArrayList;
        import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String sellerId;
    private String jwtToken;

    Button btn_home_product ,btn_home_order_links, btn_home_order,btn_home_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        jwtToken = intent.getStringExtra("jwtToken");
        sellerId = intent.getStringExtra("sellerid");

        btn_home_order = findViewById(R.id.btn_home_order);
        btn_home_product = findViewById(R.id.btn_home_product);
        btn_home_order_links = findViewById(R.id.btn_home_order_links);
        btn_home_report = findViewById(R.id.btn_home_report);

        btn_home_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clicked_orders_btn = new Intent(getApplicationContext(), Order_card_list.class);
                clicked_orders_btn.putExtra("jwtToken",jwtToken);
                clicked_orders_btn.putExtra("sellerid",sellerId);
                startActivity(clicked_orders_btn);
            }
        });
        btn_home_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clicked_product_btn = new Intent(getApplicationContext(), product_list.class);
                clicked_product_btn.putExtra("jwtToken", jwtToken);
                clicked_product_btn.putExtra("sellerid", sellerId);
                startActivity(clicked_product_btn);
            }
        });

        btn_home_order_links.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clicked_order_link_btn = new Intent(getApplicationContext(), DisplayOrderLinks.class);
                clicked_order_link_btn.putExtra("jwtToken",jwtToken);
                clicked_order_link_btn.putExtra("sellerid",sellerId);
                startActivity(clicked_order_link_btn);
            }
        });

        btn_home_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clicked_report_btn = new Intent(getApplicationContext(), Report.class);
                clicked_report_btn.putExtra("jwtToken",jwtToken);
                clicked_report_btn.putExtra("sellerid",sellerId);
                System.out.println("hiiii" + sellerId);
                startActivity(clicked_report_btn);
            }
        });


        ImageView icon = findViewById(R.id.img_threeLine);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                intent.putExtra("jwtToken", jwtToken);
                intent.putExtra("sellerid", sellerId);
                System.out.println("sellerrrrrrrr" + sellerId);
                startActivity(intent);
            }
        });
        }
    }

