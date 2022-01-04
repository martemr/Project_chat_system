package Main;

import java.io.IOException;

import GUI.*;
import Database.*;
import Network.*;
import Conversation.*;

public class Main {

    private static User user; // Main user, static for getting it everywhere
    static Interface mainWindow;
    static DatabaseManager database;
    static ServerTCP tcpServer;
    static ClientTCP tcpClient; // TODO : Link with interface

    static {
        //user = new User("Pseudo"); // the main user is declared once, here.
        //database = new DatabaseManager();
    }

    static public User getMainUser(){
        return user;
    }
    static public DatabaseManager getMainDatabase(){
        return database;
    }

    /** Getter for the client TCP
     * @return clientTCP
     */
    static public ClientTCP getClientTCP(){
        return tcpClient;
    }

    // Main fonction (appelée en premier lors de l'exécution)
    public static void main(String[] args) {     

        //database.testdb();
        
        //System.out.println("[Main] Starting interface");
        //mainWindow = new Interface();   
        
        // TCP Server

        //System.out.println("[Main] Running the server");
        //int port = 1234;
        //try {
        //    tcpServer = new ServerTCP(port); // Start a thread on given server, ready to wait for messages
        //    tcpServer.start();
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}


        // UDP  
        try{
            ClientUDP udp = new ClientUDP();
        } catch (IOException e) {
            System.out.println("Error in udp");
        }
        
    }

    public void messageReceived(Message msg){
        this.mainWindow.printMessage(msg);
    }

    public static void closeSystem(){
        database.closeConnection();
    }
}