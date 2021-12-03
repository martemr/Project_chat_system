package project_chat_system;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUI implements ActionListener {

    JPanel mainPanel;                     // Panneau principal qui supportera les composants
    JTextField msgCapture, pseudoCapture; // Champs de texte
    JTextArea displayMsg;                 // Zone de texte
    JButton sendMessageButton, changePseudoButton;                   // Boutons 
    JLabel pseudoLabel, messageLabel;     // Labels (= affichage)
    User user ;

    ActionListener sendAction;

    public void createPannels(){
        // Create the main panel
        //  There is only one panel at this step
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    public GUI() {

        // Créer le(s) panneau(x)
        createPannels();

        // Create and set up the window.
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame interfaceFrame = new JFrame("M&M's Chat System"); // Crée la fenetre qui supportera le panneau
        interfaceFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        displayMsg =new JTextArea();
        interfaceFrame.add(displayMsg);
        displayMsg.setBounds(0, 60, 500,300);
        displayMsg.setEditable(false); // Bloque l'édition de la zone de texte        

        // Display the window.
        interfaceFrame.setVisible(true);
    }

    // Main fonction (appelée en premier lors de l'exécution)
    public static void main(String[] args) {
        new GUI();
    }

    // Zone de gestion des actions 
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if (source==msgCapture || source==sendMessageButton) {
            String texteSaisi=msgCapture.getText(); // Capure le texte lors de l'évènement 
            displayMsg.setText(user.pseudo+" : "+ texteSaisi); // L'affiche 
        } 
        else if (source==pseudoCapture || source==changePseudoButton) {
            String nouveauPseudo=pseudoCapture.getText();
            user.pseudo= nouveauPseudo;
        }
    }
}
