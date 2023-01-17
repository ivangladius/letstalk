package com.example.letstalk;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;


public class ContactsViewAdapter
        extends RecyclerView.Adapter<ContactsViewAdapter.MyViewHolder> {

    static Context context;
    ArrayList<UserModel> userModels;
    Client client;

    public ContactsViewAdapter(Context context, ArrayList<UserModel> userModels) {
        this.context = context;
        this.userModels = userModels;

    }

    @NonNull
    @Override
    public ContactsViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new ContactsViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewAdapter.MyViewHolder holder, int position) {
        holder.btnUsername.setText(userModels.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    public static class MyViewHolder
            extends RecyclerView.ViewHolder {

        Button btnUsername = itemView.findViewById(R.id.btnUsername);
        Client client;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            client = Client.getInstance();


            //  $$$$  IMPORTANT $$$$:
            // Sending messages works like that:
            // you need to provide both primary keys for the current User and the partner you want to add
            // for example 7 13 "msg"
            // where 7 is the primary key of the one who is sending the message
            // 13 is the primary key of the one who is receiving the message
            // and "msg" is the message to be sent
            // so to add a user we use a system that we just send each other a dot
            // like this .... client.request("-1", "sendMessage", "7 13 .");
            // and            client.request("-1", "sendMessage", "13 7 .");
            // Now we know this two are friends, the dots are later invisible


            // below everything happens if the button is pressed in "Add contact" after clicking on
            // a user you searched with the search bar in ContactsAcitivy.java ( edtSearchBar )

            btnUsername.setOnClickListener(view -> {
                String currentUserKey = null;
                // Get the user primary Key from files from the android filesystem
                try {
                    currentUserKey = FileUtility.readFromFile("primary_key.txt", context);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // we have the username of the partner (btnUsername with .getText())
                // where btnUsername has the username of the User stored, you searched
                // with edtSearchbar (defined File: ContactsActivity, Line: 22)
                // so if you click add Contacts and search for users, every user will be a button
                // for example, search: m   (edtSearchBar)
                // | max     |   btnUsername.setText("max");
                // | mubi    |   btnUsername.setText("mubi");
                // | mohamed |   btnUsername.setText("mohamed");

                // (see Line: 41) and imagine this in a loop for as many users there a found
                // with the search input you provide


                // Now if you click on Button this function will be called
                // and to retrieve the username of the button we call
                // btnUsername.getText();


                // we need the primary key of the User we want to add
                // so we call getIdByUsername(), with his username
                // where (btnUsername.getText().toString()); is the username
                // we clicked on


                // add contact on which button the user clicked
                client.addContact(
                        currentUserKey,
                        String.valueOf(btnUsername.getText())
                );




                // after adding contact jump back to MainActivity
                context.startActivity(new Intent(context, MainActivity.class));
            });
        }
    }
}
