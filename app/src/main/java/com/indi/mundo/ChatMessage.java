package com.indi.mundo;

import java.util.Date;

/**
 * Created by david.arteaga on 17/8/2017.
 */

public class ChatMessage {
    private String messageText, messageUser;
    private long messageTime;

    public ChatMessage(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageTime = new Date().getTime();
    }

    public ChatMessage() {
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
