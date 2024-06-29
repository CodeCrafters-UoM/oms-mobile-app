package com.example.deleever;

import androidx.appcompat.app.AppCompatActivity;
import static com.example.deleever.constant.Constant.BASE_URL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerifyOTP extends AppCompatActivity {

    private String username;
    private EditText txt_OTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp); // Assuming the correct layout file is activity_verify_otp

        username = getIntent().getStringExtra("username");
        System.out.println("user: " + username);

        txt_OTP = findViewById(R.id.txt_OTP);

        Button btn_verifyOtp = findViewById(R.id.btn_verifyOtp);
        btn_verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = txt_OTP.getText().toString().trim();
                if (otp.isEmpty()) {
                    Toast.makeText(VerifyOTP.this, "Please fill in field", Toast.LENGTH_SHORT).show();
                    return;
                }
                verifyOTP(username, otp);
            }
        });

        Button btn_resendOtp = findViewById(R.id.btn_resendOtp);
        btn_resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOTP(username);
            }
        });
    }

    private void verifyOTP(String username, String otp) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIservice2 apiService = retrofit.create(APIservice2.class);
        verifyOtpRequest reqOTP = new verifyOtpRequest(username, otp);
        Call<Void> call = apiService.VerifyOTP(reqOTP);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    System.out.println( "Response Body: hiiiiiiiiiiii " + new Gson().toJson(response.body()));
                    Toast.makeText(VerifyOTP.this, "Verify OTP successfully", Toast.LENGTH_SHORT).show();
                    navigateToResetPassword(username);
                } else {
                    Toast.makeText(VerifyOTP.this, "Failed to Verify OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(VerifyOTP.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace(); // Log the error
            }
        });
    }

    private void resendOTP(String username) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIservice2 apiService = retrofit.create(APIservice2.class);
        forgot_password.ForgotPasswordRequest reqnew = new forgot_password.ForgotPasswordRequest(username);
        Call<Void> call = apiService.sendUserName(reqnew);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(VerifyOTP.this, "Resent OTP successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VerifyOTP.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(VerifyOTP.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace(); // Log the error
            }
        });
    }

    private void navigateToResetPassword(String username) {
        Intent intent = new Intent(getApplicationContext(), resetPassword.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish(); // Close the current activity to prevent users from returning to it
    }

    public class verifyOtpRequest {
        private String username;
        private String otp;

        public verifyOtpRequest(String username, String otp) {
            this.username = username;
            this.otp = otp;
        }
    }
}
