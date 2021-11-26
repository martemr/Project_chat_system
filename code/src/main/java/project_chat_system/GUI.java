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

    public GUI() {
        // Create the phase selection and display panels.
        usersPanel = new JPanel();
        messagePanel = new JPanel();

        // Create the main panel to contain the two sub panels.
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        usersPanel.setBackground(Color.magenta);

        // Add the select and display panels to the main panel.
        mainPanel.add(usersPanel);
        mainPanel.add(messagePanel);
    }

    private static void createAndShowGUI() {
        // Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create a new instance of LunarPhases.
        GUI interfacePanels = new GUI();

        // Create and set up the window.
        JFrame interfaceFrame = new JFrame("M&M's Chat System");
        interfaceFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        interfaceFrame.setSize(800, 600);
        // interfaceFrame.setContentPane(phases.mainPanel);

        //Container c = interfaceFrame.getContentPane().add;

        // Display the window.
        // interfaceFrame.pack();
        interfaceFrame.setVisible(true);
    }

    public static void main(String[] args) {
        createAndShowGUI();
    }

    public void actionPerformed(ActionEvent e) {
    }

}
