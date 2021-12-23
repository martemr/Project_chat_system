package Database;

import java.sql.*;
import java.util.List;
import java.util.Vector;

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
 * @param exc
 * @param message
 */
    private static void handleError(SQLException exc, String message) {
        System.err.println("SQL Error " + exc.getSQLState() + " : " + exc.getMessage());
        System.err.println(message);
        System.exit(2);
    }
/**
 * Fonction qui gère une erreur sans afficher de message personnalisé 
 * @param exc
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
 * @param requete
 * @return
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
     * @param requete
     * @return
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
     * @param requete
     * @return
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








//REQUETES SUR PSEUDOTAB

/**
 * Permet d'afficher la table pour verifier nos autres requetes
 */
    public void afficher_pseudoTab(){
        ResultSet table;
        String requete = "select * from pseudoTab";
        table=query(requete);
        try{
            System.out.println("[Database] Table pseudoTab :");
            System.out.println("-----------------------------");
            System.out.println("|  Pseudo  |  id   | status |");
            System.out.println("-----------------------------");
            while (table.next()){
                System.out.printf("| %-8s | %-5d |    %d   |\n", table.getString(1), table.getInt(2), table.getInt(3));
            }
            System.out.println("-----------------------------");
        }catch (SQLException e) {
            handleError(e);
        }
    }

    /**
     * Verifie l'existence d'un pseudo dans la base de données
     * @param pseudo
     * @return
     */
    public Boolean exist_pseudo(String pseudo){
        ResultSet username;
        String requete = "select pseudo from pseudoTab where pseudo='"+pseudo+"'";
        username=query(requete);
        try{
            username.next(); // Saute la ligne de titre
            return username.getString(1).equals(pseudo);
        }catch (SQLException e) {
            if (e.getSQLState().equals("S1000")){
                return false;
            } else {
                handleError(e, "Erreur exist_pseudo");
            }
            return false;
        }  
    }



    /**
     * Verifie l'existence d'un id dans la base de données
     * @param id
     * @return
     */
    public Boolean exist_id(int id){
        ResultSet num;
        String requete = "select id from pseudoTab where id='"+id+"'";
        num=query(requete);
        try{
            num.next();
            return true;
        }catch (SQLException e) {
            if (e.getSQLState().equals("S1000")){
                return false;
            } else {
                handleError(e, "Erreur exist_id");
            }
            return false;
        }
    }
   

    /**
     * Permet d'ajouter un utilisateur dans la base de données
     * @param user
     */
    public void add_user(User user){
        if (exist_pseudo(user.pseudo)) {
            System.out.println("Ce pseudo est déjà utilisé");
        } else {
            int status;
            if (user.status==User.Status.CONNECTED) {
                status=1;
            } else if (user.status==User.Status.ABSENT){
                status=0;
            } else {
                status=2;
            }
            String requete = "insert into pseudoTab values ('"+user.pseudo+"', '"+user.id+"', '"+status+"')";
            update(requete);
        }
    };


    /**
     * Permet de changer le pseudo d'un utilisateur
     * @param user
     * @param new_pseudo
     */
    public void change_pseudo(User user, String new_pseudo){
        if (exist_pseudo(new_pseudo)){
            System.out.println("Ce pseudo est déjà utilisé");
        } else if(new_pseudo=="") {
            System.out.println("Veuillez entrer un pseudo");
        } else {
            String requete = "update pseudoTab set pseudo='"+new_pseudo+"' where pseudo='"+user.pseudo+"'";
            update(requete);
            System.out.println("Pseudo modifié");
        }
    } 

    /**
     * Reconnexion d'un utilisateur : remet status connecté et met un nouveau pseudo
     * @param user
     */
    public void reconnexion(User user){
        String requete = "update pseudoTab set status='"+Integer.toString(1)+"' where id='"+Integer.toString(user.id)+"'";
        update(requete);
        user.status=User.Status.CONNECTED;
        change_pseudo(user, user.pseudo);
    }



    /**
     * Fonction utilisée lors de l'appui sur le bouton du pseudo
     * Lors de la connexion, vérifie si l'utilisateur est déjà dans la base de données (via id) et agit en fonction
     * @param user
     */
    public void connexion(User user){
        if (exist_id(user.id)){
            reconnexion(user);
        }else{
            add_user(user);
        }
    }


       /**
     * Deconnexion d'un utilisateur : status déconnecté et suppression du pseudo
     * @param user
     */
    public void deconnexion(User user){
        String requete = "update pseudoTab set status='"+Integer.toString(0)+"' where id='"+Integer.toString(user.id)+"'";
        update(requete);
        user.status=User.Status.ABSENT;
        change_pseudo(user, "");
    }




    /**
     * Retourne l'id associé à un pseudo
     * @param user
     * @return
     */
    public int get_id(User user){
        ResultSet id;
        String requete = "select id from pseudoTab where pseudo='"+user.pseudo+"'";
        id=query(requete);
        try{
            id.next(); // Saute la ligne de titre
            return id.getInt(1);
        }catch (SQLException e) {
            if (e.getSQLState().equals("S1000")){
                handleError(e, "L'utilisateur n'est pas dans la base de données");
            } else {
                handleError(e, "Erreur get_id");
            }
            return -1;
        }  
    } 

    
    /**
     * Retourne le pseudo associé à un id
     * @param user
     * @return
     */
    public String get_pseudo(User user){
        ResultSet pseudo;
        String requete = "select pseudo from pseudoTab where id='"+Integer.toString(user.id)+"'";
        pseudo=query(requete);
        try{
            pseudo.next(); // Saute la ligne de titre
            return pseudo.getString(1);
        }catch (SQLException e) {
            if (e.getSQLState().equals("S1000")){
                handleError(e, "L'utilisateur n'est pas dans la base de données");
            } else {
                handleError(e, "Erreur get_id");
            }
            return "";
        }
    } 


    /**
     * Retourne la liste des utilisateurs connectés
     * @return
     */
    public void connected_users(){
        String requete = "select pseudo from pseudoTab where status='"+1+"'";
        try {
            Statement stmt = con.createStatement();
            resultats = stmt.executeQuery(requete);
            while(resultats.next()){
                System.out.println(resultats.getString(1));
            }
        }catch (SQLException e) {
            handleError(e, "Anomalie lors de l'execution de la requête connected_users");         
         }
    }







    //REQUETES SUR MSGTABLE
    
    //TODO : public void history(int id_user, int id_destinataire){} //affiche l'historique des messages échangés entre deux personnes


    //FERMETURE DE LA BASE DE DONNEES
    public void closeConnection(){
        try{
            con.close();
        } catch (Exception e){
            System.out.println(e);
        }
    }    
    
    


    

    //TESTS

    User existe = new User("Martin", 60, User.Status.ABSENT);
    User inconnu = new User("bb", 89, User.Status.OCCUPIED);
    User jsp = new User("null", 33, User.Status.CONNECTED);
    public void testdb(){
        afficher_pseudoTab();
        add_user(jsp);
        afficher_pseudoTab();
    }

} 