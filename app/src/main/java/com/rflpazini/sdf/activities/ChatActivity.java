package com.rflpazini.sdf.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.rflpazini.sdf.model.Message;
import com.rflpazini.sdf.R;
import com.rflpazini.sdf.utils.ChatAdapter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    private FloatingActionButton sendMessage;
    private EditText typedMessage;
    private ListView messagesContainer;
    private ArrayList<Message> history;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sendMessage = (FloatingActionButton) findViewById(R.id.chatSendButton);
        typedMessage = (EditText) findViewById(R.id.messageEdit);
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);

        loadDummyHistory();
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
                typedMessage.setText("");
                history.add(msg);

                displayMessages(msg);
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

    private void loadDummyHistory() {
        history = new ArrayList<Message>();

        Message msg = new Message();
        msg.setId(1);
        msg.setItsMe(false);
        msg.setMsgBody("Mota não come cu, só pensa em breja e coisa de playboy");
        msg.setMsgFrom("Irajá");
        msg.setMsgDate(DateFormat.getDateTimeInstance().format(new Date()));
        history.add(msg);

        adapter = new ChatAdapter(ChatActivity.this, new ArrayList<Message>());
        messagesContainer.setAdapter(adapter);

        for (int i = 0; i < history.size(); i++) {
            Message message = history.get(i);
            displayMessages(message);
        }
    }
}