package Conversation;

import GUI.User;

import java.util.Date;  
import java.sql.Timestamp;

public class Message {
    public User from;  // User écrivant le message
    public User to;    // User recevant le message
    public String msg; // Texte du message
    public Date date;  // Horodage

    // Constructeur message
    public Message(User from, User to, String msg){
        this.from=from; this.to=to; this.msg=msg; 
        // TimeStamp
        Timestamp ts=new Timestamp(System.currentTimeMillis());  
        this.date=new Date(ts.getTime());
    }

    /*
    // TODO : Remove this constructeur, use for test
    public Message(String msg){
        User u= new User("martin");
        this.from=u; this.to=u; this.msg=msg;
        // TimeStamp
        Timestamp ts=new Timestamp(System.currentTimeMillis());  
        this.date=new Date(ts.getTime());
    }
    */

    @Override
    public String toString(){
        return msg;
    }
}
