package com.example.deleever;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deleever.Login;
import com.example.deleever.MainActivity;
import static com.example.deleever.constant.Constant.*;


public class Profile extends AppCompatActivity {

    private String jwtToken, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        jwtToken = getIntent().getStringExtra("jwtToken");
        userId = getIntent().getStringExtra("sellerid");

        System.out.println("userrrr" + userId);
        System.out.println("jwwwwww" + jwtToken);

        TextView txt_back = findViewById(R.id.txt_back);
        txt_back.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                navigateHome();
            }
        });

        // Profile Group
        findViewById(R.id.profile_group_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(Profile.this, myProfile.class);
                intent.putExtra("jwtToken", jwtToken);
                intent.putExtra("sellerid", userId);
                startActivity(intent);
            }
        });

        // Notification Group
        findViewById(R.id.notification_group_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(profile.this, NotificationActivity.class);
//                startActivity(intent);
            }
        });

        // Contact Group
        findViewById(R.id.contact_group_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Contact_us.class);
                intent.putExtra("jwtToken", jwtToken);
                intent.putExtra("sellerid", userId);
                startActivity(intent);
            }
        });

        // Language Group
//        findViewById(R.id.language_group_container).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(profile.this, LanguageActivity.class);
////                startActivity(intent);
//            }
//        });

        // Log Out Group
//        findViewById(R.id.logOut_group_container).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(profile.this, Login.class);
//                Toast.makeText(profile.this, "Log Out", Toast.LENGTH_SHORT).show();
//                startActivity(intent);
//            }
//        });

    }

    private void navigateHome() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("jwtToken", jwtToken);
        intent.putExtra("sellerid", userId);
        startActivity(intent);
        finish(); // Close the current activity to prevent users from returning to it
    }
}
