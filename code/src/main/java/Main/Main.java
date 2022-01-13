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
    public static ClientTCP tcpClient; // TODO : Link with interface
    static ServerUDP udpServer;
    static ClientUDP udpClient;
    static final int TCPPort=1234;

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
        mainWindow.disconnectUserFromList(new_user);
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



    static private boolean contains(Object[] array, Object element){
        for (int i = 0; i <array.length; i++){
            if (array[i].equals(element)){
                return true;
            }
        }
        return false;
    }


    /* STATIC PART */

    public static void closeSystem(){
        user.setFlag(User.Flag.DISCONNECTION);
        udpClient.sendBroadcast();
        //database.closeConnection();
        udpServer.closeServer();
    }

    static {
        user = new User("Pseudo"); // Main user is declared once, here.
        Tools.lire_config_xml();   // Récupère les adresses IPs
        connectedUsers = new Vector<User>();
        updateArrayConnectedUsers();
        //database = new DatabaseManager(); // Démarre la base de données
    }

    // Main fonction (appelée lors de l'exécution)
    public static void main(String[] args) {     
      
        // Lance le serveur UDP (port 1234)


        System.out.println("[Main] Creating client UDP ");
        try{
            udpClient = new ClientUDP();
        } catch (Exception e){
            e.printStackTrace();
        }

        //User Martin = new User("Martin", 60, User.Flag.CONNECTED);
        //Martin.set_ip_local("10.1.5.234");
        //Martin.set_ip_broadcast("10.1.255.255");
        //connectedUsers.add(Martin);
        //updateArrayConnectedUsers();



        // Ouvre l'interface
        System.out.println("[Main] Starting interface");
        mainWindow = new Interface();

        System.out.println("[Main] Starting server UDP ");
        startUDPServer();

        System.out.println("[Main] Starting server TCP");
        startTCPServer();


/*  
        
        System.out.println("[Main] Starting client UDP ");
        try{
            udp_client=new ClientUDP();
            //udp_client.sendBroadcast();
        } catch (Exception e){
            e.printStackTrace();
        }
        
        /*
        User Martin = new User("Martin", 60, User.Flag.CONNECTED);
        User Paul   = new User("Paul",   79, User.Flag.CONNECTED);
        User Marie  = new User("Marie",  33, User.Flag.CONNECTED);
        connectedUsers.add(Martin);
        connectedUsers.add(Paul);
        connectedUsers.add(Marie);
*/
        
         

    }

}