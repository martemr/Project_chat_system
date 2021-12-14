package Database;

import java.sql.*;
import java.util.List;
import java.util.Vector;

import GUI.User;

/* 
TODO :
- Faire une fonction style 'requete.toString()' pour afficher les resultats des requètes
- Tester toutes les procédures 1 par 1
- Mettre une ligne de commentaire avant chaque fonction qui l'explique

*/

/**
 * @author sqlitetutorial.net
 */
public class DatabaseManager{

    Connection con;
    ResultSet resultats = null;

    private static void handleError(SQLException exc, String message) {
        System.err.println(message);
        System.err.println("");
        System.exit(2);
    }

    private static void handleError(SQLException exc) {
        //exc.printStackTrace();
        System.err.println("SQL Error " + exc.getSQLState() + " : " + exc.getMessage());
        System.exit(2);
    }

    public DatabaseManager(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306","root","root");  
            System.out.println("[Database] Connection established");
            String requete = "use chatDB";
            execute(requete);
        }catch(Exception e){
            System.out.println(e);
        }
    }  

    public int update(String requete){// format general d'un update de la table
        int nbMaj =0;
        try {
            Statement stmt = con.createStatement();
            nbMaj = stmt.executeUpdate(requete);
        } catch (SQLException e) {
            handleError(e);
        }
        return nbMaj;
    }

    public ResultSet query(String requete){ //format general d'une query (pas toujours utilisable car on a besoin de la valeur de resultats)
        try {
            Statement stmt = con.createStatement();
            resultats = stmt.executeQuery(requete);
        }catch (SQLException e) {
            handleError(e);
        }
        return resultats;
    }

    public Boolean execute(String requete){ //format general d'une execution
    Boolean res = true;
        try {
        Statement stmt = con.createStatement();
        res = stmt.execute(requete);
    }catch (SQLException e) {
        handleError(e);
    }
    return res;
}

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


   /* public Boolean exist_pseudo(String pseudo){return false;} //verifie si le pseudo est deja dans la base de données */

    public void change_pseudo(User user, String new_pseudo){//change le pseudo (appel à exist_pseudo)
        String requete = "update pseudoTab set pseudo='"+new_pseudo+"' where pseudo='"+user.pseudo+"'";
        update(requete);
    } 

    /** Retourne l'id associé à un pseudo */
    public int get_id(User user){ 
        String requete = "select id from pseudoTab where pseudo='"+user.pseudo+"'";
        try {
            Statement stmt = con.createStatement();
            resultats = stmt.executeQuery(requete);
            return resultats.getInt(1);
        }catch (SQLException e) {
            handleError(e,"Anomalie lors de l'execution de la requête get_id");
            return 0;
        }
    } 
/*
    public String get_pseudo(User user){//retourne le pseudo associé à un id
        String requete = "select pseudo from pseudoTab where id='"+Integer.toString(user.id)+"'";
        try {
            Statement stmt = con.createStatement();
            resultats = stmt.executeQuery(requete);
            return resultats.getString(0);
        }catch (SQLException e) {
            //handleError("Anomalie lors de l'execution de la requête get_pseudo");
            return "";
         }
    } 

    public List<Integer> connected(){ //renvoie la liste des users connectés
        String requete = "select pseudo from pseudoTab where status='"+Integer.toString(1)+"'";
        List<Integer> vec = new Vector<>();
        try {
            Statement stmt = con.createStatement();
            resultats = stmt.executeQuery(requete);
            while(resultats.next()){
                vec.add(resultats.getInt(0));
            }
            return vec;
        }catch (SQLException e) {
            //handleError("Anomalie lors de l'execution de la requête connected");
            return vec;
         }
    } 

    public void change_status_co(User user){//place le statut à l'état connecté
        String requete = "update pseudoTab set status='"+Integer.toString(1)+"' where id='"+Integer.toString(user.id)+"'";
        update(requete);
        user.status=User.Status.CONNECTED;
    }
    public void change_status_deco(User user){//place le statut à l'état déconnecté
        String requete = "update pseudoTab set status='"+Integer.toString(0)+"' where id='"+Integer.toString(user.id)+"'";
        update(requete);
        user.status=User.Status.ABSENT;
    }

    public void history(int id_user, int id_destinataire){} //affiche l'historique des messages échangés entre deux personnes
*/
    public void closeConnection(){
        try{
            con.close();
        } catch (Exception e){
            System.out.println(e);
        }
    }    
    
      
    User existe = new User("Martin", 60, User.Status.ABSENT);
    User inconnu = new User("bb", 89, User.Status.OCCUPIED);
    public void testdb(){
        afficher_pseudoTab();
        int x=get_id(existe);
        int y=get_id(inconnu);
        System.out.println(Integer.toString(x));
        System.out.println(Integer.toString(y));
    }

} 