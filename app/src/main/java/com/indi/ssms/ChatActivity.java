package com.indi.ssms;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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
        loadMessagesFB();
        displayChatMessage();
    }

    /**
     * Método que manda los mensajes en Firebase. Se guarda uno para el usuario y uno para el contacto. El Chat se diferencia con un ID que es el ID de cada
     * usuario en el chat seprados por un guión bajo '_' .
     */
    private void sendMessageFB() {
        EditText inputMessage = (EditText) findViewById(R.id.message_input);
        //Guarda el mensaje para el en la base
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("messages").child(contact.id+"_"+firebaseAuth.getCurrentUser().getUid());
        ChatMessage newMessage = new ChatMessage(inputMessage.getText().toString(), contact.username);
        dbRef.push().setValue(newMessage);
        //Guarda el mensaje para su contacto en la base
        dbRef = FirebaseDatabase.getInstance().getReference("messages").child(firebaseAuth.getCurrentUser().getUid()+"_"+contact.id);
        dbRef.push().setValue(newMessage);

        inputMessage.setText("");
    }

    /**
     * Metodo que muestra los mensajes cargados en la lista.
     */
    private void displayChatMessage() {
        RecyclerView chatList = (RecyclerView) findViewById(R.id.messages_list_chat);
        chatList.addItemDecoration(new DividerItemDecoration(this, null));
        chatList.setHasFixedSize(true);
        chatList.setLayoutManager(new LinearLayoutManager(this));
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
