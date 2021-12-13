package Database;

import java.sql.*;
/**
 * @author sqlitetutorial.net
 */
public class DatabaseManager{
    
    public void testdb(){
        try{  
            Class.forName("com.mysql.cj.jdbc.Driver"); // On utilise le JDBC
            
            // Connection à la base de donnée (id:root, pw:root) TODO: changer avec la base de donnée de l'insa
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306","root","root"); 

            Statement stmt=con.createStatement(); 
            // Requests : "use chatDB"
            // Requests : "select * from pseudoTab;"
            // Requests : "insert into pseudoTab (pseudo, id) values ('Martin', 60);"


            //ResultSet rs=stmt.executeQuery("use mysql");
            ResultSet rs=stmt.executeQuery("select user from mysql.user");
            while(rs.next())  
            System.out.println(rs.getString(1));  
            //rs=stmt.executeQuery("select pseudo from pseudoTable");  
            //while(rs.next())  
            //System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));  
            con.close();  
        }catch(Exception e){
            System.out.println(e);
        }  
    }   
    public Boolean exist_pseudo(String pseudo){return false;} //verifie si le pseudo est deja dans la base de données 

    public void change_pseudo(String pseudo){} //change le pseudo (appel à exist_pseudo)

    public int get_id(String pseudo){ return (0);} //retourne l'id associé à un pseudo

    public String get_pseudo(int id){return "v";} //retourne le pseudo associé à un id

    public void history(int id_user, int id_destinataire){} //affiche l'historique des messages échangés entre deux personnes

    public void connected(){} //renvoie la liste des users connectés

    public void change_status(int id){}//change le statut (connecté ou pas) de chaque id


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