package com.example.letstalk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatActivity extends AppCompatActivity {

    TextView chatName;
    EditText edtTypeMessage;
    Button btnSendMessage;
    LinearLayout linearLayout;
    String currentUser;
    Client client;
    static Handler handler = null;
    ScrollView scrollView;


    // variables to check if a new message got added in the database
    // compare oldState with newState later
    // for e.g
    // oldState = messages.length -> 13
    // newState = messages.length -> 14
    // now if you compare them and they arent equal that means a new message
    // got added to the chat and reload the view
    static int oldState = -2;
    static int newState = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        client = Client.getInstance();

        // get username of chat partner and own primary key
        // from UM_RecyclerViewAdapter.java, if clicked on a friend
        // in UsersActivity.java
        String primaryKey = getIntent().getStringExtra("key");
        String chatUsername = getIntent().getStringExtra("chatUserName");

        try {
            currentUser = FileUtility.readFromFile("username.txt", getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        edtTypeMessage = findViewById(R.id.edtTypeMessage);
        btnSendMessage = findViewById(R.id.btnSendMessage);

        chatName = findViewById(R.id.chatUser);
        chatName.setText(chatUsername);

        linearLayout = findViewById(R.id.chatLayout);
        scrollView = findViewById(R.id.scrollView2);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });


//        // those are all messages from the current chat
//        String[] splittedMessages = client.getAllChatMessages(
//                primaryKey, chatUsername
//        );
//
//
//        if (splittedMessages != null)
//            oldState = splittedMessages.length;

        showMessages(primaryKey, chatUsername);

        // now reload every half a second and look if a new message arrived
        // if yes reload the view with the new messages
        // if no do nothing
        // .. if (oldState != newState)
        // ......................

        reload(primaryKey, chatUsername);


        // if button is pressed send message to chat
        btnSendMessage.setOnClickListener(view ->
        {

            client.sendMessage(
                    primaryKey,
                    chatUsername,
                    String.valueOf(edtTypeMessage.getText())
            );

            // set edtTypeMessage empty and hide keyboard for better experience
            edtTypeMessage.setText("");
            View v = this.getCurrentFocus();
            if (v != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

        });
    }


    public void showMessages(String primaryKey, String chatUsername) {


        // clear all messages before showing the updated version of the chat
        linearLayout.removeAllViews();

        String[] splittedMessages = client.getAllChatMessages(
                primaryKey, chatUsername
        );


        if (splittedMessages != null)
            newState = splittedMessages.length;

        // scroll to bottom if a new message is in the database for the chat
        // with a new anonymous thread
        if (newState != oldState) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
            oldState = newState;
        }



        int numberOfMessages = splittedMessages.length;

        // k = 1 to skip first "." message k = 2 to skip both "." messages
//        int k = 0;
//        if (numberOfMessages >= 2)
//            k = 2;


        // loop trough "numberOfMessages" messages
        for (int k = 2; k < numberOfMessages; k++) {

            // [max 20:36] [nachricht]
            String sendingMsgUserData = "";
            String message = "";

            // message is built as following:
            // "[username time] [this is some example message]"

            // now we need to extract the two braces
            // look for braces and extract their content
            Matcher x = Pattern.compile("\\[(.*?)\\]").matcher(splittedMessages[k]);

            // first brace [] -> [username time]
            if (x.find())
                sendingMsgUserData = x.group(1);

            // second brace [] -> [this is some example message
            if (x.find())
                message = x.group(1);

            // if content could not be extracted just skip and go to the next message
            else
                continue;

            // add Username and time to LinearLayout in ScrollView
            TextView userNameText = new TextView(getApplicationContext());
            userNameText.setTextSize(25);
            userNameText.setTypeface(null, Typeface.BOLD);
            userNameText.setText(sendingMsgUserData + " :");
            linearLayout.addView(userNameText);
            // for e.g
            // username1 20:36:

            // now add chat text below the sendingMsgUserData text
            TextView chatText = new TextView(getApplicationContext());
            chatText.setTextSize(25);
            chatText.setText(message);
            linearLayout.addView(chatText);

            // now looking like that

            // username1 20:36
            // this is my super cool message

            // now just add a empty text just to create a while space between the messages
            TextView emptyFiller = new TextView(getApplicationContext());
            emptyFiller.setText(" ");
            linearLayout.addView(emptyFiller);

            // now looking like that

            // username1 20:36
            // this is my super cool message
            // .... white line
            // .. next message


        }
    }

    // create new thread which reloads all messages
    public void reload(String primaryKey, String chatUsername) {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

                showMessages(primaryKey, chatUsername);
                reload(primaryKey, chatUsername);
            }
        }, 500);
    }

    // if current activity is closed kill the reload thread
    // should only run when in ChatsActivity
    @Override
    public void onDestroy() {
        // Do your stuff here
        super.onDestroy();
//        handler.removeCallbacks();
        handler.removeCallbacksAndMessages(null);
    }
}