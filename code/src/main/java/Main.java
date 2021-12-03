import GUI.*;
import Database.*;



public class Main {

    // Main fonction (appelée en premier lors de l'exécution)
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        db.connect();

        Interface mainWindow = new Interface(db);
    }

}
