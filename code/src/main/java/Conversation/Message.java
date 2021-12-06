package Conversation;

import java.time.*;
import GUI.User;

import java.util.Date;  

public class Message {
    public User from; // User écrivant le message
    public User to;   // User recevant le message
    protected String msg; // Texte du message
    protected LocalDateTime timestamp; // Horodage

    // Constructeur message
    public Message(User from, User to, String msg, Date date){
        this.from=from; this.to=to; this.msg=msg; this.timestamp = LocalDateTime.now();
    }
}
