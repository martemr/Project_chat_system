package Main;

import java.io.IOException;

import GUI.*;
import Database.*;
import Network.*;
import Conversation.*;

public class Main {

    private static User user; // Main user, static for getting it everywhere
    static Interface mainWindow;

    static {
        user = new User("Pseudo"); // the main user is declared once, here.
    }

    /** Getter for the user
     * @return main user
     */
    static public User getMainUser(){
        return user;
    }

    // Main fonction (appelée en premier lors de l'exécution)
    public static void main(String[] args) {     

        //DatabaseManager db = new DatabaseManager();
        //db.testdb();
        //db.closeConnection();
        
        System.out.println("[Main] Starting interface");
        mainWindow = new Interface();   

        // TCP Server
        ServerTCP tcpServer;
        System.out.println("[Main] Running as server");
        int port = 1234;
        try {
            tcpServer = new ServerTCP(port); // Start a thread on given server, ready to wait for messages
            tcpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public void messageReceived(Message msg){
        this.mainWindow.printMessage(msg);
    }
}