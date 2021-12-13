package Database;

import java.sql.*;
import GUI.User;
/**
 * @author sqlitetutorial.net
 */
public class DatabaseManager{

    Connection con;
    ResultSet resultats = null;

    public DatabaseManager(){
        try{
        Class.forName("com.mysql.cj.jdbc.Driver");
        con=DriverManager.getConnection("jdbc:mysql://localhost:3306","root","root");  
    }catch(Exception e){
        System.out.println(e);
    }
}  

    public void update(String requete){
        try {
            Statement stmt = con.createStatement();
            int nbMaj = stmt.executeUpdate(requete);
         } catch (SQLException e) {
             e.printStackTrace();
         }
    }
    public void query(String requete){
        try {
            Statement stmt = con.createStatement();
            resultats = stmt.executeQuery(requete);
        }catch (SQLException e) {
            arret("Anomalie lors de l'execution de la requête");
         }
    }
        

    public Boolean exist_pseudo(String pseudo){return false;} //verifie si le pseudo est deja dans la base de données 

    public void change_pseudo(String pseudo, String new_pseudo){//change le pseudo (appel à exist_pseudo)
        String requete = "update pseudoTab set pseudo='"+new_pseudo"+"' where pseudo='+pseudo+"'";
        update(requete);
    } 

    public int get_id(String pseudo){ //retourne l'id associé à un pseudo
        String requete = "select id from pseudoTab where pseudo='"+pseudo+"'";
        query(requete);
    } 

    public String get_pseudo(int id){//retourne le pseudo associé à un id
        String requete = "select pseudo from pseudoTab where id='"+Integer.toString(id)+"'";
        query(requete);
    } 

    public void connected(){} //renvoie la liste des users connectés

    public void change_status_co(User user){//place le statut à l'état connecté
        String requete = "update pseudoTab set status='"+Integer.toString(1)+"' where id='"+Integer.toString(user.id)+"'";
        update(requete);
    }
    public void change_status_deco(User user){//place le statut à l'état déconnecté
        String requete = "update pseudoTab set status='"+Integer.toString(0)+"' where id='"+Integer.toString(user.id)+"'";
        update(requete);
    }

    public void history(int id_user, int id_destinataire){} //affiche l'historique des messages échangés entre deux personnes

    con.close();    



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

    public void closeConnection(){ // TODO : get connection
        try {
                if (this.conn != null) {
                    this.conn.close();
                    System.out.println("Connection to SQLite has been close.");
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
    }   */         
} 