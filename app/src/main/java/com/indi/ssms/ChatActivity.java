package com.indi.ssms;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.indi.mundo.ChatMessage;
import com.indi.mundo.UserBase;


public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private FloatingActionButton fabSendMessageButton;
    private FirebaseListAdapter<ChatMessage> adapter;
    private FirebaseAuth firebaseAuth;
    private UserBase contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_chat);

        contact = (UserBase) getIntent().getSerializableExtra("contact");

        firebaseAuth = FirebaseAuth.getInstance();
        fabSendMessageButton = (FloatingActionButton) findViewById(R.id.fab_send_message_button);
        fabSendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText inputMessage = (EditText) findViewById(R.id.edit_chat_message);
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("messages");
                ChatMessage newMessage = new ChatMessage(inputMessage.getText().toString(), contact.username);
                dbRef.push().setValue(newMessage);
                inputMessage.setText("");
            }
        });

        displayChatMessage();
    }

    private void displayChatMessage() {
        ListView listView = (ListView) findViewById(R.id.messages_list_chat);
        adapater = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.list_chat_message_item, FirebaseDatabase.getInstance().getReference()){

        }
    }


}
