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
    private static ServerUDP udpServer;

    public static Vector<User> connectedUsers;

    /* GETTERS */

    public static User getMainUser(){
        return user;
    }
    public static DatabaseManager getMainDatabase(){
        return database;
    }
    public static ServerUDP getServerUDP(){
        return udpServer;
    }


    /* METHODES */

    /**
     * Permet de récupérer une liste de pseudos à partir du tableau de users
     * @param users
     * @return
     */
    public static String[] get_pseudos(){
        String[] pseudos = new String[connectedUsers.size()];
        for(int i=0; i<connectedUsers.size();i++){
            pseudos[i]=connectedUsers.get(i).pseudo;
        }
        return pseudos;
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
    
    public static User getUserByPseudo(String pseudo){
        boolean trouve = false;
        int index = 0;
        while(!trouve && index<connectedUsers.size()){
            if(connectedUsers.get(index).pseudo.equals(pseudo)){
                trouve=true;
                break;
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
                break;
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



    // USER ARRAY ACTIONS
    
    static public void addNewUser(User new_user){
        connectedUsers.add(new_user);
        if (mainWindow != null)
            mainWindow.addUserToList(new_user.pseudo);
    }

    static public void removeUser(User old_user){
        connectedUsers.remove(old_user);
        if (mainWindow != null)
            mainWindow.removeUserFromList(old_user.pseudo);
    }

    static public void clearListUser(){
        connectedUsers.clear();;
        //if (mainWindow != null)
        //    mainWindow.removeUserFromList(old_user.pseudo);
    }
    
    
    static public void changePseudoUser(User new_user){
        removeUser(getUserByIP(new_user.IPAddress));
        addNewUser(new_user);
    }

    /** Search if the id is already in the array of connected users */
    static public boolean isNew(User new_user){
        for (Double id : Main.get_id(connectedUsers)){
            if (id==new_user.id){
                return false;
            }
        }
        return true;
    }
    



    /* STATIC PART */

    public static void closeSystem(){
        System.out.println("[Main] closing system");
        user.setFlag(User.Flag.DISCONNECTION);
        udpServer.sendBroadcast();  // Send disconnection message
        udpServer.closeServer();    // Close udp server 
        database.closeConnection(); // Close connection to database
        System.out.println("[Main] Godd bye !");
    }

    static {
        user = new User("Pseudo"); // Main user is declared once, here.
        Tools.lire_config_xml();   // Récupère les adresses IPs
        connectedUsers = new Vector<User>();
        database = new DatabaseManager(); // Démarre la base de données
    }

    // Main fonction (appelée lors de l'exécution)
    public static void main(String[] args) {     
      
        // Lance le server udp pour attendre les broadcasts
        System.out.println("[Main] Starting server UDP ");
        try{
            udpServer = new ServerUDP();
            udpServer.start();
        } catch (IOException e) {
            System.out.println("[Main] Error while starting UDP");
        }

        // Ouvre l'interface
        System.out.println("[Main] Starting interface");
        mainWindow = new Interface();

    }

}