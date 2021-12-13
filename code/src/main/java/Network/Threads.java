package Network;

import Conversation.Message;
import GUI.Interface;

public class Threads extends Thread {

    ClientTCP client;

    public Threads(ClientTCP client) {
        this.client=client;
    }

    public void sendMessageTo(Interface inter, String host, Message message) throws Exception {
        Thread t = new Thread(new ClientTCP(host, message));
        t.start();
    };
}
