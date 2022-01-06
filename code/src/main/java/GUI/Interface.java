package GUI;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import java.awt.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Vector;

import Main.Main;
import Database.DatabaseManager;
import Conversation.Message;
import Network.ClientTCP;

public class Interface {

    // ELEMENTS INTERFACE
    JFrame interfaceFrame;
    JPanel mainPanel;                     // Panneau principal qui supportera les composants
    JTextField msgCapture, pseudoCapture; // Champs de texte
    static JTextArea displayMsg, connected;          // Zone de texte
    JButton sendMessageButton, changePseudoButton, destinataireButton;  // Boutons 
    JLabel pseudoLabel, destLabel, messageLabel;     // Labels (= affichage)
    JScrollPane scroll;
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;

    // GET VARIABLES FROM MAIN 
    static User user = Main.getMainUser();
    static ClientTCP tcpClient = Main.getClientTCP();
    static DatabaseManager database = Main.getMainDatabase();


    // CONSTRUCTEUR
    // Appel des méthodes créées ci-dessus

    public Interface() {

        // Créer le(s) panneau(x)
        createPannels();

        // Create and set up the window.
        createWindow();
        pseudo_setup(); //ajoute les champs relatifs au pseudo de l'utilisateur
        destinataire_setup(); //ajoute les champs relatifs au destinataire
        message_setup(); //ajoute les champs relatifs au message à envoyer
        conversation_setup();//ajoute la zone d'affichage de la conversation

        //Liste des utilisateurs connectés
        connected_users();

        // Display the window.
        addComponentsToPane(interfaceFrame.getContentPane());
        //interfaceFrame.pack();
        interfaceFrame.setVisible(true);

        changePseudoWindow();
    }


    



    // METHODES

    /**
     * Créer un panel
     */
    public void createPannels(){
        // Create the main panel, there is only one
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
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
 
    /**
     * Créer la fenêtre
     */
    public void createWindow(){
        JFrame.setDefaultLookAndFeelDecorated(true);
        interfaceFrame = new JFrame("M&M's Chat System"); // Crée la fenetre qui supportera le panneau
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
        interfaceFrame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        if (shouldFill) {
            //natural height, maximum width
            c.fill = GridBagConstraints.BOTH;
        }  
    }

    /**
     * Affiche une fenetre pop-up pour changer le pseudo
     */
    public void changePseudoWindow() {
        JFrame jFrame = new JFrame();
        String newPseudo = JOptionPane.showInputDialog(jFrame, "Enter your pseudo");
        while (database.change_pseudo(user, newPseudo) == -1){
            JOptionPane.showMessageDialog(jFrame, "Pseudo already used, please select an other");
            newPseudo = JOptionPane.showInputDialog(jFrame, "Enter your pseudo");   
        }
        user.change_pseudo(newPseudo);
        pseudoLabel.setText("Pseudo : "+user.pseudo);
        JOptionPane.showMessageDialog(jFrame, "Pseudo successfully changed !");
    }

    /**
     * Affiche les utilisateurs connectés
     */
    public void connected_users(){
        Vector<User> userList = new Vector<>(); // TODO: tcpclient.getListUser();
        JList<User> users = new JList<User>(userList);
        //users.VERTICAL;
      //  users.addListSelectionListener(this);
        JScrollPane scroll = new JScrollPane(users);
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

    //ListSelectionListener connectedListener = new ListSelectionListener() {
    //    //TODO : trouver comment ecouter une liste de boutons
    //};






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
        c.anchor=GridBagConstraints.PAGE_START;
        interfaceFrame.add(pseudoLabel, c);
        // Pseudo button
        changePseudoButton=new JButton("Change Pseudo");
        changePseudoButton.setBounds(600, 0, 100,30);
        changePseudoButton.addActionListener(pseudoListener);
        c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.5;
	    c.gridx = 2;
	    c.gridy = 0;
        c.anchor=GridBagConstraints.PAGE_START;
        interfaceFrame.add(changePseudoButton,c);
    }

    public void destinataire_setup(){
        GridBagConstraints c = new GridBagConstraints();
        // Destinataire label
        destLabel = new JLabel("Recipient : ");
        c.anchor=GridBagConstraints.FIRST_LINE_END;
        interfaceFrame.add(destLabel,c);
        // Destinataire button
        destinataireButton = new JButton("Change Recipient");
        destinataireButton.addActionListener(destinataireListener);
        c.anchor=GridBagConstraints.FIRST_LINE_END;
        //TODO : créer les champs relatifs au destinataire
    }

    public void message_setup(){
        GridBagConstraints c = new GridBagConstraints();
        // Message label
        messageLabel = new JLabel("Message :");
        c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.5;
	    c.gridx = 0;
	    c.gridy = 2;
        c.anchor=GridBagConstraints.PAGE_END;
        interfaceFrame.add(messageLabel, c);
        //Message field
        msgCapture=new JTextField();
        msgCapture.addActionListener(messageListener); // capture le retour chariot
        c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.5;
	    c.gridx = 1;
	    c.gridy = 2;
        c.anchor=GridBagConstraints.PAGE_END;
        interfaceFrame.add(msgCapture, c);
        // Send Message Button
        sendMessageButton=new JButton("Send");
        sendMessageButton.addActionListener(messageListener); // Capture le clic sur le bouton L'instruction this indique que la classe elle même recevra et gérera l'événement utilisateur.
        c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.5;
	    c.gridx = 2;
	    c.gridy = 2;
        c.anchor=GridBagConstraints.LAST_LINE_END;
        interfaceFrame.add(sendMessageButton, c);
    }

    public void conversation_setup(){
        GridBagConstraints c = new GridBagConstraints();
        displayMsg =new JTextArea("CONVERSATION \n \n");
        displayMsg.setEditable(false); // Bloque l'édition de la zone de texte   
        scroll = new JScrollPane(displayMsg); 
        c.fill = GridBagConstraints.BOTH;
	    c.ipady = 400;      //make this component tall
	    c.weightx = 0.0;
	    c.gridwidth = 3;
	    c.gridx = 1;
	    c.gridy = 0; 
        c.anchor=GridBagConstraints.CENTER;  
        interfaceFrame.add(scroll, c);
    }

    public void connected_setup(){
        GridBagConstraints c = new GridBagConstraints();
        connected =new JTextArea("CONNECTED USERS \n \n");
        connected.setEditable(false); // Bloque l'édition de la zone de texte   
        scroll = new JScrollPane(connected); 
        c.gridx = 0;
	    c.gridy = 0;
        c.anchor=GridBagConstraints.LINE_START;
        interfaceFrame.add(scroll, c);
    }
}
