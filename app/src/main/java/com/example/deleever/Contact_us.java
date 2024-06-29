package com.example.deleever;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.example.deleever.constant.Constant.*;

public class Contact_us extends AppCompatActivity {

    private String jwtToken, userId;

    private EditText yourNameField, businessNameField, telNoField, messageField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        jwtToken = getIntent().getStringExtra("jwtToken");
        userId = getIntent().getStringExtra("sellerid");

        yourNameField = findViewById(R.id.txt_yourNameField);
        businessNameField = findViewById(R.id.txt_businessNameField);
        telNoField = findViewById(R.id.txt_telNo);
        messageField = findViewById(R.id.txt_messageField);

        Button btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = yourNameField.getText().toString().trim();
                String businessName = businessNameField.getText().toString().trim();
                String telNo  = telNoField.getText().toString().trim();
                String message  = messageField.getText().toString().trim();

                if (name.isEmpty() || businessName.isEmpty() || message.isEmpty() || telNo.isEmpty()) {
                    Toast.makeText(Contact_us.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidPhoneNumber(telNo)) {
                    Toast.makeText(Contact_us.this, "Please enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContactUsRequest request = new ContactUsRequest();
                request.setName(name);
                request.setBusinessName(businessName);
                request.setContactNumber(telNo);
                request.setMessage(message);

                sendContactUs(request);
            }
        });

        TextView txt_back = findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                intent.putExtra("jwtToken", jwtToken);
                intent.putExtra("sellerid", userId);
                startActivity(intent);
                finish(); // Close the current activity to prevent users from returning to it
            }
        });

    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String phonePattern = "^[0-9]{10}$";
        return phoneNumber.matches(phonePattern);
    }


    private void sendContactUs(ContactUsRequest request) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // Replace with your actual API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIservice2 apiService = retrofit.create(APIservice2.class);
        System.out.println("Sending request: " + request.toString());
        Call<ContactUsRequest> call = apiService.sendContactUs("Bearer " + jwtToken, request);
        call.enqueue(new Callback<ContactUsRequest>() {
            @Override
            public void onResponse(Call<ContactUsRequest> call, Response<ContactUsRequest> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Contact_us.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                    navigateToProfile();
                } else {
                    Toast.makeText(Contact_us.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ContactUsRequest> call, Throwable t) {
                Toast.makeText(Contact_us.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace(); // Log the error
            }
        });
    }

    private void navigateToProfile() {
        Intent intent = new Intent(getApplicationContext(), Profile.class);
        intent.putExtra("jwtToken", jwtToken);
        intent.putExtra("sellerid", userId);
        startActivity(intent);
        finish(); // Close the current activity to prevent users from returning to it
    }
}
class ContactUsRequest {
    @SerializedName("name")
    private String name;

    @SerializedName("businessName")
    private String businessName;

    @SerializedName("message")
    private String message;

    @SerializedName("contactNumber")
    private String contactNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ContactUsRequest{" +
                "name='" + name + '\'' +
                ", businessName='" + businessName + '\'' +
                ", message='" + message + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                '}';
    }
}

