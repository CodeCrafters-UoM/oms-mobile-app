package com.example.deleever;

import static com.example.deleever.constant.Constant.BASE_URL;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import org.json.JSONObject;
import org.json.JSONException;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URISyntaxException;
import androidx.cardview.widget.CardView;
import android.view.View;
public class Profile extends AppCompatActivity {
    private String jwtToken, userId;
    CardView noti_round;

    int messageCount =0;
    TextView Count,txt_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        noti_round = findViewById(R.id.noti_round);
        Count = findViewById(R.id.Count);
        txt_back = findViewById(R.id.txt_back);

        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                i.putExtra("jwtToken",jwtToken);
                startActivity(i);
            }
        });

        noti_round.setVisibility(View.GONE);
//
        jwtToken = getIntent().getStringExtra("jwtToken");
        userId = getIntent().getStringExtra("sellerId");

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        jwtToken = sharedPreferences.getString("jwtToken", null);
        userId = sharedPreferences.getString("sellerId", null);

//        if (jwtToken == null || userId == null) {
//            // Handle case where token or user ID is not found
//            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }


        messageCount = sharedPreferences.getInt("messageCount", 0);

        noti_round.setVisibility(View.GONE);
        String x = String.valueOf(messageCount);
        Count.setText(x);
        if(!x.isEmpty()) {
            noti_round.setVisibility(View.VISIBLE);
        }

//        if(messageCount<=0){
//            noti_round.setVisibility(View.GONE);
//            Count.setText(String.valueOf(messageCount));
//
//        }else {
//            noti_round.setVisibility(View.VISIBLE);
//            Count.setText(String.valueOf(messageCount));
//        }
//               noti_round.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                noti_round.setVisibility(View.GONE);
//                Count.setText("");
//            }
//        });


        Log.d("Message Count", "Message count after receiving new message profile : " + messageCount);

        Log.d("Message Count", "Message count after receiving new  profile:Count " + messageCount);



//        if(messageCount != 0){
//            noti_round.setVisibility(View.VISIBLE);
//            Count.setText(String.valueOf(messageCount));
//            Log.d("Message Count", "Message count after receiving new  profile:Count " + Count);
//
//        }
//        else {
//            noti_round.setVisibility(View.GONE);
//        }


//        socketManager = SocketManager.getInstance();
//        socketManager.setNotificationListener(this); // Set the listener
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (ContextCompat.checkSelfPermission(Profile.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(Profile.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
//            }
//        }




//        socketManager.connect();



        // Notification Group
        findViewById(R.id.notification_group_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Notification.class);
                i.putExtra("jwtToken", jwtToken);
                i.putExtra("sellerid", userId);
                noti_round.setVisibility(View.GONE);
                Count.setText("");
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("messageCount", 0); // Set message count to 0
                editor.apply();
                startActivity(i);

            }
        });

        if(messageCount == 0){
            noti_round.setVisibility(View.GONE);
        }

        // Profile Group
        findViewById(R.id.profile_group_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, myProfile.class);
                intent.putExtra("jwtToken", jwtToken);
                intent.putExtra("sellerid", userId);
                startActivity(intent);
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

        // Log Out Group
        findViewById(R.id.logOut_group_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });

        TextView txt_back = findViewById(R.id.txt_back);

        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, MainActivity.class);
                intent.putExtra("jwtToken", jwtToken);
                intent.putExtra("sellerid", userId);
                startActivity(intent);
            }
        });
    }

    //    private void makeNotifications(String message) {
//        String channelID = "CHANNEL_ID_NOTIFICATION";
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelID);
//        builder.setSmallIcon(R.drawable.bell_regular);
//        builder.setContentTitle("Notification Title");
//        builder.setContentText(message); // Set the message received from the socket
//        builder.setAutoCancel(true)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
////        Intent intent = new Intent(getApplicationContext(), Notification.class);
////        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////        intent.putExtra("send data", "send data value");
////
////        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
////        builder.setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelID);
//            if (notificationChannel == null) {
//                int importance = NotificationManager.IMPORTANCE_HIGH;
//                notificationChannel = new NotificationChannel(channelID, "description", importance);
//                notificationChannel.setLightColor(Color.GREEN);
//                notificationChannel.enableVibration(true);
//                notificationManager.createNotificationChannel(notificationChannel);
//            }
//        }
//
//        notificationManager.notify(0, builder.build());
//    }
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setMessage("Are you sure you want to log out?")
                .setTitle("Log Out Confirmation");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                performLogout();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void performLogout() {
        Intent intent = new Intent(Profile.this, Login.class);
        Toast.makeText(Profile.this, "Logging Out", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }

    private void navigateHome() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("jwtToken", jwtToken);
        intent.putExtra("sellerid", userId);
        startActivity(intent);
        finish();
    }

//    @Override
//    public void onNotificationReceived(String message) {
//        this.message = message;
//        makeNotifications(message); // Update notification when message is receive
//
//
//    }


}