package com.example.deleever;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
public class ActivationURL extends AppCompatActivity {
    Button activation_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation_url);
        activation_btn = findViewById(R.id.activation_btn);
        activation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activated_app = new Intent(ActivationURL.this, Login.class);
                startActivity(activated_app);
            }
        });
    }
}

