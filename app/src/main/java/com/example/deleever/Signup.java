package com.example.deleever;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
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
import com.google.gson.annotations.SerializedName;
import retrofit2.http.Body;
import retrofit2.http.POST;
import static com.example.deleever.constant.Constant.*;

import org.json.JSONObject;


public class Signup extends AppCompatActivity {
    private EditText signup_name,signup_business_name,signup_email,signup_phone_num,signup_password,signup_username,signup_cnfrmpw;
    private Button signup_register_btn;

    String signup_name_validation,signup_business_name_validation,signup_email_validation,signup_phone_num_validation,
            signup_password_validation,signup_cnfrmpw_validation,signup_username_validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        signup_register_btn = findViewById(R.id.signup_register_btn);
        signup_name = findViewById(R.id.signup_name);
        signup_business_name = findViewById(R.id.signup_business_name);
        signup_email = findViewById(R.id.signup_email);
        signup_phone_num = findViewById(R.id.signup_phone_num);
        signup_username = findViewById(R.id.signup_username);
        signup_password = findViewById(R.id.signup_password);
        signup_cnfrmpw = findViewById(R.id.signup_cnfrmpw);

        // Create ApiService instance

        signup_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup_name_validation = signup_name.getText().toString();
                signup_business_name_validation = signup_business_name.getText().toString();
                signup_email_validation = signup_email.getText().toString();
                signup_phone_num_validation = signup_phone_num.getText().toString();
                signup_password_validation = signup_password.getText().toString();
                signup_username_validation = signup_username.getText().toString();
                signup_cnfrmpw_validation = signup_cnfrmpw.getText().toString();

                DataModal dataModal = new DataModal(signup_username_validation, signup_cnfrmpw_validation, signup_email_validation, signup_name_validation, signup_business_name_validation, signup_phone_num_validation);

                if (signup_name_validation.isEmpty()) {
                    Toast.makeText(Signup.this, "Enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (signup_business_name_validation.isEmpty()) {
                    Toast.makeText(Signup.this, "Enter your business name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (signup_email_validation.isEmpty() || !(Patterns.EMAIL_ADDRESS.matcher(signup_email_validation).matches())) {
                    Toast.makeText(Signup.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                    signup_email.setText("");
                    return;
                }
                if (signup_phone_num_validation.isEmpty() || (signup_phone_num_validation.length() != 10)) {
                    Toast.makeText(Signup.this, "Enter valid phone number", Toast.LENGTH_SHORT).show();
                    signup_phone_num.setText("");
                    return;
                }
                if (signup_username_validation.isEmpty()) {
                    Toast.makeText(Signup.this, "Enter your username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (signup_password_validation.isEmpty()) {
                    Toast.makeText(Signup.this, "Enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!(signup_password_validation.length()>=8 && signup_password_validation.length()<=12) ){
                    Toast.makeText(Signup.this, "Enter characters between 8-12  for your password", Toast.LENGTH_SHORT).show();
                    signup_password.setText("");
                    return;
                }
                if((!signup_cnfrmpw_validation.equals(signup_password_validation)) ){
                    Toast.makeText(Signup.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    signup_cnfrmpw.setText("");
                    return;
                }
                sendSignupDetails(dataModal);
            }

            private void sendSignupDetails(DataModal dataModal) {
                ApiService apiService = retrofit.create(ApiService.class);


                Call<RegisterResponse> call = apiService.createPost(dataModal);

                call.enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            RegisterResponse responseFromAPI = response.body();
                            Log.d(TAG, "id "+responseFromAPI.userId);
                            Log.d(TAG, "Registration successful" + response.body());
                            Log.d(TAG, "id "+responseFromAPI.userId);
                            Toast.makeText(Signup.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            clearFields();
                            Log.d(TAG, "Registration successful" + response.body());
                            Intent loginIntent = new Intent(Signup.this, Login.class);
                            startActivity(loginIntent);
                            // Finish the SignUp activity so that the user cannot navigate back to it
                            finish();
//                            if (responseFromAPI.userId != null) {
//                                Log.d(TAG, "id "+responseFromAPI.userId);
//                                Toast.makeText(Signup.this, "Registration successful", Toast.LENGTH_SHORT).show();
//                                clearFields();
//                                Log.d(TAG, "Registration successful" + response.body());
//                                Intent loginIntent = new Intent(Signup.this, Login.class);
//                                startActivity(loginIntent);
//                                // Finish the SignUp activity so that the user cannot navigate back to it
//                                finish();
//                            } else {
//                                System.out.println("Registration failed" + response.body());
//                                clearFields();
//                                Toast.makeText(Signup.this, "Registration failed", Toast.LENGTH_SHORT).show();
//                            }
                        }
                        else {
                            try {
                                String errorBody = response.errorBody().string();

                                // Parse the JSON string to extract the error message
                                JSONObject jsonObject = new JSONObject(errorBody);
                                String errorMessage = jsonObject.getString("error");

                                // Now you have the error message ("Username already exists") in errorMessage
                                System.out.println(errorMessage);
                                Toast.makeText(Signup.this, "Registration failed :"+errorMessage, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(Signup.this, "Registration failed with unknown error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        Toast.makeText(Signup.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

        TextView show_password = findViewById(R.id.signup_show_password);
        show_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show_password.getText().toString().equals("Hide password")) {
                    show_password.setText("Show password");
                    signup_password.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    show_password.setText("Hide password");
                    signup_password.setTransformationMethod(null);
                }
            }
        });

        TextView login_pg_connector = findViewById(R.id.login_pg_connector);

        login_pg_connector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_pg_connector = new Intent(Signup.this, Login.class);
                startActivity(login_pg_connector);
            }
        });

    }


//    private void showToast(String message) {
//        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.BOTTOM | Gravity.RIGHT, 0, 1110);
//        toast.setMargin(50, 50);
//        toast.show();
//    }

    private void clearFields() {
        signup_email.setText("");
        signup_name.setText("");
        signup_phone_num.setText("");
        signup_password.setText("");
        signup_business_name.setText("");
        signup_username.setText("");
        signup_cnfrmpw.setText("");
    }

    class DataModal {

        @SerializedName("user")
        public User user;

        @SerializedName("seller")
        public Seller seller;

        public DataModal(String username, String password, String email, String name, String businessName, String contactNumber) {
            this.user = new User(username, password, email, name);
            this.seller = new Seller(businessName, contactNumber);
        }

        public static class User {
            @SerializedName("username")
            public String username;

            @SerializedName("password")
            public String password;

            @SerializedName("email")
            public String email;

            @SerializedName("role")
            public String role = "SELLER"; // assuming all signups are for sellers

            @SerializedName("name")
            public String name;

            public User(String username, String password, String email, String name) {
                this.username = username;
                this.password = password;
                this.email = email;
                this.name = name;
            }
        }

        public static class Seller {
            @SerializedName("businessName")
            public String businessName;

            @SerializedName("contactNumber")
            public String contactNumber;

            public Seller(String businessName, String contactNumber) {
                this.businessName = businessName;
                this.contactNumber = contactNumber;
            }
        }
    }

    public class RegisterResponse {
        @SerializedName("userId")
        public String userId;

        @SerializedName("error")
        public String error;
}

}
