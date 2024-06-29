package com.example.deleever;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.deleever.constant.Constant.BASE_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Notification extends AppCompatActivity {
    private static final String PREF_NAME = "MyPrefs";
    private TextView textView;
    private int notificationId;

    RecyclerView notifications_list;
    NotificationAdapter notificationAdapter;

    List<NotificationCard> notifications = new ArrayList<>();
    private Retrofit retrofit;
    private ApiService apiService;
    public String jwtToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
//        notificationId = getNotificationIdFromSharedPreferences();
//        Log.d("Profile", "Received notificationId: " + notificationId);

        TextView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Profile.class);
                i.putExtra("jwtToken",jwtToken);
                i.putExtra("notificationId",notificationId);
                startActivity(i);
            }
        });

        jwtToken = getIntent().getStringExtra("jwtToken");

        textView = findViewById(R.id.textViewData);


        String xY = getIntent().getStringExtra("message");
        textView.setText(xY);

        notifications_list = findViewById(R.id.notifications_list);
        notifications_list.setLayoutManager(new LinearLayoutManager(this));
        notificationAdapter = new NotificationAdapter(Notification.this, notifications, jwtToken);
        notifications_list.setAdapter(notificationAdapter);

        displayNotifications();

    }
    private int getNotificationIdFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("notificationId", 0); // Default value is 0
    }
    private void displayNotifications() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        Call<List<NotificationCard>> call = apiService.getNotifications("Bearer " + jwtToken);

        call.enqueue(new Callback<List<NotificationCard>>() {
            @Override
            public void onResponse(Call<List<NotificationCard>> call, Response<List<NotificationCard>> response) {

//                if (response.isSuccessful()) {
//                    List<NotificationCard> newNotifications = response.body();
//                    if (newNotifications != null && !newNotifications.isEmpty()) {
//                        // Filter out notifications that are already deleted locally
//                        List<NotificationCard> filteredNotifications = new ArrayList<>();
//                        for (NotificationCard newNotification : newNotifications) {
//                            boolean alreadyDeleted = false;
//                            for (NotificationCard localNotification : notifications) {
//                                if (newNotification.getId().equals(localNotification.getId())) {
//                                    alreadyDeleted = true;
//                                    break;
//                                }
//                            }
//                            if (!alreadyDeleted) {
//                                filteredNotifications.add(newNotification);
//                            }
//                        }
//                        notificationAdapter.setItems(filteredNotifications);
//                    } else {
//                        Toast.makeText(Notification.this, "Notifications are empty", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Log.d(TAG, "Response unsuccessful. Code: " + response.code());
//                    try {
//                        Log.d(TAG, "Error body: " + response.errorBody().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    Toast.makeText(Notification.this, "Response unsuccessful", Toast.LENGTH_SHORT).show();
//                }
//

                if (response.isSuccessful()) {
                    List<NotificationCard> notifications = response.body();
                    if (notifications != null && !notifications.isEmpty()) {
                        notificationAdapter.setItems(notifications);
                    } else {
                        Toast.makeText(Notification.this, "notifications are empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "Response unsuccessful. Code: " + response.code());
                    try {
                        Log.d(TAG, "Error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(Notification.this, "Response unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NotificationCard>> call, Throwable t) {
                Log.e(TAG, "Error fetching notification", t);
                Toast.makeText(Notification.this, "Error fetching Notifications" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    Context context;
    String jwtToken;

    List<NotificationCard> notifications = new ArrayList<>();

    public void setItems(List<NotificationCard> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    public NotificationAdapter(Context context, List<NotificationCard> notifications, String jwtToken) {
        this.context = context;
        this.notifications = notifications;
        this.jwtToken = jwtToken;
    }

    @NonNull
    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_card, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationViewHolder holder, int position) {
        NotificationCard notificationCard = notifications.get(position);
        holder.message.setText(notificationCard.getMessage());

        holder.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(holder.getAdapterPosition());
            }
        });
    }



    public void removeItem(int position) {
        notifications.remove(position);
        notifyItemRemoved(position);

    }

    @Override
    public int getItemCount() {
        if (notifications == null) {
            return 0;
        }
        return notifications.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        private TextView message,clear;
        CardView notification_card;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            clear = itemView.findViewById(R.id.clear);
            notification_card = itemView.findViewById(R.id.notification_card);
        }
    }
}

class NotificationCard {
    @SerializedName("id")
    private String id;  // Changed to String to handle UUID
    @SerializedName("message")
    private String message;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("updatedAt")
    private String updatedAt;
    @SerializedName("orderId")
    private int orderId;
    @SerializedName("user")
    private User user;
    @SerializedName("userId")
    private String userId;
    @SerializedName("read")
    private Boolean read;
    @SerializedName("order")
    private Order_card order;

    public NotificationCard(String id, String message, String createdAt, String updatedAt, int orderId, User user, String userId, Boolean read, Order_card order) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.orderId = orderId;
        this.user = user;
        this.userId = userId;
        this.read = read;
        this.order = order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Order_card getOrder() {
        return order;
    }

    public void setOrder(Order_card order) {
        this.order = order;
    }
}