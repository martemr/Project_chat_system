package Main;

import java.io.IOException;
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

    static public Vector<User> connectedUsers;
    static public String[] connectedPseudos;
    static public double[] connectedId;

    /* GETTERS */

    static public User getMainUser(){
        return user;
    }
    static public DatabaseManager getMainDatabase(){
        return database;
    }

    static public ClientTCP getClientTCP(){
        return tcpClient;
    }

    static public ServerTCP getServerTCP(){
        return tcpServer;
    }    
    
    static public ServerUDP getServerUDP(){
        return udpServer;
    }

    static public ClientUDP getClientUDP(){
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
        try {
            tcpServer = new ServerTCP(port); // Start a thread on given server, ready to wait for messages
            tcpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initTCPClient(String host, int port){
        tcpClient = new ClientTCP(host, port);
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

    static public User getUserByPseudo(String pseudo){
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

    /**
     * Permet de récupérer une liste de pseudos à partir du tableau de users
     * @param users
     * @return
     */
    static public double[] get_id(Vector<User> users){
        double[] id = new double[users.size()];
        for(int i=0; i<users.size();i++){
            id[i]=users.get(i).id;
        }
        return id;
    }

    // USER ARRAY ACTIONS

    static public void updateArrayConnectedUsers(){
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




    /* STATIC PART */

    public static void closeSystem(){
        user.setFlag(User.Flag.DISCONNECTION);
        udpClient.sendBroadcast();
        udpServer.closeServer();
        database.closeConnection();
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

        System.out.println("[Main] Starting server TCP");
        startTCPServer(3070);

    }

}