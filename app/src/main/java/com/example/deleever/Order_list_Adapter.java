package com.example.deleever;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;





        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.ViewGroup;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import java.util.List;

public class Order_list_Adapter extends RecyclerView.Adapter<Order_list_holder> {
    Context context;
    private List<Order_list_row> orderList;



    public Order_list_Adapter(Context context,List<Order_list_row> orderList) {
        this.context=context;
        this.orderList=orderList;
    }

    @NonNull
    @Override
    public Order_list_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new Order_list_holder(LayoutInflater.from(context).inflate(R.layout.order_list_row,parent,false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull Order_list_holder holder, int position) {
        holder.data_order_details_address.setText(orderList.get(position).getDate_order_details_address());
        holder.data_order_details_name.setText(orderList.get(position).getDate_order_details_name());
        holder.data_order_details_status.setText(orderList.get(position).getDate_order_details_status());
        holder.data_order_details_id.setText(String.valueOf(orderList.get(position).getDate_order_details_id()));
        holder.data_order_details_time.setText(String.valueOf(orderList.get(position).getDate_order_details_time()));
        holder.data_order_details_date.setText(String.valueOf(orderList.get(position).getDate_order_details_date()));


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    void filterList(List<Order_list_row> filteredList){
        orderList = filteredList;
        notifyDataSetChanged();
    }
}
