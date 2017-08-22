package com.indi.holders;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
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
        TextView messageUser, userUser, timeUser, messageContact, userContact, timeContact;
        messageUser = view.findViewById(R.id.mesagge_text_item_message);
        userUser = view.findViewById(R.id.username_item_message);
        timeUser = view.findViewById(R.id.time_item_message);


        messageContact = view.findViewById(R.id.contact_mesagge_text_item_message);
        userContact = view.findViewById(R.id.contact_username_item_message);
        timeContact = view.findViewById(R.id.contact_time_item_message);

        if(chatMessage.getMessageUser().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())) {
            messageUser.setBackground(chatActivity.getResources().getDrawable(R.drawable.rounded_corner_user_message, null));
            messageContact.setText("");
            userContact.setText("");
            timeContact.setText("");

            messageUser.setText(chatMessage.getMessageText());
            userUser.setText(chatMessage.getMessageUser());
            timeUser.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", chatMessage.getMessageTime()));
        }
        else{
            messageUser.setBackgroundResource(0);
            messageUser.setText("");
            userUser.setText("");
            timeUser.setText("");

            messageContact.setText(chatMessage.getMessageText());
            userContact.setText(chatMessage.getMessageUser());
            timeContact.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", chatMessage.getMessageTime()));
        }
    }
}
