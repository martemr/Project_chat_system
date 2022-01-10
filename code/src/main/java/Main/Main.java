package Main;

import java.io.IOException;
import java.util.Vector;

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
    static ServerUDP udp_server;
    static final int UDPPort=1234;
    static final int TCPPort=4321;

    static public Vector<User> connectedUsers;
    static public String[] connectedPseudos;
    static public int[] connectedId;

    static {
        user = new User("Pseudo"); // the main user is declared once, here.
        connectedUsers = new Vector<User>();
        updateConnectedUsers();
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

    static public ServerUDP getServerUDP(){
        return udp_server;
    }

    public static void startUDPServer(){
        try{
            ServerUDP udp_server = new ServerUDP(UDPPort);
            udp_server.start();
            ClientUDP udp_client=new ClientUDP();
            udp_client.sendBroadcast();
        } catch (IOException e) {
            System.out.println("[Main] Error while starting UDP");
        }
    }

    public static void startTCPServer(){
        try {
            tcpServer = new ServerTCP(TCPPort); // Start a thread on given server, ready to wait for messages
            tcpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet de récupérer une liste de pseudos à partir du tableau de users
     * @param users
     * @return
     */
    static public String[] get_pseudo(Vector<User> users){
        String[] pseudos = new String[users.size()];
        for(int i=0; i<users.size();i++){
            pseudos[i]=users.get(i).pseudo;
        }
        return pseudos;
    }

    /**
     * Permet de récupérer une liste de pseudos à partir du tableau de users
     * @param users
     * @return
     */
    static public int[] get_id(Vector<User> users){
        int[] id = new int[users.size()];
        for(int i=0; i<users.size();i++){
            id[i]=users.get(i).id;
        }
        return id;
    }

    static public void updateConnectedUsers(){
        connectedPseudos=get_pseudo(connectedUsers);
        connectedId=get_id(connectedUsers);
    }

    // Main fonction (appelée en premier lors de l'exécution)
    public static void main(String[] args) {     

        //database.testdb();
        
        // Démarre le serveur UDP sur le port 1234
        
        /*
        User Martin = new User("Martin", 60, User.Flag.CONNECTED);
        User Paul   = new User("Paul",   79, User.Flag.CONNECTED);
        User Marie  = new User("Marie",  33, User.Flag.CONNECTED);
        connectedUsers.add(Martin);
        connectedUsers.add(Paul);
        connectedUsers.add(Marie);*/

        System.out.println("[Main] Starting interface");
        mainWindow = new Interface();   

        System.out.println("[Main] Starting server UDP ");
        startUDPServer();

    }

    public void messageReceived(Message msg){
        this.mainWindow.printMessage(msg);
    }

    public static void closeSystem(){
        //database.closeConnection();
    }
}