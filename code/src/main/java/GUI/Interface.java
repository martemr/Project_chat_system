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

        // Créer le(s) panneau(x)
        
        // Create and set up the window.
        createWindow();
        createPannels();
        pseudo_setup(); //ajoute les champs relatifs au pseudo de l'utilisateur
        
        destinataire_setup(); //ajoute les champs relatifs au destinataire
        message_setup(); //ajoute les champs relatifs au message à envoyer
        conversation_setup();//ajoute la zone d'affichage de la conversation
        //connected_setup();

        
        //Liste des utilisateurs connectés
        
        
        // Display the window.
        addComponentsToPane(interfaceFrame.getContentPane());
        //interfaceFrame.pack();
        interfaceFrame.setVisible(true);
        
        changePseudoWindow();
        connected_setup(Main.connectedUsers);

        //connected_users(Main.getServerUDP().connectedUsers);
    }


    



    // METHODES

    /**
     * Créer un panel
     */
    public void createPannels(){
        // Create the main panel, there is only one
        //mainPanel = new JPanel();
        //mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        //mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0,0,0,0,0, 0};
        gridBagLayout.rowHeights = new int[]{0,0,0, 0};
        gridBagLayout.columnWeights = new double[]{0.4, 0.1, 0.2, 0.1, 0.2, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.05, 0.9, 0.05, Double.MIN_VALUE};
        interfaceFrame.getContentPane().setLayout(gridBagLayout);
        //mainPanel.setLayout(gridBagLayout);
        //interfaceFrame.add(mainPanel, gridBagLayout);
        //interfaceFrame.getContentPane();
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
        //interfaceFrame.setLayout(new GridBagLayout());
        
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
        //while (database.change_pseudo(user, newPseudo) == -1){
        //    JOptionPane.showMessageDialog(jFrame, "Pseudo already used, please select an other");
        //    newPseudo = JOptionPane.showInputDialog(jFrame, "Enter your pseudo");   
        //}
        user.change_pseudo(newPseudo);
        pseudoLabel.setText("Pseudo : "+user.pseudo);
        sendPopUp("Pseudo successfully changed !");
        //jFrame.dispose();
    }

    public void sendPopUp(String message){
        JFrame jFrame = new JFrame();
        JOptionPane.showMessageDialog(jFrame, message);
    }

    /**
     * Permet de récupérer une liste de pseudos à partir du tableau de users
     * @param users
     * @return
     */
    public String[] get_pseudo(Vector<User> users){
        String[] pseudos = new String[users.size()];
        for(int i=0; i<users.size();i++){
            pseudos[i]=users.get(i).pseudo;
        }
        return pseudos;
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
      //  c.anchor=GridBagConstraints.PAGE_START;
        interfaceFrame.add(pseudoLabel, c);
        // Pseudo button
        changePseudoButton=new JButton("Change Pseudo");
        //changePseudoButton.setBounds(600, 0, 100,30);
        changePseudoButton.addActionListener(pseudoListener);
        c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.5;
	    c.gridx = 2;
	    c.gridy = 0;
       // c.anchor=GridBagConstraints.PAGE_START;
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
       // c.anchor=GridBagConstraints.LAST_LINE_START;
        interfaceFrame.add(destLabel,c);
        // Destinataire button
        destinataireButton = new JButton("Change Recipient");
        destinataireButton.addActionListener(destinataireListener);
        c.weightx = 0.5;
	    c.gridx = 4;
	    c.gridy = 0;
       // c.anchor=GridBagConstraints.LAST_LINE_START;
        interfaceFrame.add(destinataireButton,c);
        //TODO : créer les champs relatifs au destinataire
    }

    public void message_setup(){
        GridBagConstraints c = new GridBagConstraints();
        // Message label
        messageLabel = new JLabel("Message :");
        c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.5;
	    c.gridx = 1;
	    c.gridy = 2;
        //c.anchor=GridBagConstraints.PAGE_END;
        interfaceFrame.add(messageLabel, c);
        //Message field
        msgCapture=new JTextField();
        msgCapture.addActionListener(messageListener); // capture le retour chariot
        c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.5;
	    c.gridx = 2;
	    c.gridy = 2;
        c.gridwidth=2;
       // c.anchor=GridBagConstraints.PAGE_END;
        interfaceFrame.add(msgCapture, c);
        // Send Message Button
        sendMessageButton=new JButton("Send");
        sendMessageButton.addActionListener(messageListener); // Capture le clic sur le bouton L'instruction this indique que la classe elle même recevra et gérera l'événement utilisateur.
        c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.5;
	    c.gridx = 4;
	    c.gridy = 2;
       // c.anchor=GridBagConstraints.LAST_LINE_END;
        interfaceFrame.add(sendMessageButton, c);
    }

    public void conversation_setup(){
        GridBagConstraints c = new GridBagConstraints();
        displayMsg =new JTextArea("CONVERSATION \n \n");
        displayMsg.setEditable(false); // Bloque l'édition de la zone de texte   
        scroll = new JScrollPane(displayMsg); 
        c.fill = GridBagConstraints.BOTH;
	   // c.ipady = 400;      //make this component tall
	    c.weightx = 0.0;
	    c.gridx = 1;
	    c.gridy = 1; 
        c.gridwidth = 4;
       // c.anchor=GridBagConstraints.CENTER;  
        interfaceFrame.add(scroll, c);
    }

    /** 
     * Permet d'afficher une liste de sélection des users
     * @param userList
     */
    public void connected_users(Vector<User> userList){
        // TODO: tcpclient.getListUser();
        String[] users = get_pseudo(userList);
        liste = new JList<String>(users);
        liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        liste.addListSelectionListener(connectedListener);
        interfaceFrame.add(liste);
    }

    public void connected_setup(Vector<User> userList){
        GridBagConstraints c = new GridBagConstraints();
        
        String[] users = get_pseudo(userList);
        liste = new JList<String>(users);
        liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        liste.addListSelectionListener(connectedListener);
        //connected =new JTextArea("CONNECTED USERS \n \n");
        //liste.setEditable(false); // Bloque l'édition de la zone de texte   
        scroller = new JScrollPane(liste); 
        c.fill = GridBagConstraints.BOTH;
        c.weightx=0.0;
        c.gridx = 0;
	    c.gridy = 0;
        c.gridheight = 3;
        c.gridheight = GridBagConstraints.REMAINDER;
     //   c.anchor=GridBagConstraints.LINE_START;
        interfaceFrame.add(scroller, c);
    }
}
