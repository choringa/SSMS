package com.indi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indi.holders.ContactsHolder;
import com.indi.holders.MessageHolder;
import com.indi.mundo.ChatMessage;
import com.indi.mundo.UserBase;
import com.indi.ssms.ChatActivity;
import com.indi.ssms.MainActivity;
import com.indi.ssms.R;

import java.util.List;

/**
 * Created by Choringa on 5/11/17.
 */

public class ListChatAdapter extends RecyclerView.Adapter<MessageHolder> {

    private static final String TAG = "ListContactsAdapter";

    private List<ChatMessage> messages;
    private ChatActivity chatActivity;

    public ListChatAdapter(List<ChatMessage> messages, ChatActivity chatActivity) {
        this.messages = messages;
        this.chatActivity = chatActivity;
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chat_message_item, parent, false);
        MessageHolder ch = new MessageHolder(v, chatActivity);
        return ch;
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {
        holder.bindMessage(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


}
