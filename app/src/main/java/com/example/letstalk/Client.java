package com.example.letstalk;

import android.util.Log;

import java.io.*;
import java.net.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    private static final Client instance = new Client("181.215.69.116", 9999);
    // singleton pattern
    public static Client getInstance() {
        return instance;
    }

    private Socket clientSocket;
    private InetAddress address;
    private final Integer port;

    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Client(String address, Integer port) {
        try {
            this.address = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            System.out.println("HOST NOT REACHABLE !!!");
            e.printStackTrace();
        }
        this.port = port;
    }

    private void connect() {
        try {
            clientSocket = new Socket(this.address, this.port);
            out = new ObjectOutputStream(clientSocket.getOutputStream());

        } catch (IOException e) {
            System.out.println("COULD NOT CONNECT!!!");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void closeSocket() {
        try {
            clientSocket.close();
        } catch (IOException ignore) {
        }
    }

    public String request(String key, String operation, String payload) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<JSONObject> jsonFuture;

        jsonFuture = executor.submit(() -> {

            connect();

            PrintWriter out;
            BufferedReader in;

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            JSONObject jsonMessage = new JSONObject();
            jsonMessage.put("key", key);
            jsonMessage.put("operation", operation);
            jsonMessage.put("payload", payload);

            out.println(jsonMessage.toString());
            Log.d("XXXCLIENT", jsonMessage.toString());
            String replyString = in.readLine();
            Log.d("XXXCLIENT", jsonMessage.toString());


            int i = replyString.indexOf("{");
            replyString = replyString.substring(i);
            JSONObject json = new JSONObject(replyString.trim());
            System.out.println(json.toString(4));

            return json;
        });

        JSONObject data = null;
        try {
            data = jsonFuture.get(5L, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }

        executor.shutdown();

        closeSocket();

        if (data != null) {
            try {
                return data.get("payload").toString();
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public String createUser(String username, String email, String password) {
        return request("-1", "createUser",
                username + " " + email + " " + password);
    }

    public String login(String email, String password) {
        System.out.println("email len: " + email.length() + " password len: " + password.length());
        if (email.length() == 0 || password.length() == 0)
            return null;
        return request(
                "-1",
                "login",
                email + " " + password);
    }


}

