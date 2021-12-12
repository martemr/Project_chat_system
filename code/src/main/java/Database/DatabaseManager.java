package Database;

import java.sql.*;
/**
 * @author sqlitetutorial.net
 */
public class DatabaseManager{
    
    public void testdb(){
        try{  
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Connection à la base de donnée (id:root, pw:root)
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306","root","root"); 

            Statement stmt=con.createStatement();  
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