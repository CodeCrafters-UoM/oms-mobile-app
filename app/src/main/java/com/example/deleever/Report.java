package com.example.deleever;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
public class Report extends AppCompatActivity {
    RecyclerView report_list;
    List<ReportCard> Report_detail_list;
    ReportAdapter reportAdapter;
    Button report_to_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        report_to_home = findViewById(R.id.report_to_home);
        report_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clicked_report_to_home = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(clicked_report_to_home);            }
        });

        report_list = findViewById(R.id.report_list);
        report_list.setHasFixedSize(true);
        report_list.setLayoutManager(new GridLayoutManager(this,1));
        Report_detail_list=new ArrayList<>();

        Report_detail_list.add(new ReportCard("New Orders",40));
        Report_detail_list.add(new ReportCard("Accept Orders",34));
        Report_detail_list.add(new ReportCard("Reject Orders",4));
        Report_detail_list.add(new ReportCard("Delivered Orders",10));
        Report_detail_list.add(new ReportCard("Return Orders",3));
        Report_detail_list.add(new ReportCard("Settle Orders",20));
        Report_detail_list.add(new ReportCard("Non-settle Orders",9));
        Report_detail_list.add(new ReportCard("Closed Orders",7));

        reportAdapter = new ReportAdapter(this,Report_detail_list);
        report_list.setAdapter(reportAdapter);
    }
}

class ReportViewHolder extends RecyclerView.ViewHolder{

    public TextView Order_type,Total_order;
    public ReportViewHolder(@NonNull View itemView) {
        super(itemView);

        Order_type=itemView.findViewById(R.id.OrderType);
        Total_order=itemView.findViewById(R.id.TotalOrders);
    }
}

class ReportAdapter extends RecyclerView.Adapter<ReportViewHolder>{

    private Context context;
    private List<ReportCard> Report_detail_list;
    public  ReportAdapter(Context context,List<ReportCard> Report_detail_list){
        this.context=context;
        this.Report_detail_list=Report_detail_list;
    }
    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //PASS THE LAYOUT
        return new ReportViewHolder(LayoutInflater.from(context).inflate(R.layout.report_row,parent,false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        holder.Order_type.setText(Report_detail_list.get(position).getOrder_type());
        holder.Total_order.setText(String.valueOf(Report_detail_list.get(position).getTotal_orders()));
    }

    @Override
    public int getItemCount() {
        return Report_detail_list.size();
    }
}

class ReportCard{
    String Order_type="";
    int Total_orders =0;

    public ReportCard(String order_type, int total_orders) {
        Order_type = order_type;
        Total_orders = total_orders;
    }

    public void setOrder_type(String order_type) {
        Order_type = order_type;
    }

    public void setTotal_orders(int total_orders) {
        Total_orders = total_orders;
    }

    public String getOrder_type() {
        return Order_type;
    }

    public int getTotal_orders() {
        return Total_orders;
    }
}