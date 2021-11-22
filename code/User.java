package code;

public enum Status {
    CONNECTED, ABSENT, OCCUPIED
}

public class User {

    //Attributs
    public String pseudo;
    private int id;
    public Status status;

    //Constructeur 
    public User(String pseudo){
        this.pseudo=pseudo;
        this.id=QQCHONSAITPASQUOI.GETNEWID(); //TODO 
        this.status=CONNECTED;
    }

    //Méthodes
    protected change_pseudo();
  
}
