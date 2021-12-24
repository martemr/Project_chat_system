package GUI;

import java.io.*;

public class User implements Serializable{

    public enum Status {
        CONNECTED, ABSENT, OCCUPIED
    }
    
    //Attributs
    public String pseudo;
    public int id;
    public Status status;
    private File userData;
    
    //Constructeur 
    @Deprecated
    public User(String pseudo, int id, Status status){
        this.pseudo = pseudo; this.id=id; this.status=status;
    }

    /** Constructor for User
     *  @param pseudo default name for the pseudo 
     *  @return User where
     *      Pseudo = pseudo
     *      Status = CONNECTED
     *      id = existing id or new id created (an id is already existing if the program has already been executed on the machine)
     *  @throws IOException
     */
    public User(String pseudo){
        this.pseudo=pseudo;

        // Automatically connected
        this.status=Status.CONNECTED;

        // Creating or getting the id
        this.id=0;
        try {
            // Creating or getting the .userdata file (situated in Project_chat_system/code/.userdata)
            String localDir = System.getProperty("user.dir");
            userData = new File(localDir + "/code/.userdata");
            if (!userData.exists()) { 
                if (!userData.createNewFile()) throw new FileNotFoundException();
                System.out.println("[User] file code/.userdata not exists, creating one...");
            }

            // Read the existing uid  
            BufferedReader reader = new BufferedReader(new FileReader(userData));
            String line = reader.readLine();
            while (line != null) {
                if (line.startsWith("uid:")){
                    String uidString = line.substring(4, 14); // id is 14 characters long
                    this.id = Integer.valueOf(uidString);
                    System.out.println("[User] id successfully get ");
                }
                line = reader.readLine(); // read next line 
            }
            reader.close();

            // Creating a new id if not already gave
            if (this.id==0){
                this.id = this.hashCode(); // Le uid est défini avec le hash de la classe
                FileWriter fileWriter = new FileWriter(userData);
                fileWriter.write("uid:"+Integer.toString(this.id));
                fileWriter.close();
                System.out.println("[User] id created");
            }
            
        } catch (IOException e) {
            System.out.println("[User] " + e);
        }
    }

    /** Function for changing pseudo
     * @param new_pseudo new pseudo to be set
     */
    protected void change_pseudo(String new_pseudo){
        this.pseudo=new_pseudo;
    }
  
}
