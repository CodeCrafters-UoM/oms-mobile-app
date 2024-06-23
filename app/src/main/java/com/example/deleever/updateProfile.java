package com.example.deleever;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deleever.ProfileResponse;
import com.example.deleever.myProfile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.deleever.constant.Constant.*;
import static com.example.deleever.constant.Constant.*;


public class updateProfile extends AppCompatActivity {

    private EditText editName, editEamil, editBusinessName, editContactNumber, editUserName, editRole;
    private String jwtToken, userId;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        editName = findViewById(R.id.txt_nameHint);
        editEamil = findViewById(R.id.txt_emailHint);
        editBusinessName = findViewById(R.id.txt_business_nameHint);
        editContactNumber = findViewById(R.id.txt_contact_numberHint);
        editUserName = findViewById(R.id.txt_usernameHint);
        editRole = findViewById(R.id.txt_roleHint);
        btnSave = findViewById(R.id.btn_save);

        // Disable the role and username fields
        editUserName.setEnabled(false);
        editRole.setEnabled(false);

        TextView txt_back = findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                navigateMyProfile();
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("jwtToken")) {
            String name = intent.getStringExtra("name");
            String email = intent.getStringExtra("email");
            String businessName = intent.getStringExtra("businessName");
            String contactNumber = intent.getStringExtra("contactNumber");
            String role = intent.getStringExtra("role");
            String userName = intent.getStringExtra("userName");
            jwtToken = intent.getStringExtra("jwtToken");
            userId = intent.getStringExtra("sellerid");

            editName.setText(name);
            editEamil.setText(email);
            editBusinessName.setText(businessName);
            editContactNumber.setText(contactNumber);
            editRole.setText(role);
            editUserName.setText(userName);

        } else {
            Toast.makeText(this, "No profile details received", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    private void updateProfile() {
        String updatedName = editName.getText().toString().trim();
        String updatedEmail = editEamil.getText().toString().trim();
        String updatedBusinessName = editBusinessName.getText().toString().trim();
        String updatedContactNumber = editContactNumber.getText().toString().trim();


        if (updatedName.isEmpty() || updatedEmail.isEmpty() || updatedBusinessName.isEmpty() || updatedContactNumber.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        ProfileResponse updatedProfile = new ProfileResponse();
        updatedProfile.setName(updatedName);
        updatedProfile.setEmail(updatedEmail);
        updatedProfile.setBusinessName(updatedBusinessName);
        updatedProfile.setContactNumber(updatedContactNumber);

        updateProfileMethod(userId, updatedProfile);
    }

    private void updateProfileMethod(String userId, ProfileResponse updatedProfile) {
        Retrofit retrofit = updateProduct.RetrofitClient.getClient(BASE_URL); // Replace with your actual API base URL
        APIservice2 apiService = retrofit.create(APIservice2.class);
        Call<ProfileResponse> call = apiService.updateProfile("Bearer " + jwtToken, userId, updatedProfile); // Pass JWT token to API
        call.enqueue(new Callback<ProfileResponse>() {

            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(updateProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    navigateMyProfile();
                } else {
                    Log.e("UpdateProfileError", "Failed to update Profile: " + response.message());
                    Toast.makeText(updateProfile.this, "Failed to update Profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(updateProfile.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateMyProfile() {
        Intent intent = new Intent(getApplicationContext(), myProfile.class);
        intent.putExtra("jwtToken", jwtToken);
        intent.putExtra("sellerid", userId);
        startActivity(intent);
        finish(); // Close the current activity to prevent users from returning to it
    }

}