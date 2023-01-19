package com.example.letstalk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

    String username;

    ImageButton btnSettings;
    ImageButton btnAddContacts;

    TextView tvNetworkError;

    static Handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        btnSettings = findViewById(R.id.btnSettings);
        btnAddContacts = findViewById(R.id.btnAddContact);

        tvNetworkError = findViewById(R.id.tvNetworkError);

        Context context = getApplicationContext();

        client = Client.getInstance();

        // read username from file
        username = null;
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
        if (friends != null) {
            tvNetworkError.setVisibility(View.INVISIBLE);

//            btnAddContacts.setText("Add Contacts");
//            btnSettings.setText("Settings");

            RecyclerView recyclerView = findViewById(R.id.mRecyclerView);

            // clear current view and send them to setupUserModel()
            // where the String friends get splitted
            // and every "friend" gets added to the Recyclerview

            userModels.clear();
            setupUserModels(friends);

            // after view is created now set the new View
            UM_RecyclerViewAdapter adapter
                    = new UM_RecyclerViewAdapter(this, userModels);

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else { // is network error
// erorr text
            tvNetworkError.setText("No Connection");
            tvNetworkError.setVisibility(View.VISIBLE);

            userModels.clear();
        }
    }

    public void reload(String username) {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // load every 5 seconds all friends from database
                listFriends(username);
                reload(username);
            }
        }, 5000);
    }




    private void setupUserModels(String friends) {

        if (friends != null) {
            String[] sfriends = friends.split(" ");
            // if user has 0 friends dont display any boxes
            // this fixed the visual bug if you had no friends
            // but still displayed one empty user box
            if (! (sfriends[0].equals("") && sfriends.length == 1)) {

                // now add every friend to the userModel
                // so it will be later displayed by the RecyclerView
                for (String sf : sfriends) {
                    userModels.add(new UserModel(sf));
                }
            }
        }
    }
    // kill background thread running the function listFriends if activity is not open
    // in AndroidManifest.xml all Activities are not saved if not currently active,
    // thus we can so easily call override the function onDestroy to kill all running background threads

    // if pressed back just close app
    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    // kill all running background threads
    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}