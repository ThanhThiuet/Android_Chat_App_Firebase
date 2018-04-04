package studymyselft.nguyenthanhthi.chatapp;

import java.util.Date;

/**
 * Created by thanhthi on 05/04/2018.
 */

public class ChatMessage {

    private String messageUser;
    private String messageText;
    private long messageTime;

    public ChatMessage(String messageUser, String messageText) {
        this.messageUser = messageUser;
        this.messageText = messageText;
        // initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessage() {
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
