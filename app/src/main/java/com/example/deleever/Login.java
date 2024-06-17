package com.example.deleever;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.sql.SQLOutput;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    private static final String BASE_URL = "http://192.168.91.146:8000/api/v1/";

    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_TOKEN = "jwtToken";

    private static final String KEY_SELLER_ID = "sellerId";

    private EditText txt_userName;
    private EditText txt_passWord;
    private TextView txt_showPassword,txt_registerHere;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        txt_userName = findViewById(R.id.txt_userName);
        txt_passWord = findViewById(R.id.txt_passWord);
        txt_showPassword = findViewById(R.id.txt_showPassword);
        txt_registerHere =  findViewById(R.id.txt_registerHere);


        txt_registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(Login.this, Signup.class);
                startActivity(register);
            }
        });


        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

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


                                // Store the token and seller ID
                                storeToken(receivedToken);
                                storeSellerId(user.getId());

                                // Navigate to Home_page activity
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
        TextView txt_forgetPassword = findViewById(R.id.txt_forgetPassword);
        txt_forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ForgotPassword activity
                Intent intent = new Intent(Login.this, forgot_password.class);
                startActivity(intent);
            }
        });

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
        // Pass token to Home_page activity
        Intent intent = new Intent(Login.this, MainActivity.class);
        String token = retrieveToken();
        String sellerId = retrieveSellerId();
        intent.putExtra("jwtToken", token); // Add token as an extra to the intent
        intent.putExtra("sellerid", sellerId);
        System.out.println("seler id  " + sellerId);
        startActivity(intent);
        finish(); // Finish MainActivity to prevent going back
    }
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
