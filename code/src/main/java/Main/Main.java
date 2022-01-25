package Main;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Vector;

import GUI.*;
import Database.*;
import Network.*;
import Conversation.*;

public class Main {

    private static User user; // Main user, static for getting it everywhere
    public static Interface mainWindow;
    static DatabaseManager database;
    static ServerTCP tcpServer;
    public static ClientTCP tcpClient; 
    static ServerUDP udpServer;
    static ClientUDP udpClient;

    public static Vector<User> connectedUsers;
    public static String[] connectedPseudos;
    public static double[] connectedId;

    /* GETTERS */

    public static User getMainUser(){
        return user;
    }
    public static DatabaseManager getMainDatabase(){
        return database;
    }

    public static ClientTCP getClientTCP(){
        return tcpClient;
    }

    public static ServerTCP getServerTCP(){
        return tcpServer;
    }    
    
    public static ServerUDP getServerUDP(){
        return udpServer;
    }

    public static ClientUDP getClientUDP(){
        return udpClient;
    }


    /* METHODES */

    public static void startUDPServer(){
        try{
            udpServer = new ServerUDP();
            udpServer.start();
        } catch (IOException e) {
            System.out.println("[Main] Error while starting UDP");
        }
    }

    public void closeServerTCP(){
        udpServer.closeServer();
        udpServer.currentThread().yield();
    }

    public static void startTCPServer(int port){
        System.out.println("[Main] Starting server TCP");
        try {
            tcpServer = new ServerTCP(port); // Start a thread on given server, ready to wait for messages
            tcpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startTCPClient(String host, int port){
        System.out.println("[Main] Starting client TCP");
        tcpClient = new ClientTCP(host, port);
        tcpClient.start();
    }

    /**
     * Permet de récupérer une liste de pseudos à partir du tableau de users
     * @param users
     * @return
     */
    public static String[] get_pseudo(Vector<User> users){
        String[] pseudos = new String[users.size()];
        for(int i=0; i<users.size();i++){
            pseudos[i]=users.get(i).pseudo;
        }
        return pseudos;
    }

    public static User getUserByPseudo(String pseudo){
        boolean trouve = false;
        int index = 0;
        while(!trouve && index<connectedUsers.size()){
            if(connectedUsers.get(index).pseudo.equals(pseudo)){
                trouve=true;
            } else {
                index+=1;
            }
        }
        if(trouve){
            return connectedUsers.get(index);
        } else {
            return null;
        }
    }

    static public User getUserByIP(InetAddress IP){
        boolean trouve = false;
        int index = 0;
        while(!trouve && index<connectedUsers.size()){
            if(connectedUsers.get(index).IPAddress.equals(IP)){
                trouve=true;
            } else {
                index+=1;
            }
        }
        if(trouve){
            return connectedUsers.get(index);
        } else {
            return null;
        }
    }

    /**
     * Permet de récupérer une liste de pseudos à partir du tableau de users
     * @param users
     * @return
     */
    public static double[] get_id(Vector<User> users){
        double[] id = new double[users.size()];
        for(int i=0; i<users.size();i++){
            id[i]=users.get(i).id;
        }
        return id;
    }

    // USER ARRAY ACTIONS

    public static void updateArrayConnectedUsers(){
        connectedPseudos=get_pseudo(connectedUsers);     
        connectedId=get_id(connectedUsers);
    }    
    
    static public void addNewUser(User new_user){
        connectedUsers.add(new_user);
        updateArrayConnectedUsers();
        if (mainWindow != null)
            mainWindow.updateConnectedUserList(new_user);
    }
    
    static public void changePseudoUser(User new_user){
        mainWindow.removeUserFromList(new_user);
        connectedUsers.remove(new_user);
        addNewUser(new_user);
    }

    static public boolean isNew(User new_user){
        for (int i = 0; i <Main.connectedId.length; i++){
            if (Main.connectedId[i]==new_user.id){
                return false;
            }
        }
        return true;
    }


    public void messageReceived(Message msg){
        this.mainWindow.printMessage(msg);
    }

    static public void sendMessage(Message msg){
        if (tcpClient!=null){
            tcpClient.sendMessage(msg);
        } else if (tcpServer!=null){
            tcpServer.sendTCPMsg(msg);
        }
    }



    /* STATIC PART */

    public static void closeSystem(){
        System.out.println("[Main] closing system");
        user.setFlag(User.Flag.DISCONNECTION);
        udpClient.sendBroadcast();  // Send disconnection message
        udpServer.closeServer();    // Close udp server 
        database.closeConnection(); // Close connection to database
        System.out.println("[Main] Godd bye !");
    }

    static {
        user = new User("Pseudo"); // Main user is declared once, here.
        Tools.lire_config_xml();   // Récupère les adresses IPs
        connectedUsers = new Vector<User>();
        updateArrayConnectedUsers();
        database = new DatabaseManager(); // Démarre la base de données
    }

    // Main fonction (appelée lors de l'exécution)
    public static void main(String[] args) {     
      
        // Lance le server udp pour attendre les broadcasts
        System.out.println("[Main] Creating client UDP ");
        try{
            udpClient = new ClientUDP();
        } catch (Exception e){
            e.printStackTrace();
        }

        // Ouvre l'interface
        System.out.println("[Main] Starting interface");
        mainWindow = new Interface();

        System.out.println("[Main] Starting server UDP ");
        startUDPServer();

        //System.out.println("[Main] Starting server TCP");
        //startTCPServer(3070);

    }

}