package com.indi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indi.holders.ContactsHolder;
import com.indi.mundo.UserBase;
import com.indi.ssms.MainActivity;
import com.indi.ssms.R;

import java.util.List;

/**
 * Created by Choringa on 5/11/17.
 */

public class ListContactsAdapter extends RecyclerView.Adapter<ContactsHolder> {

    private static final String TAG = "ListContactsAdapter";

    private List<UserBase> contactos;
    private MainActivity mainActivity;

    public ListContactsAdapter(List<UserBase> contactos, MainActivity mainActivity) {
        this.contactos = contactos;
        this.mainActivity = mainActivity;
    }

    @Override
    public ContactsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_adapter_contacts, parent, false);
        ContactsHolder ch = new ContactsHolder(v);
        return ch;
    }

    @Override
    public void onBindViewHolder(ContactsHolder holder, int position) {
        holder.bindContact(contactos.get(position));
    }

    @Override
    public int getItemCount() {
        return contactos.size();
    }


}
