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

    private String userDataPath = "../../udata";
    private String readBuf = new String();
    private File userData;
    
    //Constructeur 
    public User(String pseudo){
        this.pseudo=pseudo;

        // Automatically connected
        this.status=Status.CONNECTED;

        // Check if this user is new
        // The user already exists
        try(FileReader fileReader = new FileReader(userDataPath)) {
            int ch = fileReader.read();
            while(ch != -1) {
                readBuf+=ch;
            }
            this.id = Integer.valueOf(readBuf);
            fileReader.close();
        // First connection of the user
        } catch (FileNotFoundException e) {
            // Create the file user data
            userData = new File(userDataPath); 
            
            // Write the content in file 
            try(FileWriter fileWriter = new FileWriter(userDataPath)) {
                String fileContent = UUID.randomUUID().toString();
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
