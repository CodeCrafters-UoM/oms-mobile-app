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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class resetPassword extends AppCompatActivity {

    private String username;

    private EditText txt_passWord, txt_conpassWord;
    private Button btn_resetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        txt_passWord = findViewById(R.id.txt_passWord);
        txt_conpassWord = findViewById(R.id.txt_conpassWord);
        btn_resetPassword = findViewById(R.id.btn_resPassword);


        username = getIntent().getStringExtra("username");

        btn_resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = txt_passWord.getText().toString().trim();
                String confirmPassword = txt_conpassWord.getText().toString().trim();

                if (validateInputs(newPassword, confirmPassword)) {
                    resetUserPassword(username, newPassword);
                }
            }
        });

        TextView txt_back = findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToVerifyOTP(username);
            }
        });

    }

    private boolean validateInputs(String password, String confirmPassword) {
        if (password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(resetPassword.this, "Both fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 8) {
            Toast.makeText(resetPassword.this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(resetPassword.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void resetUserPassword(String username, String newPassword) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIservice2 apiService = retrofit.create(APIservice2.class);
        resetPasswordRequest resetPass = new resetPasswordRequest(username, newPassword);
        System.out.println("sssssssss" + resetPass.username + resetPass.newPassword);
        Call<Void> call = apiService.resetPassword(resetPass);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(resetPassword.this, "Password reset successfully", Toast.LENGTH_SHORT).show();
                    // Optionally, navigate to another activity, e.g., login
                    finish(); // Close the current activity
                } else {
                    Toast.makeText(resetPassword.this, "Failed to reset password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(resetPassword.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace(); // Log the error
            }
        });
    }

    private void navigateToVerifyOTP(String username) {
        Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish(); // Close the current activity to prevent users from returning to it
    }

    public class resetPasswordRequest {
        private String username;
        private String newPassword;

        public resetPasswordRequest(String username, String newPassword) {
            this.username = username;
            this.newPassword = newPassword;
        }
    }
}