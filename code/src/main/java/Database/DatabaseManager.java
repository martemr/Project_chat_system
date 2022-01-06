package Database;

import java.sql.*;
import java.util.*;
import java.text.*;
import java.util.Date;


import Conversation.Message;


import GUI.User;

/**
 * @author sqlitetutorial.net
 */
public class DatabaseManager{

    Connection con;
    ResultSet resultats = null;


//HANDLERS

    /**
     * Fonction qui gère une erreur en affichant un message personnalisé
     * @param exc exception levé
     * @param message message à afficher à la suite
     */
    private static void handleError(SQLException exc, String message) {
        System.err.println("SQL Error " + exc.getSQLState() + " : " + exc.getMessage());
        System.err.println(message);
        System.exit(2);
    }

    /**
     * Fonction qui gère une erreur sans afficher de message personnalisé 
     * @param exc exception levé
     */
    private static void handleError(SQLException exc) {
        //exc.printStackTrace();
        System.err.println("SQL Error " + exc.getSQLState() + " : " + exc.getMessage());
        System.exit(2);
    }



//CONSTRUCTEUR

    /**
     * Constructeur de la base de données : connecte à la bonne base de données
     */
    public DatabaseManager(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("[Database] Connection...");
            con=DriverManager.getConnection("jdbc:mysql://srv-bdens.insa-toulouse.fr:3306","tp_servlet_007","EiJ0eoVo");  
            System.out.println("[Database] Connection established");
            String requete = "use tp_servlet_007";
            execute(requete);
        }catch(Exception e){
            System.out.println(e);
        }
    }  




//FONCTIONS GENERALES SUR LES REQUETES
    /**
     * Fonction qui permet de mettre à jour la table
     * @param requete requete à envoyer à la base de données
     * @return nombre de mise à jour dans la table effectuées
     */
    public int update(String requete){
        int nbMaj =0;
        try {
            Statement stmt = con.createStatement();
            nbMaj = stmt.executeUpdate(requete);
        } catch (SQLException e) {
            handleError(e);
        }
        return nbMaj;
    }


    /**
     * Fonction qui permet de faire une requete sur les valeurs de la table
     * @param requete requete à envoyer à la base de données
     * @return résultat de la requete sous la forme ResultSet
     */
    public ResultSet query(String requete){ 
        try{
            Statement stmt = con.createStatement();
            resultats = stmt.executeQuery(requete);
        }catch (SQLException e) {
            handleError(e);
        }
        return resultats;
    }

    /**
     * Fonction qui permet de faire une execution generale sur la table
     * @param requete requete à envoyer à la base de données
     * @return true si la requete à bien été exécutée
     */
    public Boolean execute(String requete){
        Boolean res = true;
        try {
        Statement stmt = con.createStatement();
        res = stmt.execute(requete);
        }catch (SQLException e) {
        handleError(e);
        }
        return res;
    }



//REQUETES SUR MSGTABLE

public void afficher_msgTab(){
    ResultSet table;
    String requete = "select * from msgTable";
    table=query(requete);
    try{
        System.out.println("[Database] Table msgTable :");
        System.out.println("-----------------------------");
        System.out.println("|  Emetteur  |  Destinataire   |               Message                   |               Date               |");
        System.out.println("-----------------------------");
        while (table.next()){
            System.out.printf("| %-8d   | %-5d           |    %s                        |  %s    |\n", table.getInt(1), table.getInt(2), table.getString(3), table.getString(4));
        }
        System.out.println("-----------------------------");
    }catch (SQLException e) {
        handleError(e);
    }
}



    
    /**
     * Affiche l'historique des messages entre 2 personnes, triés par date d'envoi
     * Format : date pseudo : message
     * @param emetteur
     * @param destinataire
     */
    public Queue<Message> history(User emetteur, User destinataire){
        Queue<Message> histo = new PriorityQueue<>(); 
        String requete = "select emetteur, destinataire, message, date from msgTable where ((emetteur='"+emetteur.id
        +"' and destinataire='"+ destinataire.id+"') or (emetteur='"+destinataire.id+"' and destinataire='"+emetteur.id
        +"')) order by date" ;
        try {
            Statement stmt = con.createStatement();
            resultats = stmt.executeQuery(requete);
            while(resultats.next()){
                if (resultats.getInt(1)==emetteur.id){
                    Message msg = new Message(emetteur, destinataire, resultats.getString(3), resultats.getString(4));
                    histo.add(msg);
                } else {
                    Message msg = new Message(destinataire, emetteur, resultats.getString(3), resultats.getString(4));
                    histo.add(msg);
                }
            }
        }catch (SQLException e) {
            handleError(e, "Anomalie lors de l'execution de la requête historique");         
        }
        return histo;
    }


    /**
     * Ajoute un nouveau message à la base de données
     * @param message
     */
    public void nouveau_message(Message message){
            String requete = "insert into msgTable values ('"+message.from.id+"', '"+message.to.id+"', '"+
            message.msg+"', '"+message.date+"')";
            update(requete);
    }
    

    //FERMETURE DE LA BASE DE DONNEES
    public void closeConnection(){
        try{
            con.close();
        } catch (Exception e){
            System.out.println(e);
        }
        System.out.println("[Database] Connection succesfully closed");        
    }    
    
    


    

    //TESTS

    User existe = new User("Martin", 60, User.Status.ABSENT);
    User inconnu = new User("cc", 79, User.Status.OCCUPIED);
    User jsp = new User("null", 33, User.Status.CONNECTED);
    //Message msg = new Message(inconnu, existe, "zheufv");
    public void testdb(){
       // nouveau_message(msg);
        afficher_msgTab();
        history(inconnu, existe);
    
    }

} 