import java.io.IOException;


import GUI.*;
import Database.*;
import Network.*;
import Conversation.*;

public class Main {

    // Main fonction (appelée en premier lors de l'exécution)
    public static void main(String[] args) {
        //DatabaseManager db = new DatabaseManager();
        //db.testdb();
        //db.closeConnection();
        
        Interface mainWindow = new Interface();
        //User u = new User("Martin");
        //System.out.printf("User %s %d \n", u.pseudo, u.id);
        

        // TCP Server
        ServerTCP tcpServer;
        System.out.println("[Main] Running as server");
        int port = 1234;
        try {
            tcpServer = new ServerTCP(port); // Start a thread on given server, ready to wait for messages
            tcpServer.init();
            tcpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}