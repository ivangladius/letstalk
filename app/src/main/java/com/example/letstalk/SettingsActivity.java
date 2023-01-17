package com.example.letstalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    private final String loginFile = "login_status.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Context context = getApplicationContext();

        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnGoBack = findViewById(R.id.btnGoBack);

        // by logging out we just write false
        // to the loginFile, to state that the user is logged out
        btnLogout.setOnClickListener(view -> {
            try {
                FileUtility.writeToFile(loginFile, "false", context);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // jump to MainActivity, user will be logged out
            // so he will land on the CreateUser page
            Intent myIntent = new Intent(SettingsActivity.this, MainActivity.class);
            SettingsActivity.this.startActivity(myIntent);
        });

        // just go back to UsersActivity
        btnGoBack.setOnClickListener(view -> {
            Intent myIntent = new Intent(SettingsActivity.this, UsersActivity.class);
            SettingsActivity.this.startActivity(myIntent);
        });


    }
}