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

        client = new Client("181.215.69.116", 9999);


        Log.d("CLASSNAME", getClass().getName());

        edtSearchBar = findViewById(R.id.edtSearchBar);
        RecyclerView recyclerView = findViewById(R.id.mContactsView);

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("DDDLISTENER", "TEXT HAS CHANGED: " + s);

                userModels.clear();


                String users[] = client.request(
                        "-1",
                        "searchUsers",
                        s.toString().toLowerCase(Locale.ROOT)).split(" ");

                userModels.clear();
                for (String u : users) {
                    Log.d("XXXUSER", u);
                    userModels.add(new UserModel(u));
                }
                displayModels(recyclerView);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        edtSearchBar.addTextChangedListener(tw);
    }

    public void displayModels(RecyclerView recyclerView) {
        ContactsViewAdapter contactsViewAdapter
                = new ContactsViewAdapter(this, userModels);

        recyclerView.setAdapter(contactsViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}