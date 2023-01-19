package com.example.letstalk;

import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class ChangeUserDataActivity extends AppCompatActivity {

    Client client;
    String primaryKey = null;
    // in change will be the string username or email
    // depending on the String it will do different operations
    String change = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_data);


        Context context = getApplicationContext();


        // read primary key of user
        try {
            primaryKey = FileUtility.readFromFile("primary_key.txt", context);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        client = Client.getInstance();

        // will either contain "username" or "email" or "password"
        // depending on the string it will change the username or the email

        change = getIntent().getStringExtra("change");

        TextView tvOperation = findViewById(R.id.tvOperation);
        EditText edtUserInfo = findViewById(R.id.edtUserInfo);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        TextView tvExistError = findViewById(R.id.tvChangeExist);

        // just display the operation on top of the view depending on
        // the string change
        if (change.equals("username")) {
            tvOperation.setText("Change Username");
            edtUserInfo.setHint("new username");
        } else if (change.equals("email")) {
            tvOperation.setText("Change Email");
            edtUserInfo.setHint("new email");
        } else if (change.equals("password")) {
            tvOperation.setText("Change Password");
            edtUserInfo.setHint("new password");
        }

        btnSubmit.setOnClickListener(view -> {

            boolean changeDataSuccessful = false;

            // get input from edtUserInfo
            String userInputData = edtUserInfo.getText().toString();


            String status = null;
            switch (change) {
                case "username":

                    // change username and also write to file
                    status = client.changeUsername(primaryKey, userInputData);
                    if (status.equals("-1")) {
                        tvExistError.setText("Username Already exist");
                        tvExistError.setVisibility(VISIBLE);
                    } else
                        changeDataSuccessful = true;

                    try {
                        FileUtility.writeToFile("username.txt", userInputData, context);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "email":
                    status = client.changeEmail(primaryKey, userInputData);
                    if (status.equals("-1")) {
                        tvExistError.setText("Email Already exist");
                        tvExistError.setVisibility(VISIBLE);
                    } else
                        changeDataSuccessful = true;
                    break;
                case "password":
                    client.changePassword(primaryKey, userInputData);
                    break;
            }

            // jump back to settings
            if (changeDataSuccessful) {
                Intent myIntent = new Intent(ChangeUserDataActivity.this, SettingsActivity.class);
                ChangeUserDataActivity.this.startActivity(myIntent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(ChangeUserDataActivity.this, SettingsActivity.class);
        ChangeUserDataActivity.this.startActivity(myIntent);
    }
}

