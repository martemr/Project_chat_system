package project_chat_system;

/*
 * SwingApplication.java is a 1.4 example that requires
 * no other files.
 */
/*
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import java.awt.Container;
import java.awt.Component;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUI implements ActionListener {

    JPanel mainPanel, usersPanel, messagePanel;
    JTextField msgCapture, pseudoCapture;
    JTextArea displayMsg;
    JButton sendButton;
    JLabel pseudoLabel, messageLabel;

    ActionListener sendAction;

    public void createPannels(){
        // Create the phase selection and display panels.
        //usersPanel = new JPanel();
        //messagePanel = new JPanel();

        // Create the main panel to contain the two sub panels.
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        //usersPanel.setBackground(Color.magenta);
        //usersPanel.setBounds(100, 100, 100, 100);

        // Add the select and display panels to the main panel.
        //mainPanel.add(usersPanel);
        //mainPanel.add(messagePanel);
    }

    public GUI() {
        createPannels();

        // Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create and set up the window.
        JFrame interfaceFrame = new JFrame("M&M's Chat System");
        interfaceFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        interfaceFrame.setSize(800, 600);
        interfaceFrame.setLayout(null);
        
        // Pseudo field
        pseudoLabel = new JLabel("Pseudo :");
        pseudoLabel.setBounds(0, 0, 100, 30);  
        interfaceFrame.add(pseudoLabel);

        pseudoCapture=new JTextField();
        pseudoCapture.setBounds(100, 0, 500,30);  
        pseudoCapture.addActionListener(this); // capture le retour chariot
        interfaceFrame.add(pseudoCapture);
        
        // Message fields
        messageLabel = new JLabel("Message :");
        messageLabel.setBounds(0, 30, 100,30);
        interfaceFrame.add(messageLabel);

        msgCapture=new JTextField();
        msgCapture.setBounds(100, 30, 500,30);  
        msgCapture.addActionListener(this); // capture le retour chariot
        interfaceFrame.add(msgCapture);
        
        sendButton=new JButton("Send");
        sendButton.setBounds(600, 30, 100, 30);
        sendButton.addActionListener(this); 
        interfaceFrame.add(sendButton);

        // Message display
        displayMsg =new JTextArea();
        interfaceFrame.add(displayMsg);
        displayMsg.setBounds(0, 60, 500,300);
        displayMsg.setEditable(false); // Bloque l'édition de la zone de texte        

        // Display the window.
        interfaceFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new GUI();
    }

    public void actionPerformed(ActionEvent e) {
        String texteSaisi=msgCapture.getText();
        displayMsg.setText(texteSaisi);
    }

}
