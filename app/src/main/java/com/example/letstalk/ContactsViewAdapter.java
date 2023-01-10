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
            client = new Client("181.215.69.116", 9999);

            btnUsername.setOnClickListener(view -> {
                Log.d("ContactsView", "onClick: " + getAdapterPosition());
                Log.d("ContactsView", "MESSAGEING :" + btnUsername.getText());
                String currentUser = null;
                String username = null;
                try {
                    currentUser = FileUtility.readFromFile("primary_key.txt", context);
                    username = FileUtility.readFromFile("username.txt", context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String secondUser = client.request(
                        "-1",
                        "getIdByUsername",
                        btnUsername.getText().toString());

                Log.d("XXXCURRENT", "PRIM: " + currentUser + " SEC: " + secondUser);

                client.request(
                        "-1",
                        "sendMessage",
                        currentUser + " " + secondUser + " .");

                Log.d("SSSENDING", "Message: " + currentUser + " " + secondUser + " .");


                context.startActivity(new Intent(context, MainActivity.class));


//                Intent intent = new Intent(context, UsersActivity.class);
//                intent.putExtra("username", username); //Optional parameters
//                intent.putExtra("activity", "1"); //Optional parameters
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//                context.startActivity(intent);
            });
        }

    }

}
