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

    // create userModel for RecyclerView to display all friends
    // which contains all friends usernames
    ArrayList<UserModel> userModels = new ArrayList<>();
    Client client;

    Button btnSettings;
    Button btnAddContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        btnSettings = findViewById(R.id.btnSettings);
        btnAddContacts = findViewById(R.id.btnAddContact);

        Context context = getApplicationContext();
        client = Client.getInstance();

        // read username from file
        String username = null;
        try {
            username = FileUtility.readFromFile("username.txt", context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // list friends when UsersActivity is loaded
        listFriends(username);


        // reload every 5 seconds to list friends
        reload(username);


        // if clicked on button settings jump to SettingsActivity.java
        btnSettings.setOnClickListener(view -> {
            Intent myIntent = new Intent(UsersActivity.this, SettingsActivity.class);
            UsersActivity.this.startActivity(myIntent);
        });

        // if clicked on button Add Contact jump to ContactsActivity.java
        btnAddContacts.setOnClickListener(view -> {
            Intent myIntent = new Intent(UsersActivity.this, ContactsActivity.class);
            UsersActivity.this.startActivity(myIntent);
        });
    }

    public void listFriends(String username) {

        // getting String like "max hans peter gandalf"
        String friends = client.listFriends(username);

        RecyclerView recyclerView = findViewById(R.id.mRecyclerView);

        // clear current view and send them to setupUserModel()
        // where the String friends get splitted
        // and every "friend" gets added to the Recyclerview

        if (friends != null) {
            userModels.clear();
            setupUserModels(friends);
        }

        // after view is created now set the new View
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
                // load every 5 seconds all friends
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
            // if user has 0 friends dont display any boxes
            // this fixed the visual bug if you had no friends
            // but still displayed one empty user box
            if (!(sfriends[0].equals("") && sfriends.length == 1)) {
                // now add every friend to the userModel
                // so it will be later displayed by the RecyclerView
                for (String sf : sfriends) {
                    userModels.add(new UserModel(sf));
                }
            }
        }
    }
}