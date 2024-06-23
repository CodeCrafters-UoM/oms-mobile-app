package com.example.deleever;

import androidx.appcompat.app.AppCompatActivity;
import static com.example.deleever.constant.Constant.BASE_URL;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deleever.APIservice2;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class forgot_password extends AppCompatActivity {

    private EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        TextView txt_logIn = findViewById(R.id.txt_logIn);
        txt_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

        username = findViewById(R.id.txt_userName);

        Button btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = username.getText().toString().trim();

                if (userName.isEmpty()) {
                    Toast.makeText(forgot_password.this, "Please fill in field", Toast.LENGTH_SHORT).show();
                    return;
                }

                forgotPassword(userName);
            }
        });
    }

    private void forgotPassword(String username) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // Replace with your actual API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIservice2 apiService = retrofit.create(APIservice2.class);
        Call<Void> call = apiService.sendUserName(username);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(forgot_password.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                    sendOTP(username);
                } else {
                    Toast.makeText(forgot_password.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(forgot_password.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace(); // Log the error
            }
        });
    }

    private void sendOTP(String username) {
        Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish(); // Close the current activity to prevent users from returning to it
    }
}
