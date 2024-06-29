package com.example.deleever;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.example.deleever.constant.Constant.*;




public class Report extends AppCompatActivity {

    private static final String TAG = "ReportActivity";
    private String jwtToken;

    private RecyclerView reportList;
    private ReportAdapter reportAdapter;
    private List<ReportCard.OrderStatusCounts> reportCards = new ArrayList<>();
    private TextView username, businessName, created_date,txt_back;
    private Button reportToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        jwtToken = getIntent().getStringExtra("jwtToken");

        txt_back = findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                i.putExtra("jwtToken",jwtToken);
                startActivity(i);
            }
        });
        username = findViewById(R.id.username);
        businessName = findViewById(R.id.businessName);
        created_date = findViewById(R.id.created_date);
        reportList = findViewById(R.id.report_list);

        reportList.setLayoutManager(new LinearLayoutManager(this));
        reportAdapter = new ReportAdapter(this, reportCards);
        reportList.setAdapter(reportAdapter);

        setCurrentDate();
        fetchReportDetails();

    }

    private void setCurrentDate() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);
        created_date.setText(formattedDate);
    }

    private void fetchReportDetails() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<ReportCard> call = apiService.getReportDetails("Bearer " + jwtToken);

        call.enqueue(new Callback<ReportCard>() {
            @Override
            public void onResponse(Call<ReportCard> call, Response<ReportCard> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ReportCard reportCard = response.body();
                    username.setText(reportCard.getUserName());
                    businessName.setText(reportCard.getBusinessName());
                    reportAdapter.setItems(reportCard.getOrderStatusCounts());
                } else {
                    Toast.makeText(Report.this, "Failed to fetch report details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportCard> call, Throwable t) {
                Toast.makeText(Report.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error fetching data", t);
            }
        });
    }
}


class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private Context context;
    private List<ReportCard.OrderStatusCounts> reportCard;

    public ReportAdapter(Context context, List<ReportCard.OrderStatusCounts> reportCard) {
        this.context = context;
        this.reportCard = reportCard;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.report_row, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        ReportCard.OrderStatusCounts orderStatus = reportCard.get(position);
        holder.OrderStatus.setText(orderStatus.getTitle());
        holder.TotalOrders.setText(String.valueOf(orderStatus.getNumber()));
    }

    @Override
    public int getItemCount() {
        return reportCard.size();
    }

    public void setItems(List<ReportCard.OrderStatusCounts> reportCards) {
        this.reportCard = reportCards;
        notifyDataSetChanged();
    }

    class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView OrderStatus, TotalOrders;

        ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            OrderStatus = itemView.findViewById(R.id.OrderStatus);
            TotalOrders = itemView.findViewById(R.id.TotalOrders);
        }
    }
}

class ReportCard {
    @SerializedName("userName")
    private String userName;

    @SerializedName("businessName")
    private String businessName;

    @SerializedName("orderStatusCounts")
    private List<OrderStatusCounts> orderStatusCounts;

    public String getUserName() {
        return userName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public List<OrderStatusCounts> getOrderStatusCounts() {
        return orderStatusCounts;
    }

    public static class OrderStatusCounts {
        @SerializedName("title")
        private String title;

        @SerializedName("number")
        private int number;

        public String getTitle() {
            return title;
        }

        public int getNumber() {
            return number;
        }
    }
}

