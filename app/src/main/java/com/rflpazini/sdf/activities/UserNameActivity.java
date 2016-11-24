package com.rflpazini.sdf.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.rflpazini.sdf.model.User;
import com.rflpazini.sdf.R;
import com.rflpazini.sdf.utils.Constants;
import com.rflpazini.sdf.utils.Dialogs;
import com.rflpazini.sdf.utils.UserLocalInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserNameActivity extends AppCompatActivity {

    private Button btLogin;
    private Button btInfo;
    private EditText userName;
    private ProgressDialog loginProgressDialog;

    public static final String TAG = UserNameActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);

        btInfo = (Button) findViewById(R.id.doubtButton);
        btLogin = (Button) findViewById(R.id.loginButton);
        userName = (EditText) findViewById(R.id.nameDialog);

        loginProgressDialog = new ProgressDialog(this);
        loginProgressDialog.setMessage(getString(R.string.dialog_sign));
        loginProgressDialog.setCancelable(false);

        setListeners();
    }

    private void setListeners() {
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userName.getText().toString().trim().length() != 0) {
                    String user = userName.getText().toString();
                    new RetrieveInfoLogin().execute(user);
                }
            }
        });

        btInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserNameActivity.this);
                builder.setMessage(getString(R.string.info_msg))
                        .setTitle("Duvidas sobre o Sinal de fumaça")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    class RetrieveInfoLogin extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            loginProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... user) {
            Log.i(TAG, user[0]);

            try {
                URL urlAuth = new URL(Constants.AUTH_USER_API_URL + user[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) urlAuth.openConnection();
                urlConnection.setRequestMethod(Constants.POST_REQUEST);
                urlConnection.setConnectTimeout(10000);
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                new Dialogs().errorMessage(UserNameActivity.this, getString(R.string.dialog_error_connection));
                Log.e(TAG + "response", "There was an error with the resquest \nThe response of this is " + response);
            } else {
                Gson g = new Gson();
                User user = g.fromJson(response, User.class);

                try {
                    SharedPreferences localPreferences = getSharedPreferences(Constants.USER_LOCAL_INFO, 0);
                    SharedPreferences.Editor editor = localPreferences.edit();
                    if (user.getId() > 0) {
                        editor.putInt("id", user.getId());
                    }
                    editor.putString("userName", user.getUserName());
                    editor.putString("token", user.getUserToken());
                    editor.commit();

                }catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
                Intent chatIntent = new Intent(UserNameActivity.this, ChatActivity.class);
                startActivity(chatIntent);
                UserNameActivity.this.finish();
            }
            loginProgressDialog.dismiss();
        }
    }
}
