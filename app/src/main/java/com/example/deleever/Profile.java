package com.example.deleever;

import static com.example.deleever.constant.Constant.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profile extends AppCompatActivity {
    private String jwtToken, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(Profile.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(Profile.this,new String[]{Manifest.permission.POST_NOTIFICATIONS},101);
            }
        }

        jwtToken = getIntent().getStringExtra("jwtToken");
        userId = getIntent().getStringExtra("sellerid");

        System.out.println("userrrr" + userId);

        TextView txt_back = findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                navigateHome();
            }
        });

        // Profile Group
//        findViewById(R.id.profile_group_container).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Profile.this, myProfile.class);
//                intent.putExtra("jwtToken", jwtToken);
//                intent.putExtra("sellerid", userId);
//                startActivity(intent);
//            }
//        });

        // Notification Group
        findViewById(R.id.notification_group_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNotification();
            }
        });

        // Contact Group
        findViewById(R.id.contact_group_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Contact_us.class);
                intent.putExtra("jwtToken", jwtToken);
                intent.putExtra("sellerid", userId);
                startActivity(intent);
            }
        });

        // Language Group
//        findViewById(R.id.language_group_container).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(profile.this, LanguageActivity.class);
////                startActivity(intent);
//            }
//        });

        // Log Out Group
//        findViewById(R.id.logOut_group_container).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(profile.this, Login.class);
//                Toast.makeText(profile.this, "Log Out", Toast.LENGTH_SHORT).show();
//                startActivity(intent);
//            }
//        });

    }

    private void navigateHome() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("jwtToken", jwtToken);
        intent.putExtra("sellerid", userId);
        startActivity(intent);
        finish(); // Close the current activity t
    }




    public void makeNotification(){

        String channelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),channelID);
        builder.setSmallIcon(R.drawable.baseline_circle_notifications_24)
                .setContentTitle("Deelever new delivery income")
                .setContentText("You have a new delivery income please check the delivery")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent i = new Intent(getApplicationContext(),Notification.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        displayNotification(i);


        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,i,PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelID);
            if(notificationChannel == null){
                int importance =  NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(channelID,"description",importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
//        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.0){
//

        notificationManager.notify(0,builder.build());

    }

    private void displayNotification(Intent i) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<Void> call = apiService.getNotifications("Bearer " + jwtToken);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    i.putExtra("sendData",response.body().toString());//can  pass api url

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}

