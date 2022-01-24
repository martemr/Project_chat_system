package GUI;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class User implements Serializable{

    public enum Flag {
        CONNECTION, PSEUDO_CHANGE, PSEUDO_NOT_AVAILABLE, CONNECTED, DISCONNECTION, INIT_CONVERSATION, REFUSE_CONVERSATION
    }
    
    //Attributs
    public String pseudo;
    public String oldPseudo;
    public long id;
    public Flag flag; 
    public int portTCP;
    private File userData;
    public InetAddress IPAddress;
    public InetAddress IPAddressBroadcast;
    
    //Constructeur 
    public User(String pseudo, long id, Flag flag){
        this.pseudo = pseudo; this.id=id; this.flag=flag; this.oldPseudo="";
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
        this.oldPseudo="";
        // Creating or getting the id
        this.id=0;
        try {
            // Creating or getting the .userdata file (situated in Project_chat_system/code/.userdata)
            String localDir = System.getProperty("user.dir");
            userData = new File(localDir + "/code/.userdata");
            if (!userData.exists()) { 
                if (!userData.createNewFile()) throw new FileNotFoundException();
                System.out.println("[User] file code/.userdata doesn't exist, creating one...");
            }

            // Read the existing uid  
            BufferedReader reader = new BufferedReader(new FileReader(userData));
            String line = reader.readLine();
            while (line != null) {
                if (line.startsWith("uid:")){
                    String uidString = line.substring(4, 13); // id is 14 characters long
                    this.id = Integer.valueOf(uidString);
                    System.out.println("[User] id successfully get ");
                }
                line = reader.readLine(); // read next line 
            }
            reader.close();

            // Creating a new id if not already gave
            if (this.id==0){
                this.id = randomGenerator(); // Le uid est défini aléatoirement
                FileWriter fileWriter = new FileWriter(userData);
                fileWriter.write("uid:"+this.id);
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
        this.oldPseudo=this.pseudo;
        this.pseudo=new_pseudo;
    }

    public void setFlag(Flag new_flag){
        this.flag=new_flag;
    }

    public void set_ip_local(String ip){
        try{
            this.IPAddress= InetAddress.getByName(ip);
        } catch (UnknownHostException e){
            System.out.println("[User] UnknownHostException");
            e.printStackTrace();
        }
    }

    public void set_ip_broadcast(String ip){
        try{
            this.IPAddressBroadcast= InetAddress.getByName(ip);
        } catch (UnknownHostException e){
            System.out.println("[User] UnknownHostException");
            e.printStackTrace();
        }
    }
  
    private long randomGenerator(){
        return (long)Math.floor(Math.random()*(999999999-100000000+1)+100000000);
    }
}
