package com.example.deleever;

import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

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
        import android.widget.Toast;

public class Order_list extends AppCompatActivity {
    Button order_summery;
    ListView order_details_list;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        String[] date_order_details_name ={"Name","Name","Name","Name","Name","Name","Name","Name"};
        ListAdapter listAdapter_order_list = new ArrayAdapter<String>(this,R.layout.order_list_details_row,date_order_details_name);
        order_details_list = findViewById(R.id.order_details_list);
        order_list_details order_list_details_row = new order_list_details(this,date_order_details_name);
        order_details_list.setAdapter(order_list_details_row);

        order_details_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected_order = (String) parent.getItemAtPosition(position);

//                Intent intent_order_list = new Intent(Order_list.this, Order_summery.class);
//                startActivity(intent_order_list);
            }
        });


    }
}

class order_list_details extends ArrayAdapter<String> {
    Context context;
    int[] date_order_details_date;
    int[] date_order_details_time;
    String[] date_order_details_status;
    int[] date_order_details_id;

    String[] date_order_details_name;

    String[] date_order_details_address;


    order_list_details(Context context, String[] date_order_details_name) {
        super(context, R.layout.order_list_details_row, R.id.date_order_details_name, date_order_details_name);
        this.context = context;
        this.date_order_details_name = date_order_details_name;
        this.date_order_details_time = date_order_details_time;
        this.date_order_details_status = date_order_details_status;
        this.date_order_details_time = date_order_details_time;
        this.date_order_details_time = date_order_details_time;
        this.date_order_details_time = date_order_details_time;

    }
    //single

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View order_list_details_row = inflater.inflate(R.layout.order_list_details_row, parent, false);
        //TextView date_order_details_date  = order_list_details_row.findViewById(R.id.date_order_details_date);
        // TextView date_order_details_time  = order_list_details_row.findViewById(R.id.date_order_details_time);
        // TextView date_order_details_status  = order_list_details_row.findViewById(R.id.date_order_details_status);
        //TextView date_order_details_id    = order_list_details_row.findViewById(R.id.date_order_details_id);
        TextView date_order_details_name = order_list_details_row.findViewById(R.id.date_order_details_name);
        //TextView date_order_details_address  = order_list_details_row.findViewById(R.id.date_order_details_address);

//        date_order_details_date.setText(this.date_order_details_name[position]);
//        date_order_details_time.setText(this.date_order_details_time[position]);
//        date_order_details_status.setText(this.date_order_details_status[position]);
        date_order_details_name.setText(this.date_order_details_name[position]);
        return order_list_details_row;
    }
}
