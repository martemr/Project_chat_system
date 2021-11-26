package code;

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
        this.pseudo="default_pseudo";
       // this.id=QQCHONSAITPASQUOI.GETNEWID(); //TODO 
        //this.status=CONNECTED;
    }

    //Méthodes
    protected void change_pseudo(String new_pseudo){
        this.pseudo=new_pseudo;
    }
  
}
