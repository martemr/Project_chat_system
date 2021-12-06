package GUI;

import java.awt.event.*;
import javax.swing.*;

import java.sql.Timestamp;    
import java.util.Date;  


import Database.DatabaseManager;

public class Interface implements ActionListener {

    // Elements interface
    JFrame interfaceFrame;
    JPanel mainPanel;                     // Panneau principal qui supportera les composants
    JTextField msgCapture, pseudoCapture; // Champs de texte
    JTextArea displayMsg;                 // Zone de texte
    JButton sendMessageButton, changePseudoButton;                   // Boutons 
    JLabel pseudoLabel, messageLabel;     // Labels (= affichage)
    User user ;
    JScrollPane scroll;


    ActionListener sendAction;

    Timestamp ts;
    Date date;

    public void createPannels(){
        // Create the main panel
        //  There is only one panel at this step
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    public Interface(DatabaseManager db) {

        // Créer le(s) panneau(x)
        createPannels();

        // Create and set up the window.
        JFrame.setDefaultLookAndFeelDecorated(true);
        interfaceFrame = new JFrame("M&M's Chat System"); // Crée la fenetre qui supportera le panneau
        interfaceFrame.setDefaultCloseOperation(closeApplication(db)); // TODO : Set the close action on the cross bouton
        interfaceFrame.setSize(800, 600);
        interfaceFrame.setLayout(null);

        //Create user
        user = new User("titi");
        
        // Pseudo field
        pseudoLabel = new JLabel("Pseudo :");
        pseudoLabel.setBounds(0, 0, 100, 30);  
        interfaceFrame.add(pseudoLabel);

        pseudoCapture=new JTextField(user.pseudo);
        pseudoCapture.setBounds(100, 0, 500,30);  
        pseudoCapture.addActionListener(this); // capture le retour chariot
        interfaceFrame.add(pseudoCapture); // lie la capture à la fenetre

        changePseudoButton=new JButton("Change Pseudo");
        changePseudoButton.setBounds(600, 0, 100,30);
        changePseudoButton.addActionListener(this);
        interfaceFrame.add(changePseudoButton);
        
        // Message fields
        messageLabel = new JLabel("Message :");
        messageLabel.setBounds(0, 30, 100,30);
        interfaceFrame.add(messageLabel);

        msgCapture=new JTextField();
        msgCapture.setBounds(100, 30, 500,30);  
        msgCapture.addActionListener(this); // capture le retour chariot
        interfaceFrame.add(msgCapture);
        
        sendMessageButton=new JButton("Send");
        sendMessageButton.setBounds(600, 30, 100, 30);
        sendMessageButton.addActionListener(this); // Capture le clic sur le bouton L'instruction this indique que la classe elle même recevra et gérera l'événement utilisateur.
        interfaceFrame.add(sendMessageButton);

        // Message display
        displayMsg =new JTextArea("CONVERSATION \n \n");
        displayMsg.setEditable(false); // Bloque l'édition de la zone de texte   
        scroll = new JScrollPane(displayMsg);  
        scroll.setBounds(0,60,500,300);   
        interfaceFrame.add(scroll);

        // Display the window.
        interfaceFrame.setVisible(true);
    }

    // Zone de gestion des actions 
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if (source==msgCapture || source==sendMessageButton) {
            String texteSaisi=msgCapture.getText(); // Capure le texte lors de l'évènement 
            ts=new Timestamp(System.currentTimeMillis());  
            date=new Date(ts.getTime());
            displayMsg.append(date + "   "+user.pseudo+" : "+ texteSaisi+"\n"); // L'affiche 
        } 
        else if (source==pseudoCapture || source==changePseudoButton) {
            String nouveauPseudo=pseudoCapture.getText();
            user.pseudo= nouveauPseudo;
        }
    }

    public int closeApplication(DatabaseManager db) {
        db.closeConnection();
        interfaceFrame.dispose();
        return 0;
    }


}
