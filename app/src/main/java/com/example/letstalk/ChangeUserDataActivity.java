package com.example.letstalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
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

        // just display the operation on top of the view depending on
        // the string change
        if (change.equals("username"))
            tvOperation.setText("Change Username");

       else if (change.equals("email"))
            tvOperation.setText("Change Email");

        else if (change.equals("password")) {
            tvOperation.setText("Change Password");
        }

        btnSubmit.setOnClickListener(view -> {

            // get input from edtUserInfo
            String userInputData = edtUserInfo.getText().toString();


            switch (change) {
                case "username":

                    // change username and also write to file
                    client.changeUsername(primaryKey, userInputData);
                    try {
                        FileUtility.writeToFile("username.txt", userInputData, context);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "email":
                    client.changeEmail(primaryKey, userInputData);
                    break;
                case "password":
                    client.changePassword(primaryKey, userInputData);
                    break;
            }

            // jump back to settings
            Intent myIntent = new Intent(ChangeUserDataActivity.this, SettingsActivity.class);
            ChangeUserDataActivity.this.startActivity(myIntent);
        });


    }
}

