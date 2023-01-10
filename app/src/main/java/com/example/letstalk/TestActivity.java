package com.example.letstalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;

public class TestActivity extends AppCompatActivity {

    EditText edtUserInput;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        edtUserInput = findViewById(R.id.edtUserInput);
        submit = findViewById(R.id.btnTest);

        Context context = getApplicationContext();
        String[] files = context.fileList();
        for (String file : files)
            Log.d("STRING STRING", file);

        String login = null;
        String key = null;
        try {
            login = FileUtility.readFromFile("login_status.txt", context);
            key = FileUtility.readFromFile("primary_key.txt", context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("LOGIN" , "LOGIN: " + login);
        Log.d("KEY" , "KEY: " + key);

        if (login.equals("false")) {
            Intent myIntent = new Intent(TestActivity.this, MainActivity.class);
            myIntent.putExtra("key", key); //Optional parameters
            TestActivity.this.startActivity(myIntent);
        }

    }
}