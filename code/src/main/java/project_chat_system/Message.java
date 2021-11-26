package project_chat_system;

import java.time.*;

public class Message {
    public User from; // User écrivant le message
    public User to;   // User recevant le message
    protected String msg; // Texte du message
    protected LocalDateTime timestamp; // Horodage

    // Constructeur message
    Message(User from, User to, String msg){
        this.from=from; this.to=to; this.msg=msg; this.timestamp = LocalDateTime.now();
    }
}
