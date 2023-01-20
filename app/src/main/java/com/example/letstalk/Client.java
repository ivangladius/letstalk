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

    private static Client instance = null;

    // singleton pattern
    public static Client getInstance() {

        if (instance == null)
            instance = new Client("181.215.69.116", 9999);

        return instance;
    }

    private Socket clientSocket;
    private InetAddress address;
    private final Integer port;


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
//            out = new ObjectOutputStream(clientSocket.getOutputStream());

        } catch (IOException e) {
            System.out.println("COULD NOT CONNECT!!!");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void closeSocket() {
        try {
            if (clientSocket != null)
                clientSocket.close();
        } catch (IOException ignore) {
        }
    }

    public String readFromServer(Socket clientSocket, BufferedReader reader) throws IOException, JSONException {

        // read data from Server (json as string format)
        String result = reader.readLine();

        // trim object ready to extract information
        int i = result.indexOf("{");
        result = result.substring(i);
        JSONObject json = new JSONObject(result.trim());

        return json.get("payload").toString();
    }

    public void sendToServer(Socket clientSocket, String operation, String payload, PrintWriter writer) throws IOException, JSONException {

        JSONObject json = new JSONObject();
        json.put("operation", operation);
        json.put("payload", payload);

        // write data to Server (json will be converted to a String)
        writer.println(json);

    }


    public String request(String operation, String payload) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> payloadFuture;


        payloadFuture = executor.submit(() -> {

            connect();

            BufferedReader reader = null;
            PrintWriter writer = null;

            try {
                reader =
                        new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer =
                        new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (writer != null)
                sendToServer(clientSocket, operation, payload, writer);

            String data = null;
            if (reader != null)
                data = readFromServer(clientSocket, reader);

            if (reader != null && writer != null) {
                reader.close();
                writer.close();
            }

            return data;

        });

        String data = null;
        try {
            data = payloadFuture.get(5L, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }

        executor.shutdown();

        closeSocket();

        return data;
    }

    public String createUser(String username, String email, String password) {
        return request("createUser",
                username + " " + email + " " + password);
    }

    public String login(String email, String password) {
        // if user enters garbage do nothing
        return request(
                "login",
                email + " " + password);
    }

    public void addContact(String currentUserId, String contactUsername) {
        String secondUserKey = request(
                "getIdByUsername",
                contactUsername);

        request(
                "sendMessage",
                currentUserId + " " + secondUserKey + " .");

        request(
                "sendMessage",
                secondUserKey + " " + currentUserId + " .");

    }

    public String listFriends(String username) {
        return request(
                "listFriends",
                username);
    }

    public String sendMessage(String primaryKey, String chatUsername, String message) {

        String partnerKey = request(
                "getIdByUsername",
                chatUsername
        );
        String msgToSend =
                primaryKey + " " + partnerKey + " " + "[" +
                        message.replace("\n", " ").replace("\r", " ").concat("]");

        return request(
                "sendMessage",
                msgToSend
        );
    }

    public String[] getAllChatMessages(String primaryKey, String chatUsername) {
        String payload = request(
                "getMessages",
                primaryKey + " " + chatUsername);

        if (payload != null)
            return payload.split("\t");

        return null;
    }

    public String[] searchUsers(String token) {
        String payload = request(
                "searchUsers",
                token.toString().toLowerCase(Locale.ROOT));

        if (payload != null)
            return payload.split(" ");

        return null;
    }

    public String getEmailByUsername(String username) {
        return request(
                "getEmailByUsername",
                username);
    }

    public String changeUsername(String userId, String usernameToChangeTo) {
        return request(
                "changeUsername",
                userId + " " + usernameToChangeTo);
    }

    public String changeEmail(String userId, String emailToChangeTo) {
        return request(
                "changeEmail",
                userId + " " + emailToChangeTo);
    }

    public String changePassword(String userId, String passwordToChangeTo) {
        return request(
                "changePassword",
                userId + " " + passwordToChangeTo);
    }
}

