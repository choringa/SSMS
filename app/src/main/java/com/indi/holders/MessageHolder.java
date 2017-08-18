package com.indi.holders;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.indi.mundo.ChatMessage;
import com.indi.ssms.ChatActivity;
import com.indi.ssms.R;

/**
 * Created by david.arteaga on 18/8/2017.
 */

public class MessageHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "MessageHolder";

    private ChatActivity chatActivity;
    private View view;

    public MessageHolder(View itemView, ChatActivity chatActivity) {
        super(itemView);
        view = itemView;
        this.chatActivity = chatActivity;
    }

    public void bindMessage(ChatMessage chatMessage){
        TextView message, user, time;
        message = (TextView) view.findViewById(R.id.mesagge_text_item_message);
        user = (TextView)view.findViewById(R.id.username_item_message);
        time = (TextView) view.findViewById(R.id.time_item_message);

        message.setText(chatMessage.getMessageText());
        user.setText(chatMessage.getMessageUser());
        time.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", chatMessage.getMessageTime()));
    }
}
