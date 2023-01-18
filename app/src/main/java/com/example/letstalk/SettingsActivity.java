package com.example.letstalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    private final String loginFile = "login_status.txt";

    Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // get client object
        client = Client.getInstance();
        Context context = getApplicationContext();

        // username send from UsersActivity
        String username = null;
        try {
            username = FileUtility.readFromFile("username.txt", context);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnGoBack = findViewById(R.id.btnGoBack);

        Button btnChangeUserInfo = findViewById(R.id.btnChangeUsername);
        Button btnChangeEmailInfo = findViewById(R.id.btnChangeEmail);
        Button btnChangePasswordInfo = findViewById(R.id.btnChangePassword);

        TextView tvUsername = findViewById(R.id.tvUsername);
        TextView tvEmail = findViewById(R.id.tvEmail);

        // display username and email on settings page
        // we have already username so get email now
        String email = client.getEmailByUsername(username);

        tvUsername.setText("user: " + username);
        tvEmail.setText("email: " + email);


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

        // if clicked on change Username
        // give ChangeUserDataActivity the variable "username" which means the user
        // want to change the username
        btnChangeUserInfo.setOnClickListener(view -> {
            Intent myIntent = new Intent(SettingsActivity.this, ChangeUserDataActivity.class);
            myIntent.putExtra("change", "username");
            SettingsActivity.this.startActivity(myIntent);
        });

        // if clicked on change Email
        // give ChangeUserDataActivity the variable "email" which indicates the user
        // want to change the email
        btnChangeEmailInfo.setOnClickListener(view -> {
            Intent myIntent = new Intent(SettingsActivity.this, ChangeUserDataActivity.class);
            myIntent.putExtra("change", "email");
            SettingsActivity.this.startActivity(myIntent);
        });


        // if clicked on change Password
        // give ChangeUserDataActivity the variable "password" which indicates the user
        // want to change the password
        btnChangePasswordInfo.setOnClickListener(view -> {
            Intent myIntent = new Intent(SettingsActivity.this, ChangeUserDataActivity.class);
            myIntent.putExtra("change", "password");
            SettingsActivity.this.startActivity(myIntent);
        });


    }
}