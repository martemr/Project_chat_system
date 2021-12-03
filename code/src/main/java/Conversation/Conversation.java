package Conversation;

import GUI.User;

public class Conversation extends Thread {
    
    // Attributs
    User me;
    User other;

    public Conversation(User u1, User u2) {
        this.me = u1; this.other = u2;
    }

    public void send_message(String msg_txt) {
        //TODO Message msg = new Message(me, other, msg_txt);


    }

    //public run(){
    //}
}
