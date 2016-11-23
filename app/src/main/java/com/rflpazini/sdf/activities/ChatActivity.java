package com.rflpazini.sdf.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.rflpazini.sdf.model.Message;
import com.rflpazini.sdf.R;
import com.rflpazini.sdf.utils.ChatAdapter;
import com.rflpazini.sdf.utils.Constants;
import com.rflpazini.sdf.utils.SendMessage;
import com.rflpazini.sdf.utils.UserLocalInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = ChatActivity.class.getSimpleName();

    private FloatingActionButton sendMessage;
    private EditText typedMessage;
    private ListView messagesContainer;
    private ArrayList<Message> history;
    private ArrayList<Message> serverHistory;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sendMessage = (FloatingActionButton) findViewById(R.id.chatSendButton);
        typedMessage = (EditText) findViewById(R.id.messageEdit);
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);

        loadServerHistory();
        setListeners();
    }

    private void setListeners() {
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = typedMessage.getText().toString();

                if (TextUtils.isEmpty(message)) {
                    return;
                }

                Message msg = new Message();
                msg.setMsgBody(message);
                msg.setMsgDate(DateFormat.getDateTimeInstance().format(new Date()));
                msg.setItsMe(true);
                msg.setMsgFrom(new UserLocalInfo(getApplicationContext()).getuName());

                typedMessage.setText("");
                history.add(msg);
                displayMessages(msg);

                Gson gMessage = new Gson();
                new SendMessage().execute(gMessage.toJson(msg));
            }
        });
    }

    private void displayMessages(Message msg) {
        adapter.add(msg);
        adapter.notifyDataSetChanged();

        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadServerHistory() {
        serverHistory = new ArrayList<Message>();
        history = new ArrayList<Message>();
        adapter = new ChatAdapter(ChatActivity.this, new ArrayList<Message>());
        messagesContainer.setAdapter(adapter);

        final Handler handler = new Handler();
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            new RetrieveMessages().execute();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                });
            }
        };
        timer.schedule(task, 0, 2000);
        history = serverHistory;
    }

    public class RetrieveMessages extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(Constants.MESSAGE_GET_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod(Constants.GET_REQUEST);
                httpURLConnection.connect();

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
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("RESPONSE: " + s);
            transformJson(s);
            updateLocalMessages();
        }
    }

    protected void transformJson(String s) {
        try {
            JSONArray jsonArray = new JSONArray(s);
            serverHistory = new ArrayList<Message>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject g = jsonArray.getJSONObject(i);
                Message msg = new Message();
                msg.setId(Integer.valueOf(g.getString("id")));
                msg.setMsgFrom(g.getString("msgFrom"));
                msg.setMsgBody(g.getString("msgBody"));
                msg.setMsgDate(g.getString("msgDate"));

                serverHistory.add(msg);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    protected void updateLocalMessages() {
        int ss, hs, def = 0;
        ss = serverHistory.size();
        hs = history.size();
        if (ss != hs) {
            def = ss - hs;
            for (int i = ss - def; i < ss; i++) {
                Message msg = serverHistory.get(i);
                history.add(msg);
                displayMessages(msg);
            }
        }
    }
}
