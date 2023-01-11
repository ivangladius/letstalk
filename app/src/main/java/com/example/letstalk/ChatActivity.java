package com.example.letstalk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
    static int oldState = -2;
    static int newState = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        client = new Client("181.215.69.116", 9999);

        String primaryKey = getIntent().getStringExtra("key");
        String chatUserName = getIntent().getStringExtra("chatUserName");

        try {
            currentUser = FileUtility.readFromFile("username.txt", getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        edtTypeMessage = findViewById(R.id.edtTypeMessage);
        btnSendMessage = findViewById(R.id.btnSendMessage);

        chatName = findViewById(R.id.chatUser);
        chatName.setText(chatUserName);

        linearLayout = findViewById(R.id.chatLayout);
        scrollView = findViewById(R.id.scrollView2);

//        scrollView.scrollTo(0, scrollView.getBottom());


        // TODO
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

//        scrollView.fullScroll(View.FOCUS_DOWN);

//        linearLayout.removeAllViews();
//
//        String fullMessages = client.request(
//                "-1",
//                "getMessages",
//                primaryKey + " " + chatUserName);


        // TODO:
        /*
        Matcher m = Pattern.compile("\\[(.*?)\\]").matcher(x);
        while (m.find()) {
            System.out.println(m.group(1));
        }
         */


        String fullMessages = client.request(
                "-1",
                "getMessages",
                primaryKey + " " + chatUserName);


        // strip [ .... ] of message
        oldState = fullMessages.split("\t").length;
        Log.d("XXXOLD", "oldstate: " + oldState);

        showMessages(primaryKey, chatUserName);
        reload(primaryKey, chatUserName);


        btnSendMessage.setOnClickListener(view ->
        {

            String partnerKey = client.request(
                    "-1",
                    "getIdByUsername",
                    chatUserName
            );
            String msgToSend =
                    primaryKey + " " + partnerKey + " " + "[" +
                            String.valueOf(edtTypeMessage.getText()).replace("\n", " ").replace("\r", " ").concat("]");
            Log.d("XEDT", "MSG TO SEND: " + primaryKey);

//            client.request(
//                    "-1",
//                    "sendMessage",
//                    primaryKey + " " + partnerKey + " " + String.valueOf(edtTypeMessage.getText()).replace("\n", " ").replace("\r", " ")
//            );
            client.request(
                    "-1",
                    "sendMessage",
                    msgToSend
            );
//            Log.d("XEDT", "EDIT: " + String.valueOf(edtTypeMessage.getText()).replace("\n", " ").replace("\r", " "));
//            text = text.replace("\n", "").replace("\r", "");
            finish();
            startActivity(getIntent());
//            setContentView(R.layout.activity_chat);
//            showMessages(primaryKey, chatUserName);
        });
    }


    public void showMessages(String primaryKey, String chatUserName) {


        linearLayout.removeAllViews();

        String fullMessages = client.request(
                "-1",
                "getMessages",
                primaryKey + " " + chatUserName);


        // strip [ .... ] of message
        // TODO
        String[] messages = fullMessages.split("\t");
        newState = messages.length;
        if (newState != oldState) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
            oldState = newState;
        }
////            scrollView.scrollTo(0, scrollView.getBottom());
//            scrollView.fullScroll(View.FOCUS_DOWN);
//        }
        Log.d("LLLMESSAGE", fullMessages);

        for (String m : messages) {

            String currentUser = "";
            String message = "";
            Matcher x = Pattern.compile("\\[(.*?)\\]").matcher(m);
            if (x.find())
                currentUser = x.group(1);
            if (x.find())
                message = x.group(1);
            else {
                Log.d("XSKIP", "SKIPPED");
                continue;
            }
            Log.d("KCURRENT", "currentUser: " + currentUser + " message: " + message);

//            String sendingUser = client.request(
//                    "-1",
//                    "getUsername",
//                    currentUser
//            );

            TextView userNameText = new TextView(getApplicationContext());
            userNameText.setTextSize(25);
            userNameText.setTypeface(null, Typeface.BOLD);
            userNameText.setText(currentUser + " :");
            linearLayout.addView(userNameText);

            TextView chatText = new TextView(getApplicationContext());
            chatText.setTextSize(25);
            chatText.setText(message);
            linearLayout.addView(chatText);

            TextView emptyFiller = new TextView(getApplicationContext());
            emptyFiller.setText(" ");
            linearLayout.addView(emptyFiller);
//            String msg = currentUser + ": " + m.substring(1).substring(0, m.length() - 2);
//            String msg = currentUser + ":\n" + message;

            Log.d("XXXSHOW", message);

//            linearLayout.addView(chatText);

        }
    }

    public void reload(String primaryKey, String chatUserName) {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

                showMessages(primaryKey, chatUserName);
                reload(primaryKey, chatUserName);
            }
        }, 500);
    }

    @Override
    public void onDestroy() {
        // Do your stuff here
        super.onDestroy();
//        handler.removeCallbacks();
        handler.removeCallbacksAndMessages(null);
    }
}