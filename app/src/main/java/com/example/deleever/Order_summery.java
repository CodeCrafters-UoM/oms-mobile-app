package com.example.deleever;
        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ListAdapter;
        import android.widget.ListView;
        import android.widget.TextView;


public class Order_summery extends AppCompatActivity {



    ListView Order_details_list,Customer_details_list,Product_details_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summery);

        String customer_name_data = getIntent().getStringExtra("name");
        String Order_id_data = getIntent().getStringExtra("orderId");
        String Order_code_data = getIntent().getStringExtra("productCode");
        String Order_description_data = getIntent().getStringExtra("description");
        String customer_address_data = getIntent().getStringExtra("contact");
        String customer_contact_data = getIntent().getStringExtra("address");

        TextView customer_name = findViewById(R.id.customer_name_data);
        customer_name.setText(customer_name_data);

        TextView customer_address = findViewById(R.id.customer_address_data);
        customer_address.setText(customer_address_data);


        TextView customer_contact = findViewById(R.id.customer_contact_data);
        customer_contact.setText(customer_contact_data);

        TextView Order_description = findViewById(R.id.Order_description_data);
        Order_description.setText(Order_description_data);


        TextView Order_code = findViewById(R.id.Order_code_data);
        Order_code.setText(Order_code_data);


        TextView Order_id = findViewById(R.id.Order_id_data);
        Order_id.setText(Order_id_data);

    }
}


//        String[] date_order_details_name ={"Name","Name","Name","Name","Name","Name","Name","Name"};
//        String[] payment_method_data ={"Name","Name","Name","Name","Name","Name","Name","Name"};
//        String[] customer_name_data ={"Name","Name","Name","Name","Name","Name","Name","Name"};
//
//        ListAdapter listAdapter_customer_details_list = new ArrayAdapter<String>(this,R.layout.customer_details_list_row,customer_name_data);
//
//        Order_details_list = findViewById(R.id.Order_details_list);
//        order_details_row order_details_row = new order_details_row(this,date_order_details_name);
//        Order_details_list.setAdapter(order_details_row);
//
//        Product_details_list = findViewById(R.id.Product_details_list);
//        payment_details_row payment_details_row = new payment_details_row(this,payment_method_data);
//        Product_details_list.setAdapter(payment_details_row);
//
//        Customer_details_list = findViewById(R.id.Customer_details_list);
//        customer_details_row customer_details_row = new customer_details_row(this,customer_name_data);
//        Customer_details_list.setAdapter(customer_details_row);
//
//
//        Customer_details_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String selected_customer_details = (String) parent.getItemAtPosition(position);
//
//                Intent intent_customer_details_list = new Intent(Order_summery.this, Customer_details_screen.class);
//                startActivity(intent_customer_details_list);
//            }
//        });
//    }
//}

//class order_details_row extends ArrayAdapter<String>{
//    Context context;
//    String[] Order_description_data;
//
//    order_details_row(Context context,  String[] Order_description_data){
//        super(context,R.layout.order_details_row,R.id.Order_description_data,Order_description_data);
//        this.context = context;
//        this.Order_description_data=Order_description_data;
//    }
//
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//
//        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View order_details_row =  inflater.inflate(R.layout.order_details_row,parent,false);
//        TextView Order_description_data  = order_details_row.findViewById(R.id.Order_description_data);
//        Order_description_data.setText(this.Order_description_data[position]);
//        return order_details_row;
//
//    }
//}
//
//class customer_details_row extends ArrayAdapter<String>{
//    Context context;
//    String[] customer_name_data;
//    customer_details_row(Context context,  String[] customer_name_data){
//        super(context,R.layout.customer_details_row,R.id.customer_name_data,customer_name_data);
//        this.context = context;
//        this.customer_name_data=customer_name_data;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//
//        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View customer_details_row =  inflater.inflate(R.layout.customer_details_row,parent,false);
//        TextView customer_name_data  = customer_details_row.findViewById(R.id.customer_name_data);
//        customer_name_data.setText(this.customer_name_data[position]);
//        return customer_details_row;
//
//    }
//
//}
//
//
//class payment_details_row extends ArrayAdapter<String>{
//    Context context;
//    String[] payment_method_data;
//    payment_details_row(Context context,  String[] payment_method_data){
//        super(context,R.layout.payment_details_row,R.id.payment_method_data,payment_method_data);
//        this.context = context;
//        this.payment_method_data=payment_method_data;
//    }
//
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//
//        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View payment_details_row =  inflater.inflate(R.layout.payment_details_row,parent,false);
//        TextView payment_method_data  = payment_details_row.findViewById(R.id.payment_method_data);
//        payment_method_data.setText(this.payment_method_data[position]);
//        return payment_details_row;
//
//    }
//}
//
//
