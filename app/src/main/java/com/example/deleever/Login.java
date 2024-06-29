package com.example.deleever;

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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.example.deleever.constant.Constant.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;


public class Login extends AppCompatActivity implements NotificationListener {

    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_TOKEN = "jwtToken";
    private static final String KEY_SELLER_ID = "sellerId";
    private static final String KEY_MESSAGE_COUNT = "messageCount";


    private EditText txt_userName;
    private EditText txt_passWord;
    private TextView txt_showPassword,txt_registerHere;
    private boolean isPasswordVisible = false;


    private SocketManager socketManager;
    private String userId, message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_registerHere = findViewById(R.id.txt_registerHere);

        txt_registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Signup.class);
                startActivity(i);
            }
        });
        socketManager = SocketManager.getInstance();
        socketManager.setNotificationListener(this); // Set the listener

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(Login.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
        socketManager.connect();


        // Initialize views
        txt_userName = findViewById(R.id.txt_userName);
        txt_passWord = findViewById(R.id.txt_passWord);
        txt_showPassword = findViewById(R.id.txt_showPassword);

        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TextView txt_forgot = findViewById(R.id.txt_forgetPassword);
        txt_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), forgot_password.class);
                startActivity(intent);
            }
        });

        // Create ApiService instance
        APIservice2 apiService = retrofit.create(APIservice2.class);

        // Set onClickListener for Login button
        Button btn_logIn = findViewById(R.id.btn_logIn);
        btn_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get username and password from EditText fields
                String username = txt_userName.getText().toString().trim();
                String password = txt_passWord.getText().toString().trim();

                // Create a LoginRequest object with username and password
                LoginRequest loginRequest = new LoginRequest(username, password);

                // Make API call
                Call<LoginResponse> call = apiService.LogIn(loginRequest);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            LoginResponse loginResponse = response.body();
                            if (loginResponse != null) {
                                Log.e("Response Body", new Gson().toJson(loginResponse));
                                Log.e("Login", "Login successful");
                                Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();

                                // Get the JWT token and seller ID from the response
                                String receivedToken = loginResponse.getToken();
                                User user = loginResponse.getUser();

                                // Display user information (you can update TextViews or any UI element accordingly)
//                                if (user != null) {
//                                    Log.e("User", "ID: " + user.getId());
//                                    Log.e("User", "Username: " + user.getUsername());
//                                    Log.e("User", "Role: " + user.getRole());
//                                    Log.e("User", "Email: " + user.getEmail());
//                                    Log.e("User", "Image: " + user.getImage());
//                                    // Update UI elements with user information if needed
//                                }
//
//                                // Store the token and seller ID
                                storeToken(receivedToken);
                                storeSellerId(user.getId());

                                // Navigate to MainActivity activity
                                navigateToHomePage();
                            }
                        } else {
                            Log.e("Login", "Login failed");
                            Log.e("Login", "Login failed with response code: " + response.code());
                            Log.e("Login", "Error message: " + response.message());
                            Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                            // Handle login failure (show error message, etc.)
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.e("Login", "API call failed: " + t.getMessage());
                        // Handle API call failure
                    }
                });
            }
        });

        // Set onClickListener for Forget Password TextView
//        TextView txt_forgetPassword = findViewById(R.id.txt_forgetPassword);
//        txt_forgetPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Navigate to ForgotPassword activity
//                Intent intent = new Intent(Login.this, ForgotPassword.class);
//                startActivity(intent);
//            }
//        });

        // Set onClickListener for Show/Hide Password TextView
        txt_showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    txt_passWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isPasswordVisible = false;
                    txt_showPassword.setText("Show Password");
                } else {
                    txt_passWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isPasswordVisible = true;
                    txt_showPassword.setText("Hide Password");
                }
            }
        });
    }

    private void storeToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    private void storeSellerId(String sellerId) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SELLER_ID, sellerId);
        editor.apply();
    }

    private String retrieveToken() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    private String retrieveSellerId() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_SELLER_ID, null);
    }


    private void navigateToHomePage() {
        // Pass token to MainActivity activity
        Intent intent = new Intent(Login.this, MainActivity.class);
        String token = retrieveToken();
        String sellerId = retrieveSellerId();
        intent.putExtra("jwtToken", token); // Add token as an extra to the intent
        intent.putExtra("sellerid", sellerId);
        System.out.println("seler id  " + sellerId);
        startActivity(intent);
        finish(); // Finish Login to prevent going back
    }
    @Override
    public void onNotificationReceived(String message) {
        this.message = message;

        if (message != null && !message.isEmpty()) {
            createNotification(message);
            // Pass messageCount to Profile activity
//            int messageCount = socketManager.getMessageCount();
//            storeMessageCount(messageCount);
//            System.out.println("before "+messageCount);
        }

//        System.out.println("before "+socketManager.getMessageCount());

    }
    int notificationId = 0; // Initialize a global notification ID counter

    private void createNotification(String message) {

        String channelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelID);
        builder.setSmallIcon(R.drawable.bell_regular);
        builder.setContentTitle("Deleever");
        builder.setContentText(message); // Set the message received from the socket
        builder.setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_DEFAULT);

//        Intent intent = new Intent(this, Notification.class);
//        intent.putExtra("message", message); // Pass the notification message to the activity
//
//        // Create a PendingIntent
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pendingIntent);


//        intent.putExtra("message", message); // Pass the notification message to the activity
//
//        // Create a PendingIntent
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pendingIntent);
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

        notificationManager.notify(notificationId++, builder.build());


        int messageCount = notificationId;
//
        Log.d("Message Count", "Message count  receiving new message: " + messageCount);
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("messageCount", messageCount);
        editor.apply();
    }

//    private void storeMessageCount(int messageCount) {
//        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt(KEY_MESSAGE_COUNT, messageCount);
//        editor.apply();
//    }
//    private void storeNotificationId(int i) {
//        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("notificationId", notificationId);
//        editor.apply();
//    }

//    private int getNotificationIdFromSharedPreferences() {
//        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//        return sharedPreferences.getInt(KEY_MESSAGE_COUNT, 0);
//    }
}



class LoginRequest {
    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class LoginResponse {
    @SerializedName("token")
    private String token;

    @SerializedName("user")
    private User user;

    // Getters and setters for token and user
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

class User {
    @SerializedName("id")
    private String id;

    @SerializedName("username")
    private String username;

    @SerializedName("role")
    private String role;

    @SerializedName("email")
    private String email;

    @SerializedName("image")
    private String image;

    // Getters and setters for user fields
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
interface NotificationListener {
    void onNotificationReceived(String message);
}



class SocketManager {
    private static  SocketManager instance;
    private Socket socket;
    private final String TAG = "SocketManager";
    private NotificationListener notificationListener; // Listener instance
    int messageCount = 0;

    private SocketManager() {
        try {
            Log.d(TAG, "Connecting to Socket.IO server at: " + BASE_URL);
            String baseUrl = "https://api.deleever.one"; // Ensure this URL is correct

            socket = IO.socket(baseUrl);
        } catch (URISyntaxException e) {
            Log.e(TAG, "Socket URI syntax error: " + e.getMessage());
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
        socket.on(Socket.EVENT_CONNECT_ERROR, onError);
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

    private Emitter.Listener onError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "Error connecting to Socket.IO server: " + args[0]);
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
                    if(!message.isEmpty()){
                        messageCount +=1;

                    }else {
                        messageCount=0;
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, "JSON error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    };

    public void disconnect() {
        socket.disconnect();
    }

//    public int getMessageCount() {
//        Log.d(TAG, "Received notification:getMessageCount  " + messageCount);
//
//        return messageCount;
//    }
}
