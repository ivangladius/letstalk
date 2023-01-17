package com.example.letstalk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactsActivity extends AppCompatActivity {


    EditText edtSearchBar;
    ArrayList<UserModel> userModels = new ArrayList<>();
    Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        client = Client.getInstance();



        edtSearchBar = findViewById(R.id.edtSearchBar);
        RecyclerView recyclerView = findViewById(R.id.mContactsView);

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            // this function will get called if the user input in
            // the search bar for finding users has changed
            // for example :
            // m
            // ma
            // max
            // so this function gets called 3 times
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // first clear all user
//                userModels.clear();
                userModels.clear();

                // now get all users with the letter in variable "s"
                String[] users = client.searchUsers(s.toString());

                // now add all found user to userModels list
                for (String u : users)
                    userModels.add(new UserModel(u));

                // now display all found users
                displayModels(recyclerView);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        // needed to override the functions above
        edtSearchBar.addTextChangedListener(tw);
    }

    // display all found users in userModels, and displays them
    public void displayModels(RecyclerView recyclerView) {
        ContactsViewAdapter contactsViewAdapter
                = new ContactsViewAdapter(this, userModels);

        recyclerView.setAdapter(contactsViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}