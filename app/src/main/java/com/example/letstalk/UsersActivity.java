package com.example.letstalk;

import static com.example.letstalk.ChatActivity.handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    ArrayList<UserModel> userModels = new ArrayList<>();
    Client client;

    Button btnSettings;
    Button btnAddContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        String actNumber = getIntent().getStringExtra("activity");
        if (actNumber != null) {
            if (actNumber.equals("1")) {
                startActivity(getIntent());
                finish();
                Log.d("RRRELOAD", "RELOADED BABY");
            }
        }

//        overridePendingTransition(0, 0);

        btnSettings = findViewById(R.id.btnSettings);
        btnAddContacts = findViewById(R.id.btnAddContact);

        Context context = getApplicationContext();
        client = new Client("181.215.69.116", 9999);
        String _username_secure = null;
        try {
            _username_secure = FileUtility.readFromFile("username.txt", context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        listFriends(_username_secure);
        reload(_username_secure);

        btnSettings.setOnClickListener(view -> {
            Intent myIntent = new Intent(UsersActivity.this, SettingsActivity.class);
            myIntent.putExtra("key", " "); //Optional parameters
            UsersActivity.this.startActivity(myIntent);
        });

        btnAddContacts.setOnClickListener(view -> {
            Intent myIntent = new Intent(UsersActivity.this, ContactsActivity.class);
            myIntent.putExtra("key", " "); //Optional parameters
            UsersActivity.this.startActivity(myIntent);
        });
    }

    public void listFriends(String _username_secure) {

        String friends = client.request(
                "-1",
                "listFriends",
                _username_secure);

        Log.d("FRIENDS", "FRIENDS: ");
        Log.d("FRIENDS", "FIRST: " + friends);



        RecyclerView recyclerView = findViewById(R.id.mRecyclerView);

        if (friends != null) {
            userModels.clear();
            setupUserModels(friends);
        }

        UM_RecyclerViewAdapter adapter
                = new UM_RecyclerViewAdapter(this, userModels);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void reload(String _username_secure) {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

                listFriends(_username_secure);
                reload(_username_secure);
            }
        }, 5000);
    }

    // if pressed back just close app
    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }


    private void setupUserModels(String friends) {

        if (friends != null) {
            String[] sfriends = friends.split(" ");
            for (String sf : sfriends) {
                Log.d("FRIENDS", "SECOND: " + sf);
                userModels.add(new UserModel(sf));
            }
        }
    }
}