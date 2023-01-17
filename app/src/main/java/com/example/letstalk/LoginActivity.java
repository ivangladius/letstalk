package com.example.letstalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {


    private EditText edtEmail, edtPassword;

    private Client client;

    // final because we never change the String values
    private final String loginFile = "login_status.txt";
    private final String keyFile = "primary_key.txt";
    private final String usernameFile = "username.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        Button btnSubmit = findViewById(R.id.btnSubmit);
        TextView txtLoginInfo = findViewById(R.id.txtLoginInfo);

        TextView tvErrorExist = findViewById(R.id.tvExistError);

        Context context = getApplicationContext();

        // get Client instance object
        Client client = Client.getInstance();

        // if user is already logged in jump to UsersActivity
        String tempUsername = FileUtility.isUserLoggedIn(context);
        if (tempUsername != null) {
            Intent myIntent = new Intent(LoginActivity.this, UsersActivity.class);
            myIntent.putExtra("username", tempUsername);
            LoginActivity.this.startActivity(myIntent);
        }

        btnSubmit.setOnClickListener(view -> {

            // get String[] {"userId", "username"} from function login
            String payload = client.login(
                    String.valueOf(edtEmail.getText()),
                    String.valueOf(edtPassword.getText())
            );

            // now split payload where
            // credentials[0] is the userId
            // credentials[1] is the username

            String userId = null;
            String username = null;
            if (payload != null) {
                String[] credentials = payload.split(" ");
                userId = credentials[0]; // get UserId from returned String[]
                username = credentials[1]; // get username from returned String[]
            }
            // to save later in files, to check if login status is true in
            // MainActivity

            if (userId != null && username != null) {
                try {
                    // write credentials to file, meaning he is logged in
                    FileUtility.writeToFile(keyFile, userId, context);
                    FileUtility.writeToFile(usernameFile, username, context);
                    FileUtility.writeToFile(loginFile, "true", context);

                    Intent myIntent = new Intent(LoginActivity.this, UsersActivity.class);
                    myIntent.putExtra("key", userId);
                    myIntent.putExtra("username", username);
                    LoginActivity.this.startActivity(myIntent);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else { // if userId and username is null, account does not exist
                tvErrorExist.setText("Account does not Exist");
                tvErrorExist.setVisibility(View.VISIBLE);
            }


            // if successfully logged in jump to UsersActivity

            // send userId and username to UsersActivity and jump to that


        });

        // "create account" ? if clicked jump to MainActivity page to create account
        txtLoginInfo.setOnClickListener(view -> {
            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(myIntent);
        });
    }
}