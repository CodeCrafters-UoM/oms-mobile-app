package com.example.deleever;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.deleever.constant.Constant.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Notification extends AppCompatActivity {

    private TextView textView,noti;
    private Retrofit retrofit;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        textView = findViewById(R.id.textViewData);
        noti = findViewById(R.id.noti);

        String x = getIntent().getStringExtra("sendData");
        noti.setText(x);

        System.out.println("xx "+x);
        // Initialize Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        int userId = getIntent().getIntExtra("userId", 1); // Replace with actual user ID
        fetchNotifications(userId);
    }

    private void fetchNotifications(int userId) {
        Call<List<NotificationCard>> call = apiService.getNotifications(userId);
        call.enqueue(new Callback<List<NotificationCard>>() {
            @Override
            public void onResponse(Call<List<NotificationCard>> call, Response<List<NotificationCard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NotificationCard> notifications = response.body();
                    displayNotifications(notifications);
                } else {
                    Log.e(TAG, "Response unsuccessful or body is null");
                }
            }

            @Override
            public void onFailure(Call<List<NotificationCard>> call, Throwable t) {
                Log.e(TAG, "Error fetching notifications", t);
            }
        });
    }

    private void displayNotifications(List<NotificationCard> notifications) {
        StringBuilder notificationText = new StringBuilder();
        for (NotificationCard notification : notifications) {
            notificationText.append(notification.getMessage())
                    .append(" - ")
                    .append(notification.getCreatedAt())
                    .append("\n");
        }
        textView.setText(notificationText.toString());
    }
}



class NotificationCard {
    @SerializedName("userId")
    private int id;
    private String message;
    private String createdAt;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
