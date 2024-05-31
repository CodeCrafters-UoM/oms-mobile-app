package com.example.deleever;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView icon = findViewById(R.id.img_backIcon);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });

        TextView txt_contact = findViewById(R.id.txt_contact);
        txt_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(), Contact_us.class);
                startActivity(intent);

            }
        });


        TextView txt_LogOut = findViewById(R.id.txt_LogOut);
        txt_LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Toast.makeText(Profile.this,"Log Out", Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
        });
    }
}