package com.indi.ssms;

import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.indi.adapters.DividerItemDecoration;
import com.indi.adapters.ListChatAdapter;
import com.indi.mundo.ChatMessage;
import com.indi.mundo.UserBase;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private FloatingActionButton fabSendMessageButton;
    private FirebaseAuth firebaseAuth;
    private UserBase contact;
    private ArrayList<ChatMessage> messagesList;
    private ListChatAdapter listChatAdapter;
    private RecyclerView chatList;
    private LinearLayoutManager mLayoutManager;
    private Toolbar toolbar;

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
                sendMessageFB();
            }
        });
        messagesList = new ArrayList<>();
        setToolbar();
        if (getSupportActionBar() != null) // Habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadMessagesFB();
        displayChatMessage();
        chatList.smoothScrollToPosition(messagesList.size());
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        TextView toolbarTitleEmail = findViewById(R.id.toolbar_title_email);
        toolbarTitle.setText(contact.username);
        toolbarTitleEmail.setText(contact.email);
        setSupportActionBar(toolbar);
    }

    /**
     * Método que manda los mensajes en Firebase. Se guarda uno para el usuario y uno para el contacto. El Chat se diferencia con un ID que es el ID de cada
     * usuario en el chat seprados por un guión bajo '_' .
     */
    private void sendMessageFB() {
        EditText inputMessage = (EditText) findViewById(R.id.message_input);
        if(!inputMessage.getText().toString().trim().equals("")) {
            //Guarda el mensaje para el en la base
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("messages").child(contact.id + "_" + firebaseAuth.getCurrentUser().getUid());
            ChatMessage newMessage = new ChatMessage(inputMessage.getText().toString(), firebaseAuth.getCurrentUser().getDisplayName());
            dbRef.push().setValue(newMessage);
            //Guarda el mensaje para su contacto en la base
            dbRef = FirebaseDatabase.getInstance().getReference("messages").child(firebaseAuth.getCurrentUser().getUid() + "_" + contact.id);
            dbRef.push().setValue(newMessage);
            inputMessage.setText("");
        }
    }

    /**
     * Metodo que muestra los mensajes cargados en la lista.
     */
    private void displayChatMessage() {
        chatList = (RecyclerView) findViewById(R.id.messages_list_chat);
        chatList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setStackFromEnd(true);
        chatList.setLayoutManager(mLayoutManager);
        listChatAdapter = new ListChatAdapter(messagesList,this);
        chatList.setAdapter(listChatAdapter);
    }


    /**
     * Metodo que carga los mensajes de Firebase. Siempre identifica su chat debido al id, porque empieza por su id + '_'  + idContacto.
     */
    private void loadMessagesFB() {
        FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = fbDatabase.getReference("messages").child(contact.id+"_"+firebaseAuth.getCurrentUser().getUid());

        firebaseAuth = FirebaseAuth.getInstance();
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                messagesList.add(chatMessage);
                chatList.smoothScrollToPosition(messagesList.size());
                listChatAdapter.notifyDataSetChanged();
                //progressBarLoadContacts.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
