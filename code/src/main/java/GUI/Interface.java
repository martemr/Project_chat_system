package GUI;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Vector;

import Main.Main;
import Database.DatabaseManager;
import GUI.User.Flag;
import Conversation.Message;
import Network.ClientTCP;

public class Interface {

    // ELEMENTS INTERFACE
    JFrame interfaceFrame;
    JPanel mainPanel;                     // Panneau principal qui supportera les composants
    JTextField msgCapture, pseudoCapture; // Champs de texte
    static JTextArea displayMsg, connected;          // Zone de texte
    JButton sendMessageButton, changePseudoButton, destinataireButton;  // Boutons 
    static JLabel pseudoLabel, destLabel, messageLabel;     // Labels (= affichage)
    JScrollPane scroll, scroller;
    JList<String> liste;
    Vector<User> users;
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;
    //GridBagLayout gridBagLayout;
    //GridBagConstraints c;

    // GET VARIABLES FROM MAIN 
    static User user = Main.getMainUser();
    static ClientTCP tcpClient = Main.getClientTCP();
    static DatabaseManager database = Main.getMainDatabase();


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
        
        //Liste des utilisateurs connectés
        changePseudoWindow();
        
        updateConnectedUserList();
        // Display the window.
        addComponentsToPane(interfaceFrame.getContentPane());
        interfaceFrame.setVisible(true);
    }


    



    // CREATION DE LA FENETRE

    /**
     * Créer la fenêtre
     */
    public void createWindow(){
        JFrame.setDefaultLookAndFeelDecorated(true);
        interfaceFrame = new JFrame("M&M's Chat System"); // Crée la fenetre qui supportera le panneau
        interfaceFrame.setVisible(true);
        interfaceFrame.addWindowListener(
            new WindowAdapter() { // Crée l'operation de fermeture.
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
    }
    
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
            changePseudoWindow();
        }
    };
    
    ActionListener messageListener = new ActionListener(){
        public void actionPerformed(ActionEvent e) {
            // Capture text
            String texteSaisi=msgCapture.getText();
            // Create message and stamp it
            Message message = new Message(user, user, texteSaisi);            
            // Print message
            printMessage(message);
            // Send it
            // TODO : tcpClient.sendMessage(message);
        }
    };

    ActionListener destinataireListener = new ActionListener(){
        public void actionPerformed(ActionEvent e) {
            //TODO : définir Message.to
        }
    };

    ListSelectionListener connectedListener = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e)
        {
            int index = liste.getSelectedIndex();
            User destinataire = users.get(index);
            //TODO:lancer thread conversation
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

    /** 
     * Permet d'afficher une liste de sélection des users
     * @param userList
     */
    public void connected_setup(Vector<User> userList){
        GridBagConstraints c = new GridBagConstraints();
        String[] users = Main.get_pseudo(userList);
        liste = new JList<String>(users);
        liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        liste.addListSelectionListener(connectedListener);
        scroller = new JScrollPane(liste); 
        c.fill = GridBagConstraints.BOTH;
        c.weightx=0.0;
        c.gridx = 0;
	    c.gridy = 0;
        c.gridheight = 3;
        c.gridheight = GridBagConstraints.REMAINDER;
        interfaceFrame.add(scroller, c);
    }



    /**
     * Affiche une fenetre pop-up pour changer le pseudo
     */
    public void changePseudoWindow() {
        // Lance la fenetre
        JFrame jFrame = new JFrame();
        String newPseudo = JOptionPane.showInputDialog(jFrame, "Enter your pseudo");
        // Change les parametre du User
        user.change_pseudo(newPseudo);
        user.setFlag(Flag.CONNECTION);
        // Vérifie l'unicité
        while (!Main.getClientUDP().isUniquePseudoOnNetwork()){
            newPseudo = JOptionPane.showInputDialog(jFrame, "Pseudo already used, enter a new one : ");
        }
        // Pseudo unique, connection autorisé
        user.setFlag(Flag.CONNECTED);
        // Met à jour l'interface
        pseudoLabel.setText("Pseudo : "+user.pseudo);
        sendPopUp("Pseudo successfully changed !");       
    }


    public void updateConnectedUserList(){
        connected_setup(Main.connectedUsers);
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
        int i;
        for(i=0; i<msgList.size(); i++){
            printMessage(msgList.remove());
        }
    }
    
    // TODO : Remove this, use for test
    public static void printMessage(String msgTxt){
        //TODO : ajouter message à la base de données pour pas qu'il disparaisse
        Message message = new Message(user, user, msgTxt);
        printMessage(message);
    }





}