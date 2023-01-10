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
        setContentView(R.layout.activity_main);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtUsername = findViewById(R.id.edtUsername);

        btnSubmit = findViewById(R.id.btnSubmit);
        txtLoginInfo = findViewById(R.id.txtLoginInfo);

        TextView tvEmailError = findViewById(R.id.tvEmailError);
        TextView tvUsernameError= findViewById(R.id.tvUsernameError);

        client = new Client("181.215.69.116", 9999);

        Context context = getApplicationContext();

        Log.d("CLASSNAME", getClass().getName());
        // IF ALREADY LOGGED IN
        try {
            File _loginFile = new File(context.getFilesDir(), loginFile);
            File _usernameFile = new File(context.getFilesDir(), usernameFile);

            if (_loginFile.exists() && _usernameFile.exists()) {
                String loginStatus = FileUtility.readFromFile(loginFile, context);
                String _username = FileUtility.readFromFile(usernameFile, context);
                if (loginStatus.equals("true") && !_username.equals("none")) {
                    Intent myIntent = new Intent(MainActivity.this, UsersActivity.class);
                    myIntent.putExtra("username", _username); //Optional parameters
                    MainActivity.this.startActivity(myIntent);
                }
            } else {
                FileUtility.writeToFile(loginFile, "false", context);
                FileUtility.writeToFile(usernameFile, "none", context);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnSubmit.setOnClickListener(view -> {
            String userId = client.request("-1", "createUser",
                    edtUsername.getText() + " "
                            + edtEmail.getText() + " "
                            + edtPassword.getText());

            if (userId.equals("1"))
                tvUsernameError.setVisibility(VISIBLE);
            else if (userId.equals("2"))
                tvEmailError.setVisibility(VISIBLE);
            else if (userId.equals("3")) {
                tvUsernameError.setVisibility(VISIBLE);
                tvEmailError.setVisibility(VISIBLE);
            }

            else if (userId != null) {
                try {
                    FileUtility.writeToFile(keyFile, userId, context);
                    FileUtility.writeToFile(loginFile, "true", context);
                    FileUtility.writeToFile(usernameFile, String.valueOf(edtUsername.getText()), context);

                    String _username = String.valueOf(edtUsername.getText());

                    Intent myIntent = new Intent(MainActivity.this, UsersActivity.class);
                    myIntent.putExtra("username", _username); //Optional parameters
                    MainActivity.this.startActivity(myIntent);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        txtLoginInfo.setOnClickListener(view -> {
            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(myIntent);
        });
    }
}