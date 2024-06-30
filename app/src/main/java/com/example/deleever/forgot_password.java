package com.example.deleever;

import androidx.appcompat.app.AppCompatActivity;
import static com.example.deleever.constant.Constant.BASE_URL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deleever.APIservice2;
import com.google.gson.internal.bind.util.ISO8601Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class forgot_password extends AppCompatActivity {

    private static final String TAG = "ForgotPasswordActivity";
    private EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        TextView txtLogIn = findViewById(R.id.txt_logIn);
        txtLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });

        username = findViewById(R.id.txt_userName);

        Button btnSubmit = findViewById(R.id.btn_sendOtp);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleForgotPassword();
            }
        });

    }

    private void navigateToLogin() {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }

    private void handleForgotPassword() {
        String userName = username.getText().toString().trim();

        if (userName.isEmpty()) {
            Toast.makeText(forgot_password.this, "Please fill in the username field", Toast.LENGTH_SHORT).show();
            return;
        }
        forgotPassword(userName);
    }

    private void forgotPassword(String username) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIservice2 apiService = retrofit.create(APIservice2.class);
        ForgotPasswordRequest request = new ForgotPasswordRequest(username);
        Call<Void> call = apiService.sendUserName(request);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(forgot_password.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                    sendOTP(username);
                } else {
                    Log.e(TAG, "API error: " + response.code());
                    Toast.makeText(forgot_password.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage());
                Toast.makeText(forgot_password.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendOTP(String username) {
        Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
    static class ForgotPasswordRequest {
        private String username;

        public ForgotPasswordRequest(String username) {
            this.username = username;
        }
    }
}
