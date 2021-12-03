package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author sqlitetutorial.net
 */
public class DatabaseManager{
    
    Connection conn;

    /**
    * Connect to a sample database
    */
    public void connect() {
        conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:database.db"; // TODO : donner le bon dossier où stocker la base de données
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Connect to a sample database
     *
     * @param fileName the database file name
     */
    public void createNewDatabase(String fileName) {

        String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                //DatabaseMetaData meta = conn.getMetaData(); //TODO
                //System.out.println("The driver name is " + meta.getDriverName());
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
    }
}