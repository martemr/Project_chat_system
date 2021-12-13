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

    private static void arret(String message) {
        System.err.println(message);
        System.exit(99);
     }

    public DatabaseManager(){
        try{
        Class.forName("com.mysql.cj.jdbc.Driver");
        con=DriverManager.getConnection("jdbc:mysql://localhost:3306","root","root");  
    }catch(Exception e){
        System.out.println(e);
    }
}  

    public void update(String requete){// format general d'un update de la table
        try {
            Statement stmt = con.createStatement();
            int nbMaj = stmt.executeUpdate(requete);
         } catch (SQLException e) {
             e.printStackTrace();
         }
    }

    public void query(String requete){ //format general d'une query (pas toujours utilisable car on a besoin de la valeur de resultats)
        try {
            Statement stmt = con.createStatement();
            resultats = stmt.executeQuery(requete);
        }catch (SQLException e) {
            arret("Anomalie lors de l'execution de la requête");
         }
    }
        

    public Boolean exist_pseudo(String pseudo){return false;} //verifie si le pseudo est deja dans la base de données 

    public void change_pseudo(String pseudo, String new_pseudo){//change le pseudo (appel à exist_pseudo)
        String requete = "update pseudoTab set pseudo='"+new_pseudo+"' where pseudo='"+pseudo+"'";
        update(requete);
    } 

    public int get_id(User user){ //retourne l'id associé à un pseudo
        String requete = "select id from pseudoTab where pseudo='"+user.pseudo+"'";
        try {
            Statement stmt = con.createStatement();
            resultats = stmt.executeQuery(requete);
            return resultats.getInt(0);
        }catch (SQLException e) {
            arret("Anomalie lors de l'execution de la requête");
            return 0;
         }
    } 

    public String get_pseudo(User user){//retourne le pseudo associé à un id
        String requete = "select pseudo from pseudoTab where id='"+Integer.toString(user.id)+"'";
        try {
            Statement stmt = con.createStatement();
            resultats = stmt.executeQuery(requete);
            return resultats.getString(0);
        }catch (SQLException e) {
            arret("Anomalie lors de l'execution de la requête");
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
            arret("Anomalie lors de l'execution de la requête");
            return vec;
         }
    } 

    public void change_status_co(User user){//place le statut à l'état connecté
        String requete = "update pseudoTab set status='"+Integer.toString(1)+"' where id='"+Integer.toString(user.id)+"'";
        update(requete);
    }
    public void change_status_deco(User user){//place le statut à l'état déconnecté
        String requete = "update pseudoTab set status='"+Integer.toString(0)+"' where id='"+Integer.toString(user.id)+"'";
        update(requete);
    }

    public void history(int id_user, int id_destinataire){} //affiche l'historique des messages échangés entre deux personnes

    public void closeConnection(){
        try{
            con.close();
        } catch (Exception e){
            System.out.println(e);
        }
    }    



    //Connection conn;    
    
    
    /**
     * Connect to a sample database
     *
     * @param fileName the database file name
     */
    /*public void createNewDatabase(String fileName) {

        String url = "jdbc:sqlite:code/main/java/db/" + fileName;

        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

       */         
} 