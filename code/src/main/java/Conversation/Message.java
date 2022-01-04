package Conversation;

import GUI.User;

import java.util.Date;
import java.text.*;
import java.io.Serializable;
import java.sql.Timestamp;

public class Message implements Serializable {
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

    // Constructeur avec date
    public Message(User from, User to, String msg, String dateStr){
        this.from=from; this.to=to; this.msg=msg; 
        //this.date=new Date(dateStr));
        SimpleDateFormat df=new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        try{
            this.date = df.parse(dateStr);
        }catch (ParseException e){
            System.out.println("Error parsing date");
            System.exit(1);
        } 
    }

    // TODO : Remove this constructeur, use for test
    @Deprecated
    public Message(String msg){
        User u= new User("martin");
        this.from=u; this.to=u; this.msg=msg;
        // TimeStamp
        Timestamp ts=new Timestamp(System.currentTimeMillis());  
        this.date=new Date(ts.getTime());
    }
    
    @Override
    public String toString(){
        return msg;
    }
}
