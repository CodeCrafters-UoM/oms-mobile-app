package com.example.deleever;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Login extends AppCompatActivity {

    private EditText txt_passWord;
    private TextView txt_showPassword;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn_logIn = findViewById(R.id.btn_logIn);
        btn_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this,"LOGIN SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });

        TextView txt_forgetPassword = findViewById(R.id.txt_forgetPassword);
        txt_forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), forgot_password.class);
                startActivity(intent);

            }
        });

        TextView txt_registerHere = findViewById(R.id.txt_registerHere);
        txt_registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);

            }
        });

        txt_passWord = findViewById(R.id.txt_passWord);
        txt_showPassword = findViewById(R.id.txt_showPassword);

        txt_showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPasswordVisible){
                    txt_passWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isPasswordVisible = false;

                    txt_showPassword.setText("Show Password");
                }else{
                    txt_passWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isPasswordVisible = true;

                    txt_showPassword.setText("Hide Password");
                }
            }
        });
    }

}