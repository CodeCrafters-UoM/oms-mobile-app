package com.example.deleever;

import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.ListAdapter;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;


public class product_list extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        String[] productList = new String[]{"F005 - FROCK", "F006 - NIGHT FROCK", "T001 - TROUSER", "T002 - DENIM TROUSER", "T003 - POCKET TROUSER", "S001 - T-SHIRT", "S002 - SHIRT", "T003 - LONG SLEEV SHIRT",
                "P001 - DENIM SHORT", "P002 - POCKET SHORT", "P003 - COTTON SHORT"};

        ListAdapter listAdapter = new ArrayAdapter<String>(this, R.layout.list_item, productList);
        listView = findViewById(R.id.list_view);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String product = (String) parent.getItemAtPosition(position);

//                Intent i = new Intent(product_list.this, productDetails.class);
//                startActivity(i);
            }
        });

        ImageView icon = findViewById(R.id.plus_icon);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(product_list.this,"Add product", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(), addProduct.class);
//                startActivity(intent);

            }
        });

        TextView txt_back = findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });



    }




}

