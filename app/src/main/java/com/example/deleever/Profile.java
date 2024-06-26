//package com.example.deleever;
//
//import static androidx.constraintlayout.helper.widget.MotionEffect.SOUTH;
//import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
//import static com.example.deleever.constant.Constant.BASE_URL;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.app.NotificationCompat;
//import androidx.core.content.ContextCompat;
//
//import io.socket.client.IO;
//import io.socket.client.Socket;
//import io.socket.emitter.Emitter;
//import org.json.JSONObject;
//import org.json.JSONException;
//
//import android.Manifest;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//import android.util.Log;
//import org.json.JSONException;
//import org.json.JSONObject;
//import java.net.URISyntaxException;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.net.URISyntaxException;
//
////import io.socket.client.IO;
////import io.socket.client.Socket;
////import io.socket.emitter.Emitter;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//
//public class Profile extends AppCompatActivity implements SocketManager.NotificationListener{
//    private String jwtToken, userId,message;
//    private SocketManager socketManager;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//
//        socketManager = SocketManager.getInstance();
//        socketManager.connect();
//
////        socketManager = SocketManager.getInstance();
//////        socketManager.setNotificationListener(this);
////        socketManager.connect();
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
//            if(ContextCompat.checkSelfPermission(Profile.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(Profile.this,new String[]{Manifest.permission.POST_NOTIFICATIONS},101);
//            }
//        }
//
//
//        jwtToken = getIntent().getStringExtra("jwtToken");
//        userId = getIntent().getStringExtra("sellerid");
//
////        Intent intent = new Intent(Profile.this, SocketManager.class);
////        intent.putExtra("jwtToken", jwtToken);
////        intent.putExtra("sellerid", userId);
////        startActivity(intent);
//
//        System.out.println("userrrr" + userId);
//
//        TextView txt_back = findViewById(R.id.txt_back);
//        txt_back.setOnClickListener(new View.OnClickListener()
//
//        {
//            @Override
//            public void onClick (View v){
//                navigateHome();
//            }
//        });
//
//        // Profile Group
////        findViewById(R.id.profile_group_container).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent intent = new Intent(Profile.this, myProfile.class);
////                intent.putExtra("jwtToken", jwtToken);
////                intent.putExtra("sellerid", userId);
////                startActivity(intent);
////            }
////        });
//
//        // Notification Group
//        findViewById(R.id.notification_group_container).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                makeNotification(message);
//                System.out.println(message+"  messs");
//            }
//        });
//
//        // Contact Group
//        findViewById(R.id.contact_group_container).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Profile.this, Contact_us.class);
//                intent.putExtra("jwtToken", jwtToken);
//                intent.putExtra("sellerid", userId);
//                startActivity(intent);
//            }
//        });
//
//        // Language Group
////        findViewById(R.id.language_group_container).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
//////                Intent intent = new Intent(profile.this, LanguageActivity.class);
//////                startActivity(intent);
////            }
////        });
//
//        // Log Out Group
////        findViewById(R.id.logOut_group_container).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent intent = new Intent(profile.this, Login.class);
////                Toast.makeText(profile.this, "Log Out", Toast.LENGTH_SHORT).show();
////                startActivity(intent);
////            }
////        });
//
//    }
//
//    private void navigateHome() {
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.putExtra("jwtToken", jwtToken);
//        intent.putExtra("sellerid", userId);
//        startActivity(intent);
//        finish(); // Close the current activity t
//    }
//
//
//
//
//    public void makeNotification(String message){
//        String notificationmsg = onNotificationReceived(message);
//        System.out.println(notificationmsg+"  messs");
//
//        String channelID = "CHANNEL_ID_NOTIFICATION";
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),channelID);
//        builder.setSmallIcon(R.drawable.baseline_circle_notifications_24)
//                .setContentTitle("Deelever new delivery income")
//                .setContentText("You have a new delivery income please check the delivery")
//                .setAutoCancel(true)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        Intent i = new Intent(getApplicationContext(),Notification.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.putExtra("sendData",notificationmsg);
////        SocketManager.displayNotification(i,jwtToken);
//
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,i,PendingIntent.FLAG_MUTABLE);
//        builder.setContentIntent(pendingIntent);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelID);
//            if(notificationChannel == null){
//                int importance =  NotificationManager.IMPORTANCE_HIGH;
//                notificationChannel = new NotificationChannel(channelID,"description",importance);
//                notificationChannel.setLightColor(Color.GREEN);
//                notificationChannel.enableVibration(true);
//                notificationManager.createNotificationChannel(notificationChannel);
//            }
//        }
////        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.0){
////
//
//        notificationManager.notify(0,builder.build());
//
//    }
//
//    @Override
//    public String onNotificationReceived(String message) {
//        return message;
//
//    }
//}
//
//
//class SocketManager {
//
//    private static SocketManager instance;
//    private Socket socket;
//    private final String TAG = "SocketManager";
//    private NotificationListener notificationListener;
////    String jwtToken = getInstance().jwtToken;
//
//
//    private SocketManager() {
//        try {
//            socket = IO.socket(BASE_URL);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static SocketManager getInstance() {
//        if (instance == null) {
//            instance = new SocketManager();
//        }
//        return instance;
//    }
//
//    public void setNotificationListener(NotificationListener listener) {
//        this.notificationListener = listener;
//    }
//
//    public void connect() {
//        socket.connect();
//        socket.on(Socket.EVENT_CONNECT, onConnect);
//        socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
//        socket.on("notification", sendNotification);
//    }
//
//    private Emitter.Listener onConnect = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            Log.d(TAG, "Connected to Socket.IO server");
//        }
//    };
//
//    private Emitter.Listener onDisconnect = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            Log.d(TAG, "Disconnected from Socket.IO server");
//        }
//    };
//
//    private Emitter.Listener sendNotification = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            JSONObject data = (JSONObject) args[0];
//            try {
//                String message = data.getString("message");
//                Log.d(TAG, "Received notification: " + message);
////                handleNotificationMessage(message);
//                if (notificationListener != null) {
//                    notificationListener.onNotificationReceived(message);
//                    System.out.println(message);
//                }
//
//
//                // Handle the notification as needed
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Log.d(TAG, "printStackTrace: " + e);
//
//            }
//        }
//
//
//
//        };
//
////        private void createNotification(String message) {
////
////            Retrofit retrofit = new Retrofit.Builder()
////                    .baseUrl(BASE_URL)
////                    .addConverterFactory(GsonConverterFactory.create())
////                    .build();
////            ApiService apiService = retrofit.create(ApiService.class);
////
////            Call<Void> call = apiService.createNotification("Bearer " + jwtToken);
////            call.enqueue(new Callback<Void>() {
////                @Override
////                public void onResponse(Call<Void> call, Response<Void> response) {
////                    Log.d(TAG, "mas "+response.body());
////                    if(response.isSuccessful()){
////                        i.putExtra("sendData",response.body().toString());//can  pass api url
////                        Toast.makeText(Profile.this,"notif  "+response.body().toString(),Toast.LENGTH_SHORT).show();
////                        Log.d(TAG, "mas "+response.body().toString());
////                    }
////                }
////
////                @Override
////                public void onFailure(Call<Void> call, Throwable t) {
////
////                }
////            });
////        }
//
//
//
//    public static void displayNotification(Intent i,String jwtToken,String message) {
//
//    }
//    public void disconnect() {
//        socket.disconnect();
//    }
//
//    public interface NotificationListener {
//        String onNotificationReceived(String message);
//    }
//}
//
////private void handleNotificationMessage(String message) {
////    Retrofit retrofit = new Retrofit.Builder()
////            .baseUrl(BASE_URL)
////            .addConverterFactory(GsonConverterFactory.create())
////            .build();
////    ApiService apiService = retrofit.create(ApiService.class);
////
////    Call<Void> call = apiService.getNotifications("Bearer " + jwtToken);
////    call.enqueue(new Callback<Void>() {
////        @Override
////        public void onResponse(Call<Void> call, Response<Void> response) {
////            Log.d(TAG, "mas " + response.body());
////            if (response.isSuccessful()) {
////                Log.d(TAG, "mas " + response.body().toString());
////            }
////        }
////
////        @Override
////        public void onFailure(Call<Void> call, Throwable t) {
////
////        }
////    });
////
////};
//
//
package com.example.deleever;

import static com.example.deleever.constant.Constant.BASE_URL;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URISyntaxException;
public class Profile extends AppCompatActivity implements SocketManager.NotificationListener {
    private String jwtToken, userId, message;
    private SocketManager socketManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        jwtToken = getIntent().getStringExtra("jwtToken");
        userId = getIntent().getStringExtra("sellerid");

        socketManager = SocketManager.getInstance();
        socketManager.setNotificationListener(this);
        socketManager.connect();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(Profile.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Profile.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        // Notification Group
        findViewById(R.id.notification_group_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNotification(message);
                Intent i = new Intent(getApplicationContext(), Notification.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.putExtra("sendData", message);
        i.putExtra("jwtToken",jwtToken);
                System.out.println("  makeNotification  "+message  );
                startActivity(i);
            }
        });

      
   

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

        // Language Group
//        findViewById(R.id.language_group_container).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(profile.this, LanguageActivity.class);
////                startActivity(intent);
//            }
//        });

        // Log Out Group
        findViewById(R.id.logOut_group_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });

    }


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
        finish(); // Finish the current activity to prevent users from returning to it after logging out
    }

    private void navigateHome() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("jwtToken", jwtToken);
        intent.putExtra("sellerid", userId);
        startActivity(intent);
        finish(); // Close the current activity to prevent users from returning to it
    }   

    public void makeNotification(String message) {
        if (message == null) {
            return;  // Handle the case when message is null
        }

        String channelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelID);
        builder.setSmallIcon(R.drawable.baseline_circle_notifications_24)
                .setContentTitle("Deelever new delivery income")
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent i = new Intent(getApplicationContext(), Notification.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.putExtra("sendData", message);
//        i.putExtra("jwtToken",jwtToken);
//        System.out.println("  sendData  "+message  );

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelID);
            if (notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(channelID, "description", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(0, builder.build());
    }

    @Override
    public String onNotificationReceived(String message) {
        this.message = message;
        makeNotification(message);
        return message;
    }
}

class SocketManager {
    private static volatile SocketManager instance;
    private Socket socket;
    private final String TAG = "SocketManager";
    private NotificationListener notificationListener;

    private SocketManager() {
        try {
            socket = IO.socket(BASE_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static SocketManager getInstance() {
        if (instance == null) {
            synchronized (SocketManager.class) {
                if (instance == null) {
                    instance = new SocketManager();
                }
            }
        }
        return instance;
    }

    public void setNotificationListener(NotificationListener listener) {
        this.notificationListener = listener;
    }

    public void connect() {
        socket.connect();
        socket.on(Socket.EVENT_CONNECT, onConnect);
        socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.on("notification", sendNotification);
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "Connected to Socket.IO server");
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "Disconnected from Socket.IO server");
        }
    };

    private Emitter.Listener sendNotification = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            try {
                String message = data.getString("message");
                Log.d(TAG, "Received notification: " + message);
                if (notificationListener != null) {
                    notificationListener.onNotificationReceived(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, "printStackTrace: " + e);
            }
        }
    };

    public void disconnect() {
        socket.disconnect();
    }

    public interface NotificationListener {
        String onNotificationReceived(String message);
    }
}
