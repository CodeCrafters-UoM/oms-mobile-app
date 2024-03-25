package com.example.deleever;

import android.content.Context;



        import android.content.Context;

public class Order_list_row {

    Context context;
    int date_order_details_date;
    int date_order_details_time;
    String date_order_details_status;
    int date_order_details_id;

    String date_order_details_name;

    String date_order_details_address;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setDate_order_details_date(int date_order_details_date) {
        this.date_order_details_date = date_order_details_date;
    }

    public void setDate_order_details_time(int date_order_details_time) {
        this.date_order_details_time = date_order_details_time;
    }

    public void setDate_order_details_status(String date_order_details_status) {
        this.date_order_details_status = date_order_details_status;
    }

    public void setDate_order_details_id(int date_order_details_id) {
        this.date_order_details_id = date_order_details_id;
    }

    public void setDate_order_details_name(String date_order_details_name) {
        this.date_order_details_name = date_order_details_name;
    }

    public void setDate_order_details_address(String date_order_details_address) {
        this.date_order_details_address = date_order_details_address;
    }

    public Context getContext() {
        return context;
    }

    public int getDate_order_details_date() {
        return date_order_details_date;
    }

    public int getDate_order_details_time() {
        return date_order_details_time;
    }

    public String getDate_order_details_status() {
        return date_order_details_status;
    }

    public int getDate_order_details_id() {
        return date_order_details_id;
    }

    public String getDate_order_details_name() {
        return date_order_details_name;
    }

    public String getDate_order_details_address() {
        return date_order_details_address;
    }

    public Order_list_row(int date_order_details_date, int date_order_details_time, String date_order_details_status, int date_order_details_id, String date_order_details_name, String date_order_details_address) {
        this.date_order_details_date = date_order_details_date;
        this.date_order_details_time = date_order_details_time;
        this.date_order_details_status = date_order_details_status;
        this.date_order_details_id = date_order_details_id;
        this.date_order_details_name = date_order_details_name;
        this.date_order_details_address = date_order_details_address;
    }
//public Order_list_row(int date_order_details_id, String date_order_details_name, String date_order_details_address) {
////    this.date_order_details_date = date_order_details_date;
////    this.date_order_details_time = date_order_details_time;
////    this.date_order_details_status = date_order_details_status;
//    this.date_order_details_id = date_order_details_id;
//    this.date_order_details_name = date_order_details_name;
//    this.date_order_details_address = date_order_details_address;
//}
}
