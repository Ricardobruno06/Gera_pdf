package com.example.ivan.chartexample;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by ivan on 6/6/17.
 */

public class Utils {

    private static final String FILE_PATH = "/storage/emulated/0/heart_rate_outliers.txt";

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE };

    private static String readFromFile(String path) {
        StringBuilder builder = new StringBuilder();

        try{
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while((line = br.readLine()) != null)
                builder.append(line + "\n");
            br.close();
        }catch(IOException ignored){}
        return builder.toString();
    }

    static float[] getOutliers() {
        String str = readFromFile(FILE_PATH);
        String[] strSplit = str.split("\n");
        float[] floats = new float[strSplit.length];

        if (strSplit.length > 1) {

            for (int i = 0; i < strSplit.length; i++) {
                floats[i] = Float.parseFloat(strSplit[i].split("&>")[1]);
                System.out.println(" >> " +floats[i]);
            }

        }

        return floats;
    }

    static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

}
