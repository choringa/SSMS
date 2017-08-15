package com.indi.ssms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.indi.mundo.UserBase;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private Button btnAttachment;
    private ImageButton btnSend;
    private EditText etMensaje;

    private FirebaseAuth firebaseAuth;
    private UserBase contact;
    private int dialogID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        contact = (UserBase) getIntent().getSerializableExtra("contact");
        dialogID = getIntent().getIntExtra("dialogID", 0);

        firebaseAuth = FirebaseAuth.getInstance();



    }


}
