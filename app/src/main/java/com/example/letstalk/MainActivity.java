package com.example.letstalk;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword, edtEmail;
    private Button btnSubmit;
    private TextView txtLoginInfo;


    private boolean isSinginUp = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtUsername = findViewById(R.id.edtUsername);

        btnSubmit = findViewById(R.id.btnSubmit);
        txtLoginInfo = findViewById(R.id.txtLoginInfo);

        txtLoginInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSinginUp){
                    isSinginUp = false;
                    edtUsername.setVisibility(View.GONE);
                    btnSubmit.setText("Log in");
                    txtLoginInfo.setText("Don't have an account? Sing up");
                }else {
                    isSinginUp = true;
                    edtUsername.setVisibility(View.VISIBLE);
                    btnSubmit.setText("Sing up");
                    txtLoginInfo.setText("Already have an account? Log in");

                }
            }
        });
    }
}