package com.example.deleever;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Customer_details_screen extends AppCompatActivity {
    ListView customer_details_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details_screen);

        String[] customer_details_screen_name ={"Name","Name","Name","Name","Name","Name","Name","Name"};
        customer_details_list = findViewById(R.id.customer_details_list_row);
        customer_details customer_details_screen_row = new customer_details(this,customer_details_screen_name);
        customer_details_list.setAdapter(customer_details_screen_row);

        TextView customer_details_to_home = findViewById(R.id.custmer_details_to_home);
        customer_details_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent custmer_details_to_home = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(custmer_details_to_home);}
        });
    }
}

class customer_details extends ArrayAdapter<String>{
    Context context;
    String[] customer_details_screen_name;
    customer_details(Context context,String[] customer_details_screen_name){
        super(context,R.layout.customer_details_list_row,R.id.customer_details_screen_name_data,customer_details_screen_name);
        this.context=context;
        this.customer_details_screen_name=customer_details_screen_name;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customer_details_row =  inflater.inflate(R.layout.customer_details_list_row,parent,false);
        TextView customer_details_screen_name  = customer_details_row.findViewById(R.id.customer_details_screen_name_data);
        customer_details_screen_name.setText(this.customer_details_screen_name[position]);
        return customer_details_row;
    }
}
