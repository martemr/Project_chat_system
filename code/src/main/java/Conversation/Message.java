package Conversation;

import java.time.*;
import GUI.User;
import java.io.Serializable;
import java.lang.Thread; 

import java.util.Date;  

public abstract class Message extends Thread implements Serializable{
    public User from; // User écrivant le message
    public User to;   // User recevant le message
    protected String msg; // Texte du message
    protected LocalDateTime timestamp; // Horodage

    // Constructeur message
    public Message(User from, User to, String msg, Date date){
        this.from=from; this.to=to; this.msg=msg; this.timestamp = LocalDateTime.now();
    }

	abstract public void run();
}
