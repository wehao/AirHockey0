package com.example.wehao.airhockey;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextResourceReader {
    private static final String TAG = "TextResourceReader";
    public static String readTextFileFromResource(Context context, int resourceId) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(nextLine);
                stringBuilder.append("\n");
            }
        } catch (IOException e) {
            Log.e(TAG, "readTextFileFromResource: " + e.toString());
        } catch (Resources.NotFoundException nfe) {
            Log.e(TAG, "readTextFileFromResource: " + nfe.toString());
        }

        return stringBuilder.toString();
    }
}
