package com.example.deleever;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.Gravity;
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


public class Signup extends AppCompatActivity {

    private static final String IP_ADDRESS = "192.168.130.146";

    private static final String BASE_URL = "http://"+IP_ADDRESS+":8000/";
    private EditText signup_name;
    private EditText signup_business_name;
    private EditText signup_email;
    private EditText signup_phone_num;
    private EditText signup_password;
    private EditText signup_username;

    Button signup_register_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup_register_btn = findViewById(R.id.signup_register_btn);
        signup_name=findViewById(R.id.signup_name);
        signup_business_name=findViewById(R.id.signup_business_name);
        signup_email=findViewById(R.id.signup_email);
        signup_phone_num=findViewById(R.id.signup_phone_num);
        signup_username = findViewById(R.id.signup_username);
        signup_password=findViewById(R.id.signup_password);

        signup_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String signup_name_validation = signup_name.getText().toString();
                String signup_business_name_validation = signup_business_name.getText().toString();
                String signup_email_validation = signup_email.getText().toString();
                String signup_phone_num_validation = signup_phone_num.getText().toString();
                String signup_password_validation = signup_password.getText().toString();
                String signup_username_validation= signup_username.getText().toString();

                if(signup_name_validation.equals("")){
                    Toast toast= Toast.makeText(getApplicationContext(), "Enter your name", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.RIGHT, 0, 1110);
                    toast.setMargin(50,50);
                    toast.show();
                }
                if(signup_business_name_validation.equals("")){
                    Toast.makeText(Signup.this, "Enter your business name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(signup_email_validation.isEmpty() || !(Patterns.EMAIL_ADDRESS.matcher(signup_email_validation).matches())){
                    Toast.makeText(Signup.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(signup_phone_num_validation.isEmpty() || (signup_phone_num_validation.length() != 10 )){
                    Toast.makeText(Signup.this, "Enter valid phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(signup_password_validation.isEmpty()){
                    Toast.makeText(Signup.this, "Enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(signup_username_validation.isEmpty()){
                    Toast.makeText(Signup.this, "Enter your user name", Toast.LENGTH_SHORT).show();
                    return;
                }

                postData(signup_name_validation,signup_email_validation,signup_phone_num_validation,signup_password_validation,signup_business_name_validation,signup_username_validation);
                Intent signup = new Intent(Signup.this, MainActivity.class);
                startActivity(signup);
            }
        });
        TextView show_password = findViewById(R.id.signup_show_password);
        show_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(show_password.getText().toString().equals("Hide password")) {
                    show_password.setText("Show password");
                    signup_password.setTransformationMethod(new PasswordTransformationMethod());

                } else {
                    show_password.setText("Hide password");
                    signup_password.setTransformationMethod(null);
                }
            }
        });
    }

    private void postData(String name, String email, String businessName, String password, String contactNumber,String userName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService retrofitAPIsignup = retrofit.create(ApiService.class);

        DataModal modal = new DataModal(name, email,businessName,password,contactNumber,userName);

        Call<DataModal> call = retrofitAPIsignup.createPost(modal);

        call.enqueue(new Callback<DataModal>() {
            @Override
            public void onResponse(Call<DataModal> call, Response<DataModal> response) {
                //setting empty text to our both edit text.
                signup_email.setText("");
                signup_name.setText("");
                signup_phone_num.setText("");
                signup_password.setText("");
                signup_business_name.setText("");
                signup_username.setText("");

                // getting response from our body and passing it to our modal class.
                DataModal responseFromAPI = response.body();

                //  getting our data from modal class and adding it to our string.
                String responseString = "Response Code : " + response.code() + "\nName : " + responseFromAPI.getName() + "\n" + "email : " + responseFromAPI.getEmail()+"\n" + "cont : " + responseFromAPI.getContactNumber()+"\n" + "bnaqme : " + responseFromAPI.getBusinessName()+"\n" + "pw : " + responseFromAPI.getPassword()+"\n" + "UN : " + responseFromAPI.getSignup_username();
                Toast.makeText(Signup.this, "Data added to API"+responseString, Toast.LENGTH_SHORT).show();

       }

            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {
                  Toast.makeText(Signup.this,"Error found is : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}



class DataModal {

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("businessName")
    private String businessName;
    @SerializedName("password")
    private String password;
    @SerializedName("contactNumber")
    private String contactNumber;

    @SerializedName("userName")
    private String signup_username;


    public DataModal(String name, String email, String businessName, String password, String contactNumber,String signup_username) {
        this.name = name;
        this.email = email;
        this.businessName = businessName;
        this.password = password;
        this.contactNumber = contactNumber;
        this.signup_username = signup_username;
    }

    public String getSignup_username() {
        return signup_username;
    }

    public void setSignup_username(String signup_username) {
        this.signup_username = signup_username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
