package GUI;

import java.io.*;
import java.lang.String;
import java.util.UUID;


public class User {

    public enum Status {
        CONNECTED, ABSENT, OCCUPIED
    }
    
    //Attributs
    public String pseudo;
    public int id;
    public Status status;

    private String userDataPath = "./udata";
    String init="00000000000";
    private char[] readBuf=init.toCharArray();
    private File userData;
    
    //Constructeur 
    public User(String pseudo, int id, Status status){
        this.pseudo = pseudo; this.id=id; this.status=status;
    }

    public User(String pseudo){
        this.pseudo=pseudo;

        // Automatically connected
        this.status=Status.CONNECTED;

        // Check if this user is new
        // The user already exists
        try(FileReader fileReader = new FileReader(userDataPath)) {
            int readErr = fileReader.read(readBuf, 0, 1); // Lit la ligne 0

            //if (readErr != 0) {
            //    fileReader.close();
            //    throw new FileNotFoundException();
            //} else {
                this.id = Integer.valueOf(String.copyValueOf(readBuf));
                System.out.println("The UUID already existing is " + this.id);
                fileReader.close();
            //}
        // First connection of the user
        } catch (FileNotFoundException e) {
            // Create the file user data
            userData = new File(userDataPath); 
            
            // Write the content in file 
            try(FileWriter fileWriter = new FileWriter(userDataPath)) {
                UUID uid = UUID.randomUUID(); // Give a random uid
                this.id = (int) uid.getLeastSignificantBits();
                String fileContent = Integer.toString(this.id);
                System.out.println("The new UUID is " + fileContent);
                this.id = Integer.valueOf(fileContent);
                fileWriter.write(fileContent);
                fileWriter.close();
            } catch (IOException e1) {
                System.out.println(e1);
            }

        } catch (IOException e) {
            System.out.println(e);
        }

    }

    //Méthodes
    /** Function for changing pseudo
     * @param new_pseudo new pseudo to be set
     */
    protected void change_pseudo(String new_pseudo){
        this.pseudo=new_pseudo;
    }
  
}
