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


public class Signup extends AppCompatActivity {

    private EditText signup_name;
    private EditText signup_business_name;
    private EditText signup_email;
    private EditText signup_phone_num;
    private EditText signup_password;

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
        signup_password=findViewById(R.id.signup_password);
//        findViews();

        signup_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String signup_name_validation = signup_name.getText().toString();
                String signup_business_name_validation = signup_business_name.getText().toString();
                String signup_email_validation = signup_email.getText().toString();
                String signup_phone_num_validation = signup_phone_num.getText().toString();
                String signup_password_validation = signup_password.getText().toString();
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


                Intent signup = new Intent(Signup.this, MainActivity.class);
                startActivity(signup);
            }
        });
        TextView show_password = findViewById(R.id.signup_show_password);
        show_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(signup_password_validation.isEmpty()){
//                    Toast.makeText(Signup.this, "Enter your password", Toast.LENGTH_SHORT).show();
//                }else
//                {
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

}