package com.rflpazini.sdf.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by rflpazini on 11/1/16.
 */

public class SendMessage extends AsyncTask<String, Void, String> {
    private static final String TAG = SendMessage.class.getSimpleName();

    @Override
    protected String doInBackground(String... gson) {
        try {
            URL url = new URL(Constants.MESSAGE_POST_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod(Constants.POST_REQUEST);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.connect();
            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(gson[0]);
            wr.flush();
            wr.close();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();

                return stringBuilder.toString();
            } finally {
                httpURLConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    protected void onPostExecute(String response) {
        if (response == null) {
            Log.e(TAG + "response", "There was an error with the resquest \nThe response of this is " + response);
        } else {
            Log.d(TAG, response);
        }

    }
}
