package com.example.deleever;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;





        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.SearchView;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

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
        import android.widget.ListAdapter;
        import android.widget.ListView;
        import android.widget.TextView;

        import java.util.ArrayList;
        import java.util.List;

public class Order_list extends AppCompatActivity {
    SearchView search_order_list;
    RecyclerView order_details_list;
    List<Order_list_row> orderList;
    Order_list_Adapter  orderListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        displayItems();
        search_order_list=findViewById(R.id.search_order_list);
        search_order_list.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String newText){
        List<Order_list_row> filteredList = new ArrayList<>();
//        String num = String.valueOf(recyclerView.getId());
        for(Order_list_row item: orderList){
            if(item.getDate_order_details_name().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(item);
            }
            if(String.valueOf(item.getDate_order_details_id()).contains((newText))){
                filteredList.add(item);
            }

        }

        orderListAdapter.filterList(filteredList);
    }


    private void displayItems(){
        order_details_list=findViewById(R.id.order_details_list);
        order_details_list.setHasFixedSize(true);
        order_details_list.setLayoutManager(new LinearLayoutManager(this));
        orderList=new ArrayList<>();
        orderList.add(new Order_list_row(3,3,"FFF",4,"fffabvyf","sdfiovjo"));
        orderList.add(new Order_list_row(33,3,"FFF",4,"fffabvyf","sdfiovjo"));
        orderList.add(new Order_list_row(35,3,"FFF",4,"fffabvyf","sdfiovjo"));
        orderList.add(new Order_list_row(33,3,"FFF",4,"fffabvyf","sdfiovjo"));
        orderList.add(new Order_list_row(32,3,"FFF",4,"fffabvyf","sdfiovjo"));
        orderList.add(new Order_list_row(43,3,"FFF",4,"fffabvyf","sdfiovjo"));
        orderList.add(new Order_list_row(39,3,"FFF",4,"fffabvyf","sdfiovjo"));


        orderListAdapter= new Order_list_Adapter(this,orderList);
        order_details_list.setAdapter(orderListAdapter);
    }
}

