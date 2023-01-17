package com.example.letstalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    private Button btnLogout;
    private Button btnGoBack;
    private String loginFile = "login_status.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Context context = getApplicationContext();

        btnLogout = findViewById(R.id.btnLogout);
        btnGoBack = findViewById(R.id.btnGoBack);

        btnLogout.setOnClickListener(view -> {
            try {
                FileUtility.writeToFile(loginFile, "false", context);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Intent myIntent = new Intent(SettingsActivity.this, MainActivity.class);
            myIntent.putExtra("key", " "); //Optional parameters
            SettingsActivity.this.startActivity(myIntent);
        });

        btnGoBack.setOnClickListener(view -> {
            Intent myIntent = new Intent(SettingsActivity.this, UsersActivity.class);
            myIntent.putExtra("key", " "); //Optional parameters
            SettingsActivity.this.startActivity(myIntent);
        });


    }
}