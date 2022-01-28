package Conversation;

import java.util.Date;
import java.text.*;
import java.io.Serializable;
import java.sql.Timestamp;

public class Message implements Comparable<Message>, Serializable {
    public User from;  // User écrivant le message
    public User to;    // User recevant le message
    public String msg; // Texte du message
    public Date date;  // Horodage
    public final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy"); // Format du message

    // Constructeur message
    public Message(User from, User to, String msg){
        this.from=from; this.to=to; this.msg=msg; 
        // TimeStamp
        this.date=new Date();
    }

    // Constructeur avec date
    public Message(User from, User to, String msg, String dateStr){
        this.from=from; this.to=to; this.msg=msg; this.date=convertDate(dateStr);
    }


    // Convertit la date à partir d'un string en une Date
    protected Date convertDate(String strDate){
        Date d = new Date();
        try{
            d = dateFormat.parse(strDate);
        }catch (ParseException e){
            System.out.println("[Message] Error parsing date");
        }
        return d;
    }

    // Comparer les messages par dates, permet de les trier dans l'historique
    @Override 
    public int compareTo(Message m){
        return this.date.compareTo(m.date);
    }

    // Affiche le message
    @Override
    public String toString(){
        return msg;
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
}
