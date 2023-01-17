package com.example.letstalk;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword, edtEmail;
    private Button btnSubmit;

    private TextView txtLoginInfo;
    private TextView tvEmailError;
    private TextView tvUsernameError;


    private boolean isSinginUp = true;
    private Client client;

    private String loginFile = "login_status.txt";
    private String keyFile = "primary_key.txt";
    private String usernameFile = "username.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtUsername = findViewById(R.id.edtUsername);

        btnSubmit = findViewById(R.id.btnSubmit);
        txtLoginInfo = findViewById(R.id.txtLoginInfo);

        tvEmailError = findViewById(R.id.tvEmailExist);
        tvUsernameError = findViewById(R.id.tvUsernameExist);

        client = new Client("181.215.69.116", 9999);

        Context context = getApplicationContext();

        // IF ALREADY LOGGED IN
        if (FileUtility.isAlreadyLoggedIn(context)) {
            Intent myIntent = new Intent(MainActivity.this, UsersActivity.class);
            myIntent.putExtra("username",
                    String.valueOf(edtUsername.getText())); //Optional parameters
            MainActivity.this.startActivity(myIntent);
        }


        btnSubmit.setOnClickListener(view -> {

            String userId = client.createAccount(
                    String.valueOf(edtUsername.getText()),
                    String.valueOf(edtEmail.getText()),
                    String.valueOf(edtPassword.getText()),
                    context);

            // if 1, username already exists

            String _username = FileUtility.getUsernameFromFile(context);
            String _loginstatus = FileUtility.getLoginStatusFromFile(context);
            String _userkey = FileUtility.getUserIdFromFile(context);
            Log.d("XXXFILE", _username);
            Log.d("XXXFILE", _loginstatus);
            Log.d("XXXFILE", _userkey);


            // if creating account worked jump to next activity (UsersActivity), status = logged in "true"
            if (userId != null) {
                Log.d("XXXCASE", userId);
                if (!userId.equals("1") && !userId.equals("2") && !userId.equals("3")) {
                    if (userId.equals("1")) {
                        Log.d("XXXEXIST", "USERNAME EXIST");
                        tvUsernameError.setVisibility(VISIBLE);
                    }
                        // if 2, email already exists
                    else if (userId.equals("2")) {
                        Log.d("XXXEXIST", "EMAIL EXIST");
                        tvEmailError.setVisibility(VISIBLE);
                    }
                        // if 3, both email and username already exist
                    else if (userId.equals("3")) {
                        Log.d("XXXEXIST", "USERNAME and EMAIL EXIST");
                        tvUsernameError.setVisibility(VISIBLE);
                        tvEmailError.setVisibility(VISIBLE);
                    }
                } else {
                    Log.d("XXXCASE", "4 CHECK");
                    Intent myIntent = new Intent(MainActivity.this, UsersActivity.class);
                    myIntent.putExtra("username",
                            String.valueOf(edtUsername.getText())); //Optional parameters
                    MainActivity.this.startActivity(myIntent);
                }

            }
            else
                Log.d("XXXCASE", "4 CHECK ELSE");

        });

        // if clicked jump to login activity and vice versa
        txtLoginInfo.setOnClickListener(view -> {
            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(myIntent);
        });
    }
}