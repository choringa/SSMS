package com.indi.holders;

import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.indi.mundo.UserBase;
import com.indi.ssms.MainActivity;
import com.indi.ssms.R;

/**
 * Created by Choringa on 5/11/17.
 */

public class ContactsHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "ContactsHolder";

    private View view;
    private TextView usernameContacto, phoneNumberContact;
    private Button btnShareRedes;
    private ContactsHolder instancia;
    private ProgressDialog progressDialog;
    private MainActivity mainActivity;

    public ContactsHolder(View itemView, MainActivity mainActivity) {
        super(itemView);
        view = itemView;
        this.mainActivity = mainActivity;
    }

    public void bindContact(final UserBase contact) {
        instancia = this;

        usernameContacto = (TextView) view.findViewById(R.id.tvUsernameContact);
        usernameContacto.setText(contact.username);

        phoneNumberContact = (TextView) view.findViewById(R.id.tvNumeroContact);
        phoneNumberContact.setText(contact.email);


        btnShareRedes = (Button) view.findViewById(R.id.btnShare);
        btnShareRedes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: share button
                Log.i(TAG, "btnSharePressed");
            }
        });


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "btnContactPressed");
                mainActivity.iniciarChat(contact);
            }
        });
    }


}
