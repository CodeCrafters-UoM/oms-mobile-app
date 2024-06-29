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
    public String jwtToken, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        userId = getIntent().getStringExtra("sellerid");

        TextView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Profile.class);
                i.putExtra("jwtToken", jwtToken);
                i.putExtra("notificationId", notificationId);
                i.putExtra("sellerid", userId);
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
                if (response.isSuccessful()) {
                    List<NotificationCard> notifications = response.body();
                    if (notifications != null && !notifications.isEmpty()) {
                        List<NotificationCard> filteredNotifications = filterClearedNotifications(notifications);
                        notificationAdapter.setItems(filteredNotifications);
                    } else {
                        Toast.makeText(Notification.this, "Notifications are empty", Toast.LENGTH_SHORT).show();
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
                Log.e(TAG, "Error fetching notifications", t);
                Toast.makeText(Notification.this, "Error fetching Notifications: " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<NotificationCard> filterClearedNotifications(List<NotificationCard> notifications) {
        SharedPreferences sharedPreferences = getSharedPreferences("clearedNotifications", Context.MODE_PRIVATE);
        List<NotificationCard> filteredNotifications = new ArrayList<>();

        for (NotificationCard notification : notifications) {
            if (!sharedPreferences.getBoolean(notification.getId(), false)) {
                filteredNotifications.add(notification);
            }
        }

        return filteredNotifications;
    }

    class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
        private Context context;
        private List<NotificationCard> notifications;
        private String jwtToken;
        private SharedPreferences sharedPreferences;

        public NotificationAdapter(Context context, List<NotificationCard> notifications, String jwtToken) {
            this.context = context;
            this.notifications = notifications;
            this.jwtToken = jwtToken;
            this.sharedPreferences = context.getSharedPreferences("clearedNotifications", Context.MODE_PRIVATE);
        }

        public void setItems(List<NotificationCard> notifications) {
            this.notifications = notifications;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.notification_card, parent, false);
            return new NotificationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
            NotificationCard notificationCard = notifications.get(position);
            holder.message.setText(notificationCard.getMessage());

            holder.clear.setOnClickListener(v -> clearNotification(holder.getAdapterPosition()));
        }

        private void clearNotification(int position) {
            NotificationCard notificationCard = notifications.get(position);
            String notificationId = notificationCard.getId();

            // Save the cleared notification ID in SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(notificationId, true);
            editor.apply();

            // Remove the notification from the list
            notifications.remove(position);
            notifyItemRemoved(position);

            // Optionally, update the backend about the cleared notification
            updateBackend(notificationId);
        }

        private void updateBackend(String notificationId) {
            // TODO: Implement the API call to update the backend if required
        }

        @Override
        public int getItemCount() {
            return notifications.size();
        }

        class NotificationViewHolder extends RecyclerView.ViewHolder {
            private TextView message, clear;
            CardView notification_card;

            NotificationViewHolder(@NonNull View itemView) {
                super(itemView);
                message = itemView.findViewById(R.id.message);
                clear = itemView.findViewById(R.id.clear);
                notification_card = itemView.findViewById(R.id.notification_card);
            }
        }
    }

    class NotificationCard {
        @SerializedName("id")
        private String id;
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



    class User {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;

        public User(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    class Order_card {
        @SerializedName("id")
        private String id;
        @SerializedName("title")
        private String title;

        public Order_card(String id, String title) {
            this.id = id;
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
