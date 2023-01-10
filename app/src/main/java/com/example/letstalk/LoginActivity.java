package com.example.letstalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {


    private EditText edtUsername, edtPassword, edtEmail;
    private Button btnSubmit;
    private TextView txtLoginInfo;

    private boolean isSinginUp = true;
    private Client client;

    private String loginFile = "login_status.txt";
    private String keyFile = "primary_key.txt";
    private String usernameFile = "username.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        btnSubmit = findViewById(R.id.btnSubmit);
        txtLoginInfo = findViewById(R.id.txtLoginInfo);

        client = new Client("181.215.69.116", 9999);

        Context context = getApplicationContext();
        // IF ALREADY LOGGED IN
        try {
            File _loginFile = new File(context.getFilesDir(), loginFile);
            File _usernameFile = new File(context.getFilesDir(), usernameFile);

            if (_loginFile.exists()  && _usernameFile.exists()) {
                String loginStatus = FileUtility.readFromFile(loginFile, context);
                if (loginStatus.equals("true")) {
                    String userId = FileUtility.readFromFile(keyFile, context);
                    String _username = FileUtility.readFromFile(keyFile, context);

                    Intent myIntent = new Intent(LoginActivity.this, UsersActivity.class);
                    myIntent.putExtra("key", userId); //Optional parameters
                    myIntent.putExtra("username", _username); //Optional parameters
                    LoginActivity.this.startActivity(myIntent);
                }
            } else {
                FileUtility.writeToFile(loginFile, "false", context);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnSubmit.setOnClickListener(view -> {
            String payload = client.request(
                    "-1",
                    "login",
                    edtEmail.getText() + " " + edtPassword.getText());

            String[] cred = payload.split(" ");
            String userId = cred[0];
            String username = cred[1];

            if (payload != null) {
                try {
                    Log.d("XPAYLOAD", "userID = " + userId);
                    Log.d("XPAYLOAD", "username = " + username);
                    FileUtility.writeToFile(keyFile, userId, context);
                    FileUtility.writeToFile(loginFile, "true", context);
                    FileUtility.writeToFile(usernameFile, username, context);
                } catch (IOException e) {
                    Log.d("XPAYLOAD", "PAYLOAD IS NULL");
                    e.printStackTrace();
                }
            }
            Intent myIntent = new Intent(LoginActivity.this, UsersActivity.class);
            myIntent.putExtra("key", userId); //Optional parameters
            myIntent.putExtra("username", username); //Optional parameters
            LoginActivity.this.startActivity(myIntent);

        });
        txtLoginInfo.setOnClickListener(view -> {
            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(myIntent);
        });
    }
}