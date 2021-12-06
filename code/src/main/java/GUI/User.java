package GUI;

public class User {
    public enum Status {
        CONNECTED, ABSENT, OCCUPIED
    }

    //Attributs
    public String pseudo;
    private int id;
    public Status status;

    //Constructeur 
    public User(String pseudo){
        this.pseudo=pseudo;
       // this.id=QQCHONSAITPASQUOI.GETNEWID(); //TODO 
        this.status=Status.CONNECTED;
    }

    //Méthodes
    /** Function for changing pseudo
     * @param new_pseudo new pseudo to be set
     */
    protected void change_pseudo(String new_pseudo){
        this.pseudo=new_pseudo;
    }
  
}
