package com.indi.holders;

import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.indi.mundo.UserBase;
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
    private UserBase contact;

    public ContactsHolder(View itemView) {
        super(itemView);
        view = itemView;
    }

    public void bindContact(final UserBase contact) {
        instancia = this;
        this.contact = contact;
        usernameContacto = (TextView) view.findViewById(R.id.tvUsernameContact);
        usernameContacto.setText(contact.username);

        phoneNumberContact = (TextView) view.findViewById(R.id.tvNumeroContact);
        phoneNumberContact.setText(contact.email);


        btnShareRedes = (Button) view.findViewById(R.id.btnShare);
        btnShareRedes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


}
