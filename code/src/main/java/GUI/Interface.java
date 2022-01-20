package GUI;

import Conversation.Message;
import Database.DatabaseManager;
import GUI.User.Flag;
import Main.Main;
import Network.ClientTCP;
import java.util.Queue;
import java.util.Vector;
import javax.swing.event.ListSelectionListener;





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
    User destUser;
    //static ClientTCP tcpClient = Main.getClientTCP();
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
        
        //Liste des utilisateurs connectés
        changePseudoWindow();
        
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
            Message message = new Message(user, destUser, texteSaisi);            
            // Print message
            printMessage(message);
            //Add it to database
            Main.getMainDatabase().nouveau_message(message);
            // Send it
            Main.tcpClient.sendMessage(message);

            //Main.getClientTCP().sendMessage(message);
        }
    };

    ActionListener destinataireListener = new ActionListener(){
        public void actionPerformed(ActionEvent e) {
            changeDestinataireWindow();
        }
    };

    ListSelectionListener connectedListener = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e)
        {
            displayMsg.removeAll();
            String selection = liste.getSelectedValue();
            destUser = Main.getUserByPseudo(selection);
            Main.tcpClient = new ClientTCP(destUser.IPAddress.getHostAddress(), 3070);
            Main.tcpClient.start();
            destLabel.setText("Recipient : "+destUser.pseudo);
            printHistory(user, destUser);
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
    public void connected_setup(){
        GridBagConstraints c = new GridBagConstraints();
        // Création du modèle de liste
        userListToPrint = new DefaultListModel();
        liste = new JList<String>(userListToPrint);
        // Ajout de les utilisateurs connectés avant nous 
        for (String user : Main.connectedPseudos) {
            userListToPrint.addElement(user);
        }       
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
        if(newPseudo!=null){
            user.change_pseudo(newPseudo);
            user.setFlag(Flag.PSEUDO_CHANGE);
             // Vérifie l'unicité
            while (!Main.getClientUDP().isUniquePseudoOnNetwork()){
              newPseudo = JOptionPane.showInputDialog(jFrame, "Pseudo already used, enter a new one : ");
              user.change_pseudo(newPseudo);
            }
            // Pseudo unique, connection autorisé
             user.setFlag(Flag.CONNECTED);
            // Met à jour l'interface
            pseudoLabel.setText("Pseudo : "+user.pseudo);
            sendPopUp("Pseudo successfully changed !");  
            msgCapture.setEditable(true);
        }else {
            sendPopUp("Please enter a pseudo");
            pseudoLabel.setText("Pseudo : Enter a pseudo to chat");
            msgCapture.setEditable(false);
        }    
    }


    public void changeDestinataireWindow() {
        //displayMsg.removeAll();
        JFrame jFrame = new JFrame();
        String newDest = JOptionPane.showInputDialog(jFrame, "Enter the recipient");            
        destUser=Main.getUserByPseudo(newDest);
        if (destUser==null){
            sendPopUp("This user doesn't exists");
        }

        // Si l'interface demande à parler 
        //  Ferme le server 3070
        //Main.getServerTCP().close();
        //Main.getServerTCP().yield();
        //  Lance le client sur 3070
        Main.tcpClient = new ClientTCP(destUser.IPAddress.getHostAddress(), 3070);
        Main.tcpClient.start();
        //  Lance le server sur 1111
        //Main.startTCPServer(1111);
        //Main.getServerTCP().sendTCPMsg(new Message("Heyo"));
        //Main.tcpClient.sendMessage(new Message("Heyo"));

        destLabel.setText("Recipient : "+destUser.pseudo);
        printHistory(user, destUser);
    }


    public void updateConnectedUserList(User new_user){
        userListToPrint.addElement(new_user.pseudo);
    }

    public void removeUserFromList(User user){
        if(user.flag==User.Flag.PSEUDO_CHANGE){
            userListToPrint.removeElement(user.oldPseudo);
        } else if (user.flag==User.Flag.DISCONNECTION){
            userListToPrint.removeElement(user.pseudo);
        }
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