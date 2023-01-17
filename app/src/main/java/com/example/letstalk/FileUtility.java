package com.example.letstalk;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileUtility {

    public static String loginFile = "login_status.txt";
    private static String keyFile = "primary_key.txt";
    private static String usernameFile = "username.txt";

    static public String readFromFile(String filename, Context context) throws IOException {
        FileInputStream fis = context.openFileInput(filename);
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));

        return reader.readLine();
    }

    static public void writeToFile(String filename, String fileContent, Context context) throws IOException {
        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(fileContent.getBytes(StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean isAlreadyLoggedIn(Context context) {

        File _loginFile = new File(context.getFilesDir(), loginFile);
        File _usernameFile = new File(context.getFilesDir(), usernameFile);

        if (_loginFile.exists() && _usernameFile.exists()) {
            String loginStatus = FileUtility.getLoginStatusFromFile(context);
            String username = FileUtility.getUsernameFromFile(context);
            if (loginStatus.equals("true") && !username.equals("none"))
                return true;
            else {
                FileUtility.writeLoginStatusToFile("false", context);
                FileUtility.writeUsernameToFile("none", context);
                return false;
            }
        }
        return false;
    }

    public static String getUsernameFromFile(Context context) {
        String username = null;
        try {
            username = FileUtility.readFromFile(usernameFile, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return username;
    }

    public static String getUserIdFromFile(Context context) {
        String userId = null;
        try {
            userId = FileUtility.readFromFile(keyFile, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userId;
    }
    public static String getLoginStatusFromFile(Context context) {
        String loginStatus = null;
        try {
            loginStatus = FileUtility.readFromFile(loginFile, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loginStatus;
    }
    public static void writeUsernameToFile(String content, Context context) {
        try {
            FileUtility.writeToFile(usernameFile, content, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeLoginStatusToFile(String content, Context context) {
        try {
            FileUtility.writeToFile(loginFile, content, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeUserIdToFile(String content, Context context) {
        try {
            FileUtility.writeToFile(keyFile, content, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
