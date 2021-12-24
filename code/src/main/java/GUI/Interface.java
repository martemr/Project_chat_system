package GUI;

import java.awt.event.*;
import javax.swing.*;

import java.awt.*;

import Main.Main;
import Database.DatabaseManager;
import Conversation.Message;

public class Interface implements ActionListener {

    // Elements interface
    JFrame interfaceFrame;
    JPanel mainPanel;                     // Panneau principal qui supportera les composants
    JTextField msgCapture, pseudoCapture; // Champs de texte
    static JTextArea displayMsg;          // Zone de texte
    JButton sendMessageButton, changePseudoButton;  // Boutons 
    JLabel pseudoLabel, messageLabel;     // Labels (= affichage)
    JScrollPane scroll;

    static User user = Main.getMainUser();

    ActionListener sendAction;

    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;

    public static void addComponentsToPane(Container pane) {
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }}

    public void createPannels(){
        // Create the main panel, there is only one
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    public Interface() {

        // Créer le(s) panneau(x)
        createPannels();

        // Create and set up the window.
        JFrame.setDefaultLookAndFeelDecorated(true);
        interfaceFrame = new JFrame("M&M's Chat System"); // Crée la fenetre qui supportera le panneau
        interfaceFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        interfaceFrame.setSize(2400, 1800);
        interfaceFrame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        if (shouldFill) {
            //natural height, maximum width
            c.fill = GridBagConstraints.BOTH;
            }
        
        // Pseudo field
        pseudoLabel = new JLabel("Pseudo :");
        //pseudoLabel.setBounds(0, 0, 100, 30);  
        if (shouldWeightX) {
            c.weightx = 0.5;
            }
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            c.gridy = 0;
            c.anchor=GridBagConstraints.FIRST_LINE_START;
        interfaceFrame.add(pseudoLabel, c);

        pseudoCapture=new JTextField(user.pseudo);
        //pseudoCapture.setBounds(100, 0, 500,30);  
        pseudoCapture.addActionListener(this); // capture le retour chariot
        c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.5;
	    c.gridx = 1;
	    c.gridy = 0;
        c.anchor=GridBagConstraints.PAGE_START;
        interfaceFrame.add(pseudoCapture, c); // lie la capture à la fenetre

        changePseudoButton=new JButton("Change Pseudo");
        changePseudoButton.setBounds(600, 0, 100,30);
        changePseudoButton.addActionListener(this);
        c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.5;
	    c.gridx = 2;
	    c.gridy = 0;
        c.anchor=GridBagConstraints.FIRST_LINE_END;
        interfaceFrame.add(changePseudoButton,c);
        
        // Message fields
        messageLabel = new JLabel("Message :");
       // messageLabel.setBounds(0, 30, 100,30);
       c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.5;
	    c.gridx = 0;
	    c.gridy = 2;
        c.anchor=GridBagConstraints.LAST_LINE_START;
        interfaceFrame.add(messageLabel, c);

        msgCapture=new JTextField();
       // msgCapture.setBounds(100, 30, 500,30);  
        msgCapture.addActionListener(this); // capture le retour chariot
        c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.5;
	    c.gridx = 1;
	    c.gridy = 2;
        c.anchor=GridBagConstraints.PAGE_END;
        interfaceFrame.add(msgCapture, c);
        
        sendMessageButton=new JButton("Send");
       // sendMessageButton.setBounds(600, 30, 100, 30);
        sendMessageButton.addActionListener(this); // Capture le clic sur le bouton L'instruction this indique que la classe elle même recevra et gérera l'événement utilisateur.
        c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.5;
	    c.gridx = 2;
	    c.gridy = 2;
        c.anchor=GridBagConstraints.FIRST_LINE_END;
        interfaceFrame.add(sendMessageButton, c);

        // Message display
        displayMsg =new JTextArea("CONVERSATION \n \n");
        displayMsg.setEditable(false); // Bloque l'édition de la zone de texte   
        scroll = new JScrollPane(displayMsg); 
        c.fill = GridBagConstraints.BOTH;
	    c.ipady = 400;      //make this component tall
	    c.weightx = 0.0;
	    c.gridwidth = 3;
	    c.gridx = 0;
	    c.gridy = 1; 
        c.anchor=GridBagConstraints.CENTER;
       // scroll.setBounds(0,60,500,300);   
        interfaceFrame.add(scroll, c);

        // Display the window.
        addComponentsToPane(interfaceFrame.getContentPane());
        //interfaceFrame.pack();
        interfaceFrame.setVisible(true);
    }

    // Zone de gestion des actions 
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        // Send message
        if (source==msgCapture || source==sendMessageButton) {
            // Capture text
            String texteSaisi=msgCapture.getText();
            // Create message and stamp it
            Message message = new Message(user, user, texteSaisi);

            
            // Print message
            printMessage(message);
        } 
        // Change pseudo
        else if (source==pseudoCapture || source==changePseudoButton) {
            String nouveauPseudo=pseudoCapture.getText();
            user.pseudo= nouveauPseudo;
        }
    }



    /* Affiche le message sur l'interface  **/
    public static void printMessage(Message msg){
        displayMsg.append(msg.date + "   " + msg.from.pseudo+" : "+ msg.msg +"\n"); // L'affiche 
    }

    // TODO : Remove this, use for test
    public static void printMessage(String msgTxt){
        //TODO : ajouter message à la base de données pour pas qu'il disparaisse
        Message message = new Message(user, user, msgTxt);
        printMessage(message);
    }

}
