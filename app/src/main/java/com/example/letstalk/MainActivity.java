
package com.example.letstalk;

import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.letstalk.Client;
import com.example.letstalk.FileUtility;
import com.example.letstalk.LoginActivity;
import com.example.letstalk.R;
import com.example.letstalk.UsersActivity;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword, edtEmail;

    private Client client;

    // final because the values of the Strings never cha
    private final String loginFile = "login_status.txt";
    private final String keyFile = "primary_key.txt";
    private final String usernameFile = "username.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtUsername = findViewById(R.id.edtUsername);

        Button btnSubmit = findViewById(R.id.btnSubmit);
        TextView txtLoginInfo = findViewById(R.id.txtLoginInfo);

        // get Client instance object
        client = Client.getInstance();

        TextView tvExistError = findViewById(R.id.tvExistError);

        Context context = getApplicationContext();

        // if user is already logged in jump to UsersActivity

        String tempUsername = FileUtility.isUserLoggedIn(context);
        if (tempUsername != null) {
            Intent myIntent = new Intent(MainActivity.this, UsersActivity.class);
            myIntent.putExtra("username", tempUsername);
            MainActivity.this.startActivity(myIntent);
        }


        btnSubmit.setOnClickListener(view -> {

            // create user , get data from the edit text fields
            String userId = client.createUser(
                    String.valueOf(edtUsername.getText()),
                    String.valueOf(edtEmail.getText()),
                    String.valueOf(edtPassword.getText())
            );

            // if createUser worked
            if (userId != null) {
                // if userId is 1 or 2 or 3 do nothing just display text and stay on page

                // userId = 1, if username already exist
                switch (userId) {
                    case "1":
                        tvExistError.setText("Username Already exist");
                        tvExistError.setVisibility(VISIBLE);
                        break;

                    // userId = 2, if email already exist
                    case "2":
                        tvExistError.setText("Email Already exist");
                        tvExistError.setVisibility(VISIBLE);
                        break;

                    // userId = 3, if username & email already exist
                    case "3":
                        tvExistError.setText("Username and Email Already exist");
                        tvExistError.setVisibility(VISIBLE);

                        break;
                    default:  // if createUser worked, write all important data to files

                        try {
                            FileUtility.writeToFile(keyFile, userId, context);
                            FileUtility.writeToFile(loginFile, "true", context);
                            FileUtility.writeToFile(usernameFile, String.valueOf(edtUsername.getText()), context);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // go to UsersActivity and pass the username to it

                        String username = String.valueOf(edtUsername.getText());

                        Intent myIntent = new Intent(MainActivity.this, UsersActivity.class);
                        myIntent.putExtra("username", username);
                        MainActivity.this.startActivity(myIntent);


                        break;
                }
            }
        });

        // "Sign Up?" if clicked jump to Login page
        txtLoginInfo.setOnClickListener(view -> {
            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(myIntent);
        });
    }
}

