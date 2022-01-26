package GUI;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

import java.util.Queue;
import java.util.Vector;

import Main.Main;
import Database.DatabaseManager;
import Conversation.Message;
import Conversation.User;
import Conversation.User.Flag;
import Network.ClientTCP;


public class Interface {

    // ELEMENTS INTERFACE
    JFrame interfaceFrame;
    JPanel mainPanel;                     // Panneau principal qui supportera les composants
    public JTextField msgCapture, pseudoCapture; // Champs de texte
    public static JTextArea displayMsg, connected;          // Zone de texte
    public JButton sendMessageButton, changePseudoButton, destinataireButton, quitButton;  // Boutons 
    static JLabel pseudoLabel, destLabel, messageLabel;     // Labels (= affichage)
    JScrollPane scroll, scroller;
    JList<String> liste;
    Vector<User> users;
    static final boolean shouldFill = true;
    static final boolean shouldWeightX = true;
    static final boolean RIGHT_TO_LEFT = false;

    // GET VARIABLES FROM MAIN 
    static User user = Main.getMainUser();
    User destUser;
    static DatabaseManager database = Main.getMainDatabase();

    DefaultListModel userListToPrint;

    // CONSTRUCTEUR
    // Appel des méthodes créées ci-dessus

    public Interface() {
       
        // Create and set up the window.
        createWindow();
        createPannels();

        // Ajoute les composants 
        pseudo_setup(); //ajoute les champs relatifs au pseudo de l'utilisateur
        destinataire_setup(); //ajoute les champs relatifs au destinataire
        message_setup(); //ajoute les champs relatifs au message à envoyer
        conversation_setup();//ajoute la zone d'affichage de la conversation
        quit_conversation_setup();
        
        //Liste des utilisateurs connectés
        changePseudoWindow("Enter your pseudo");
        
        connected_setup();
        // Display the window.
        addComponentsToPane(interfaceFrame.getContentPane());
        interfaceFrame.setVisible(true);
    }


    



    // CREATION DE LA FENETRE

    /**
     * Créer la fenêtre
     */
    public void createWindow(){
        interfaceFrame = new JFrame("M&M's Chat System"); // Crée la fenetre qui supportera le panneau
        interfaceFrame.setVisible(true);
        interfaceFrame.addWindowListener(
            new WindowAdapter() { // Crée l'operation de fermeture.
                @Override
                public void windowClosing(WindowEvent e) { 
                    Main.closeSystem();
                    System.out.println("[Interface] Closing frame");
                    interfaceFrame.setVisible(false);
                    System.exit(0);
                }
            }
        );
        interfaceFrame.setSize(2400, 1800);
        
        GridBagConstraints c = new GridBagConstraints();
        if (shouldFill) {
            //natural height, maximum width
            c.fill = GridBagConstraints.BOTH;
        }  
    }

    /**
     * Créer un panel
     */
    public void createPannels(){
        // Create the main panel, there is only one
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0,0,0,0,0, 0};
        gridBagLayout.rowHeights = new int[]{0,0,0, 0};
        gridBagLayout.columnWeights = new double[]{0.4, 0.1, 0.2, 0.1, 0.2, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.05, 0.9, 0.05, Double.MIN_VALUE};
        interfaceFrame.getContentPane().setLayout(gridBagLayout);
    } //TODO : mettre un bouton quitter convo qui enleve dest, ferme tcp et efface l'historique
    
    /**
     * Ajouter des composants au panel
     * @param pane
     */
    public static void addComponentsToPane(Container pane) {
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
    }
 
    



    //ACTION LISTENER ET PERFORMED

    ActionListener pseudoListener = new ActionListener(){
        public void actionPerformed(ActionEvent e) {
            changePseudoWindow("Enter your new pseudo :");
        }
    };
    
    ActionListener messageListener = new ActionListener(){
        public void actionPerformed(ActionEvent e) {
            // Capture text
            String texteSaisi=msgCapture.getText();
            // Create message and stamp it
            Message message = new Message(user, destUser, texteSaisi);            
            // Print message
            printMessage(message);
            //Add it to database
            Main.getMainDatabase().nouveau_message(message);
            // Send it
            Main.sendMessage(message);
            msgCapture.setText(null);
        }
    };

    ActionListener destinataireListener = new ActionListener(){
        public void actionPerformed(ActionEvent e) {
            close_conversation();
            changeDestinataireWindow();
        }
    };

    ActionListener quitListener = new ActionListener(){
        public void actionPerformed(ActionEvent e) {
            if (destUser!=null)
                Main.getServerUDP().sendUnicast(user, destUser, Flag.CLOSE_CONVERSATION);
            close_conversation();
        }
    };

    Boolean once = true;
    ListSelectionListener connectedListener = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e)
        {
            //if (once){
                // Write new
                String selection = liste.getSelectedValue();
                if (selection != null) { // On a choisi un utilisateur
                    System.out.println("Selection : " + selection);
                    if (destUser!=null){ // On est déjà en train de parler à quelqu'un
                        long oldDestUserId=destUser.id;
                        destUser = Main.getUserByPseudo(selection);
                        if (destUser.id != oldDestUserId){ // Est ce qu'on veut parler à la même personne ?
                            // Ferme la conversation et passe à une nouvelle    
                            close_conversation();
                            destinataireChanged();
                        } else {
                            // Met à jour juste les champs de l'interface mais pas le tcp
                            activeConversation(destUser);
                            liste.setSelectedValue(destUser.pseudo, true);
                        }
                    } else {
                        // Demarre une conversation
                        destUser = Main.getUserByPseudo(selection);
                        clear_window();
                        destinataireChanged();
                    }
                }
            //}
            //once = !once;
        }    
    };




    //CREATION DES BOUTONS
    
    public void pseudo_setup(){
        GridBagConstraints c = new GridBagConstraints();
        // Pseudo label
        pseudoLabel = new JLabel("Pseudo :"+user.pseudo); 
        if (shouldWeightX) {
            c.weightx = 0.5;
        }
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;//colonne
        c.gridy = 0;//ligne
        interfaceFrame.add(pseudoLabel, c);
        // Pseudo button
        changePseudoButton=new JButton("Change Pseudo");
        changePseudoButton.addActionListener(pseudoListener);
        c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.5;
	    c.gridx = 2;
	    c.gridy = 0;
        interfaceFrame.add(changePseudoButton,c);
    }

    public void destinataire_setup(){
        GridBagConstraints c = new GridBagConstraints();
        // Destinataire label
        destLabel = new JLabel("Recipient : ");
        c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.5;
	    c.gridx = 3;
	    c.gridy = 0;
        interfaceFrame.add(destLabel,c);
        // Destinataire button
        destinataireButton = new JButton("Change Recipient");
        destinataireButton.addActionListener(destinataireListener);
        c.weightx = 0.5;
	    c.gridx = 4;
	    c.gridy = 0;
        interfaceFrame.add(destinataireButton,c);
    }

    public void message_setup(){
        GridBagConstraints c = new GridBagConstraints();
        // Message label
        messageLabel = new JLabel("Message :");
        c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.5;
	    c.gridx = 1;
	    c.gridy = 2;
        interfaceFrame.add(messageLabel, c);
        //Message field
        msgCapture=new JTextField();
        msgCapture.setEditable(false);
        msgCapture.addActionListener(messageListener); // capture le retour chariot
        c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.5;
	    c.gridx = 2;
	    c.gridy = 2;
        c.gridwidth=2;
        interfaceFrame.add(msgCapture, c);
        // Send Message Button
        sendMessageButton=new JButton("Send");
        sendMessageButton.addActionListener(messageListener); // Capture le clic sur le bouton L'instruction this indique que la classe elle même recevra et gérera l'événement utilisateur.
        sendMessageButton.setVisible(false);
        c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.5;
	    c.gridx = 4;
	    c.gridy = 2;
        interfaceFrame.add(sendMessageButton, c);
    }

    public void conversation_setup(){
        GridBagConstraints c = new GridBagConstraints();
        displayMsg =new JTextArea("CONVERSATION \n \n");
        displayMsg.setEditable(false); // Bloque l'édition de la zone de texte   
        scroll = new JScrollPane(displayMsg); 
        c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.0;
	    c.gridx = 1;
	    c.gridy = 1; 
        c.gridwidth = 4;
        interfaceFrame.add(scroll, c);
    }

    public void quit_conversation_setup(){
        GridBagConstraints c = new GridBagConstraints();
        quitButton = new JButton("Quit Conversation");
        quitButton.addActionListener(quitListener);
        c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.5;
	    c.gridx = 0;
	    c.gridy = 2;
        interfaceFrame.add(quitButton, c);
    }

    /** 
     * Permet d'afficher une liste de sélection des users
     * @param userList
     */
    public void connected_setup(){
        GridBagConstraints c = new GridBagConstraints();
        // Création du modèle de liste
        userListToPrint = new DefaultListModel();
        liste = new JList<>(userListToPrint);
        // Ajout de les utilisateurs connectés avant nous 
        for (String userConnected : Main.get_pseudos()) {
            userListToPrint.addElement(userConnected);
        }       
        liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        liste.addListSelectionListener(connectedListener);
        scroller = new JScrollPane(liste); 
        c.fill = GridBagConstraints.BOTH;
        c.weightx=0.0;
        c.gridx = 0;
	    c.gridy = 0;
        c.gridheight = 2;
       // c.gridheight = GridBagConstraints.REMAINDER;
        interfaceFrame.add(scroller, c);
    }



    /**
     * Affiche une fenetre pop-up pour changer le pseudo
     */
    public void changePseudoWindow(String message) {
        // Lance la fenetre
        JFrame jFrame = new JFrame();
        String newPseudo = JOptionPane.showInputDialog(jFrame, message);
        if(newPseudo!=null && !newPseudo.equals("")){
            user.change_pseudo(newPseudo);
            user.setFlag(Flag.PSEUDO_CHANGE);
            Main.getServerUDP().notifyPseudoOnNetwork();
            pseudoLabel.setText("Pseudo : "+user.pseudo);
            if (destUser!=null){
                activeConversation(destUser);
            }
        }else {
            sendPopUp("Please enter a pseudo");
            pseudoLabel.setText("Pseudo : Enter a pseudo to chat");
            msgCapture.setEditable(false);
            sendMessageButton.setVisible(false);
        }    
    }

    public void raisePseudoAlreadyUsed(){
        changePseudoWindow("Pseudo already used, enter a new one : ");
    }


    public void changeDestinataireWindow() {
        //displayMsg.removeAll();
        JFrame jFrame = new JFrame();
        String newDest = JOptionPane.showInputDialog(jFrame, "Enter the recipient");            
        destUser=Main.getUserByPseudo(newDest);
        if (destUser==null){
            sendPopUp("This user doesn't exists");
        } else{
            destinataireChanged();
        } 
    }


    //public void updateConnectedUserList(User new_user){
    //    userListToPrint.addElement(new_user.pseudo);
    //    // Si une conversation est en cours on la garde
    //    if (destUser != null && destUser.id==new_user.id){
    //        activeConversation(new_user);
    //    }
    //}

    public void addUserToList(String user_pseudo){
        userListToPrint.addElement(user_pseudo);
    }

    public void removeUserFromList(String user_pseudo){
        userListToPrint.removeElement(user_pseudo);
    }

    public void clearUserFromList(){
        userListToPrint.clear();
    }

    public void destinataireChanged(){
        if (Main.getServerTCP() == null)
            Main.startTCPServer(2051); //crée un serveur TCP prêt à acceuillir un msg de destUSer
        Main.getServerUDP().sendUnicast(user, destUser, Flag.INIT_CONVERSATION);
        activeConversation(destUser);
    }

    public void activeConversation(User to){
        destUser=to;
        destLabel.setText("Recipient : " + to.pseudo);
        displayMsg.setText(null);
        printHistory(user, destUser);
        msgCapture.setEditable(true);
        sendMessageButton.setVisible(true);
    }


    public void sendPopUp(String message){
        JFrame jFrame = new JFrame();
        JOptionPane.showMessageDialog(jFrame, message);
    }

    /* Affiche le message sur l'interface  **/
    public static void printMessage(Message msg){
        displayMsg.append(msg.date + "   " + msg.from.pseudo+" : "+ msg.msg +"\n"); // L'affiche 
    }

    /* Affiche l'historique sur l'interface : Liste des messages triés par date **/
    public void printHistory(User from, User to){
        Queue<Message> msgList = database.history(from, to);
        while (!msgList.isEmpty())
            printMessage(msgList.remove());
    }

    public void close_conversation(){
        if (Main.getServerTCP() != null) {
            Main.getServerTCP().close();
        }
        liste.clearSelection();
        clear_window();
        destUser=null;
    }


    public void clear_window(){
        displayMsg.setText(null);
        msgCapture.setText(null);
        msgCapture.setEditable(false);
        destLabel.setText("Recipient : ");
        sendMessageButton.setVisible(false);
    }

    public boolean isInConversation(User user){
        return (destUser.id==user.id);
    }

    public void updateConversationPseudo(){
        activeConversation(destUser);
        liste.setSelectedValue(destUser.pseudo, true);
    }
}