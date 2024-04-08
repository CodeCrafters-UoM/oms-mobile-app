package com.example.deleever;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Order_cards_adapter extends RecyclerView.Adapter<Order_cards_adapter.ItemViewHolder>{

    private final Order_card_list_interface orderCardListInterface;

    List<Order_card> order_cards = new ArrayList<>();

    public Order_cards_adapter(Order_card_list_interface orderCardListInterface) {
        this.orderCardListInterface = orderCardListInterface;
    }

    public void setItems(List<Order_card> order_cards) {
        this.order_cards = order_cards;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_card, parent, false);
        return new ItemViewHolder(itemView,orderCardListInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Order_card currentOrder_card = order_cards.get(position);
        holder.textdeliveryAddress.setText(currentOrder_card.getDeliveryAddress());
        holder.textorderStatus.setText(currentOrder_card.getStatus());
        holder.textOrderId.setText(String.valueOf(currentOrder_card.getOrderId())+":"+currentOrder_card.getProduct().getProductCode()+"-"+currentOrder_card.getProduct().getName());
        holder.textdate.setText(currentOrder_card.SeparatedateString(currentOrder_card.getDateAndTime()));
        holder.texttime.setText(currentOrder_card.SeparateTimeString(currentOrder_card.getDateAndTime()));
        holder.textName.setText(currentOrder_card.getCustomer().getFirstName()+" "+currentOrder_card.getCustomer().getLastName());
    }

    @Override
    public int getItemCount() {
        return order_cards.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView textdate,textdeliveryAddress,textorderStatus,textName,texttime,textOrderId;

        public ItemViewHolder(@NonNull View itemView,Order_card_list_interface orderCardListInterface) {
            super(itemView);
            textOrderId = itemView.findViewById(R.id.date_order_details_id);
            textdeliveryAddress = itemView.findViewById(R.id.date_order_details_address);
            textName = itemView.findViewById(R.id.date_order_details_name);
            textorderStatus = itemView.findViewById(R.id.date_order_details_status);
            textdate = itemView.findViewById(R.id.date_order_details_date);
            texttime = itemView.findViewById(R.id.date_order_details_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(orderCardListInterface != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            orderCardListInterface.OnItemClick(pos);
                        }

                    }
                }
            });

        }}

    void filterList(List<Order_card> filteredList){
        order_cards = filteredList;
        notifyDataSetChanged();
    }


}


