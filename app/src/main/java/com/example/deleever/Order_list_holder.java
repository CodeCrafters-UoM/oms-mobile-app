package com.example.deleever;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;




        import android.view.View;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

public class Order_list_holder extends RecyclerView.ViewHolder {

    public TextView data_order_details_date,data_order_details_time,data_order_details_status,data_order_details_id,
            data_order_details_name,data_order_details_address;
    public Order_list_holder(@NonNull View itemView) {
        super(itemView);
        data_order_details_date=itemView.findViewById(R.id.data_order_details_date);
        data_order_details_time=itemView.findViewById(R.id.data_order_details_time);
        data_order_details_status=itemView.findViewById(R.id.data_order_details_status);
        data_order_details_id=itemView.findViewById(R.id.data_order_details_id);
        data_order_details_name=itemView.findViewById(R.id.data_order_details_name);
        data_order_details_address=itemView.findViewById(R.id.data_order_details_address);
    }
}
