package com.example.letstalk;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
            String replyString = in.readLine();


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
        // if user enters garbage do nothing
        if (email.length() == 0 || password.length() == 0)
            return null;
        return request(
                "-1",
                "login",
                email + " " + password);
    }

    public void addContact(String currentUserId, String contactUsername) {
        String secondUserKey = request(
                "-1",
                "getIdByUsername",
                contactUsername);

        request(
                "-1",
                "sendMessage",
                currentUserId + " " + secondUserKey + " .");

        request(
                "-1",
                "sendMessage",
                secondUserKey + " " + currentUserId + " .");

    }

    public String listFriends(String username) {
        return request(
                "-1",
                "listFriends",
                username);
    }

    public void sendMessage(String primaryKey, String chatUsername, String message) {

        String partnerKey = request(
                "-1",
                "getIdByUsername",
                chatUsername
        );
        String msgToSend =
                primaryKey + " " + partnerKey + " " + "[" +
                        message.replace("\n", " ").replace("\r", " ").concat("]");

        request(
                "-1",
                "sendMessage",
                msgToSend
        );
    }
    public String[] getAllChatMessages(String primaryKey, String chatUsername) {
        return request(
                "-1",
                "getMessages",
                primaryKey + " " + chatUsername).split("\t");
    }

    public String[] searchUsers(String token) {
        return request(
                "-1",
                "searchUsers",
                token.toString().toLowerCase(Locale.ROOT)).split(" ");
    }
    public String getEmailByUsername(String username) {
        return request(
                "-1",
                "getEmailByUsername",
                username);
    }

    public void changeUsername(String userId, String usernameToChangeTo) {
        request(
                "-1",
               "changeUsername",
                userId + " " + usernameToChangeTo);
    }

    public void changeEmail(String userId, String emailToChangeTo) {
        request(
                "-1",
                "changeEmail",
                userId + " " + emailToChangeTo);
    }
    public void changePassword(String userId, String passwordToChangeTo) {
        request(
                "-1",
                "changePassword",
                userId + " " + passwordToChangeTo);
    }
}

