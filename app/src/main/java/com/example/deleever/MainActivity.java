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

    Button btn_home_product ,btn_home_order_links, btn_home_order,btn_home_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_home_product = findViewById(R.id.btn_home_product);

        btn_home_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clicked_product_btn = new Intent(getApplicationContext(), product_list.class);
                startActivity(clicked_product_btn);
            }
        });
            btn_home_order = findViewById(R.id.btn_home_order);
            btn_home_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            Intent clicked_orders_btn = new Intent(getApplicationContext(), Order_card_list.class);
                            startActivity(clicked_orders_btn);
                    }
            });


        btn_home_report = findViewById(R.id.btn_home_report);

        btn_home_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clicked_report_btn = new Intent(getApplicationContext(), Report.class);
                startActivity(clicked_report_btn);
            }
        });


        ImageView icon = findViewById(R.id.imageView7);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);

            }
        });

    }
        }

