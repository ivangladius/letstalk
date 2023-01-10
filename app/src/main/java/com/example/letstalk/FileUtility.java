package com.example.letstalk;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileUtility {

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
}
