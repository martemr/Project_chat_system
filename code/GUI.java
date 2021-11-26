package code;
/*
 * SwingApplication.java is a 1.4 example that requires
 * no other files.
 */
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import java.awt.Container;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class GUI implements ActionListener {

    public Component createComponents() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(600, 1000));;
        panel.setBounds(600,600,1000,600);
        //panel.setBorder(BorderFactory.createEmptyBorder(-5,0,10000,1400)); //top left bottom right
        panel.setBackground(Color.lightGray);
        panel.setOpaque(false);

        JButton btn1 = new JButton("Bouton 1");
        btn1.setBorder(BorderFactory.createEmptyBorder(0,0,600,600));
        //Spécifier la couleur d'arrière-plan du bouton
        btn1.setBackground(Color.WHITE); 
        panel.add(btn1);

        return panel;
    }

    public static void main(String[] args) 
    {
        JFrame frame = new JFrame("M&M's Chat System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 1500);
        Container c = frame.getContentPane();
        c.setBackground(Color.magenta);
        frame.setVisible(true);
    }

    


    /*private static String labelPrefix = "Welcome on M&M's chat system ";
    
    private int numClicks = 0;
    final JLabel label = new JLabel(labelPrefix + "0    ");

    
    public Component createComponents() {
        JButton button = new JButton("Open chat");
        button.setMnemonic(KeyEvent.VK_I);
        button.addActionListener(this);
        label.setLabelFor(button);

        // Create a second button. The same event handler will be used. 
        /*JButton button2 = new JButton("I'm 2nd Swing button!"); 
        button2.setMnemonic(KeyEvent.VK_L); 
        button2.addActionListener(this); 
        label.setLabelFor(button2); 
    

        
        /*
         * An easy way to put space between a top-level container
         * and its contents is to put the contents in a JPanel
         * that has an "empty" border.

        JPanel pane = new JPanel(new GridLayout(0, 1));
        pane.add(button);
        //Add 2nd button to the JPanel 
        //pane.add(button2); 
        pane.add(label);
        /*pane.setBorder(BorderFactory.createEmptyBorder(
                30, //top
                30, //left
                10, //bottom
                30) //right
                );
        
        return pane;
    }
    */

   // private int numClicks = 0;
   // private static String labelPrefix = "Welcome on M&M's chat system ";
    //final JLabel label = new JLabel(labelPrefix + "0    ");
    
    // Modify the event handler code depending on which button is pressed. 
    // If the 1st button is pressed, increase the numClicks value by 1, else 
    // increase the value by 1000. 
    public void actionPerformed(ActionEvent e) { 
 
        // Using getActionCommand() method is a bit of a hack, but for the 
        // sake of this exercise, it serves its purpose. 
       /* if (e.getActionCommand().equals("I'm a Swing button!")){ 
            numClicks++; 
        } 
        else{ 
            numClicks += 1000; 
        } 
        label.setText(labelPrefix + numClicks); */
    } 
/*
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     
    private static void createAndShowGUI() {
        
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        
        //Create and set up the window.
        JFrame frame = new JFrame("SwingApplication");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        GUI app = new GUI();
        Component contents = app.createComponents();
        frame.getContentPane().add(contents, BorderLayout.CENTER);
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }*/
}