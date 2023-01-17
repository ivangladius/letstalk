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

    // final because it never will change
    // static because we need access from isUserLoggedIn to the Strings
    static private final String loginFile = "login_status.txt";
    static private final String keyFile = "primary_key.txt";
    static private final String usernameFile = "username.txt";

    static public String readFromFile(String filename, Context context) throws IOException {
        FileInputStream fis = context.openFileInput(filename);
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));

        return reader.readLine();
    }

    static public void writeToFile(String filename, String fileContent, Context context) throws IOException {
        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(fileContent.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String isUserLoggedIn(Context context) {

        // 2 lines needed for .exist() function
        File _loginFile = new File(context.getFilesDir(), loginFile);
        File _usernameFile = new File(context.getFilesDir(), usernameFile);

        // to return later, because we want to pass the username to UsersActivity.java
        // if he is logged in
        String username = null;

        String loginStatus = null;

        try {
            // check if files exist
            if (_loginFile.exists() && _usernameFile.exists()) {

                loginStatus = FileUtility.readFromFile(loginFile, context);
                username = FileUtility.readFromFile(usernameFile, context);

                // check if user was logged in -> text of login file is "true"
                // if not logged -> text of login file is "false"

                // if user is logged in, return true
                if (loginStatus.equals("true") && !username.equals("none"))
                    return username;

            } else {
                // if files do NOT exist, user first installed the app
                // lets just create the files with "logged out" status in it

                FileUtility.writeToFile(loginFile, "false", context);
                FileUtility.writeToFile(usernameFile, "none", context);

                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
