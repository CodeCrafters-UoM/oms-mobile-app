package com.example.deleever;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.example.deleever.constant.Constant.*;

public class myProfile extends AppCompatActivity {

    private String jwtToken, userId;

    private TextView txt_name, txt_email, txt_userName, txt_role, txt_businessName, txt_contactNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        txt_name = findViewById(R.id.txt_nameHint);
        txt_email = findViewById(R.id.txt_emailHint);
        txt_userName = findViewById(R.id.txt_usernameHint);
        txt_role = findViewById(R.id.txt_roleHint);
        txt_businessName = findViewById(R.id.txt_business_nameHint);
        txt_contactNumber = findViewById(R.id.txt_contact_numberHint);

        jwtToken = getIntent().getStringExtra("jwtToken");
        userId = getIntent().getStringExtra("sellerid");

        System.out.println("userid" + userId);

        TextView txt_back = findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                navigateProfile();
            }
        });

        fetchProfile();

        Button btn_update = findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Product_details", "Update button clicked");
                Intent updateIntent = new Intent(myProfile.this, com.example.deleever.updateProfile.class);
                updateIntent.putExtra("jwtToken", jwtToken);
                updateIntent.putExtra("sellerid", userId);
                updateIntent.putExtra("name", txt_name.getText().toString());
                updateIntent.putExtra("email", txt_email.getText().toString());
                updateIntent.putExtra("businessName", txt_businessName.getText().toString());
                updateIntent.putExtra("contactNumber", txt_contactNumber.getText().toString());
                updateIntent.putExtra("userName", txt_userName.getText().toString());
                updateIntent.putExtra("role", txt_role.getText().toString());
                startActivity(updateIntent);
            }
        });
    }

    private void fetchProfile() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // Replace with your actual API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIservice2 apiService = retrofit.create(APIservice2.class);

        Call<ProfileResponse> call = apiService.getProfile("Bearer " + jwtToken, userId);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful()) {
                    ProfileResponse profile = response.body();
                    if (profile != null) {
                        txt_name.setText(profile.getName());
                        txt_email.setText(profile.getEmail());
                        txt_userName.setText(profile.getUsername());
                        txt_role.setText(profile.getRole());
                        txt_businessName.setText(profile.getBusinessName());
                        txt_contactNumber.setText(profile.getContactNumber());
                    }
                } else {
                    Toast.makeText(myProfile.this, "Failed to load profile", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(myProfile.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void navigateProfile() {
        Intent intent = new Intent(getApplicationContext(), Profile.class);
        intent.putExtra("jwtToken", jwtToken);
        intent.putExtra("sellerid", userId);
        startActivity(intent);
        finish(); // Close the current activity to prevent users from returning to it
    }

}



class ProfileResponse  {
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("username")
    private String username;
    @SerializedName("role")
    private String role;
    @SerializedName("businessName")
    private String businessName;
    @SerializedName("contactNumber")
    private String contactNumber;

    // Getters
    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() { return email; }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUsername() { return username; }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getRole() { return role; }
    public void setRole(String role) {
        this.role = role;
    }
    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
