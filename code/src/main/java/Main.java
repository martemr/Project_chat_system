import GUI.*;
import Database.*;

public class Main {

    // Main fonction (appelée en premier lors de l'exécution)
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        db.testdb();
        db.closeConnection();
        
        //Interface mainWindow = new Interface();
        //User u = new User("Martin");
        //System.out.printf("User %s %d \n", u.pseudo, u.id);
    }

}
