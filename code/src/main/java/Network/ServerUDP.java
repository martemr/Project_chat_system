package Network;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import javax.swing.JOptionPane;

import Conversation.User;
import Conversation.User.Flag;
import Main.Main;

public class ServerUDP extends Thread {

    DatagramSocket receiveSocket;
    //byte[] incomingData;
    //DatagramPacket incomingPacket;

    final int sendPort=1400;
    final int receivePort=1400;

    public ServerUDP() throws IOException {
        //receiveSocket = new DatagramSocket(receivePort);
        //incomingData = new byte[65535];
        //incomingPacket = null;
    }

    public void closeServer(){
        if (receiveSocket != null)
            this.receiveSocket.close();
            System.out.println("[UDP Server] : Fermeture serveur");
    }

        
    public void sendBroadcast() {
        DatagramSocket sendSocket = null;
        User user = Main.getMainUser();
        try {
            sendSocket = new DatagramSocket();
            sendSocket.setBroadcast(true); // Send a broadcast
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(user);
            byte[] outgoingData = outputStream.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(outgoingData, outgoingData.length, user.IPAddressBroadcast, sendPort);
            sendSocket.send(sendPacket);
        } catch (IOException e) {
            System.out.println("[UDP Server] Error when sending a broadcast");
            e.printStackTrace();
        } finally {
            if (sendSocket != null)
                sendSocket.close();
            System.out.println("[UDP Server] Broadcast message sent from " + user.pseudo + " on " + user.IPAddressBroadcast.toString() + ":" + sendPort);
        }
    }

    public void sendUnicast(User userToSend, User recipient, User.Flag flag){
        userToSend.setFlag(flag);
        DatagramSocket sendSocket = null;
        try {
            sendSocket = new DatagramSocket();
            sendSocket.setBroadcast(false);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(userToSend); // Answer with my user
            byte[] outgoingData = outputStream.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(outgoingData, outgoingData.length, recipient.IPAddress, sendPort);
            sendSocket.send(sendPacket);
        } catch (IOException e) {
            System.out.println("[UDP Server] Error while sending unicast to " + userToSend.pseudo + "on" + userToSend.IPAddress.getHostAddress());
            e.printStackTrace();
        } finally {
            if (sendSocket != null)
                sendSocket.close();
            System.out.println("[UDP Server] Message sent to " + recipient.pseudo + " on " + recipient.IPAddressBroadcast.toString() + ":" + sendPort + " User:" + userToSend.pseudo +" - FLAG:" + userToSend.flag);
        }
    }

    public void notifyPseudoOnNetwork(){
        System.out.println("[UDP Server] Sending broadcast");
        sendBroadcast(); // Send the broadcast 
    }
     
    @Override
    public void run() {
        User main_user=Main.getMainUser();
        User new_user;
        try{
            receiveSocket = new DatagramSocket(receivePort);
            while (true)
            {
                /* Wait a cast */
                byte[] incomingData = new byte[65535];
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                System.out.println("[UDP Server] Wait on port " + receiveSocket.getLocalPort());
                receiveSocket.receive(incomingPacket);
                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);

                /* When smth received */
                // Désencapsule le user
                new_user = (User) is.readObject();
                System.out.println("[UDP Server] Receive : " + new_user.pseudo + " - FLAG:" + new_user.flag);
                
                // Vérifie que c'est pas soi même
                if (new_user.id != main_user.id){ // Case message from an other user
                    //Si c'est une connexion ou une demande de changement de pseudo
                    if(new_user.flag==User.Flag.PSEUDO_CHANGE){ 
                            // Pseudo identique au sien = répond avec un signal d'erreur
                        if (new_user.pseudo.equals(main_user.pseudo)){
                            // Renvoie le même user avec le flag PSEUDO_NOT_AVAILABLE
                            //Main.re(new_user);
                            sendUnicast(new_user, new_user, Flag.PSEUDO_NOT_AVAILABLE);
                        // Pseudo différent = répond avec son user
                        } else {
                            System.out.println("[UDP Server] " + new_user.pseudo + " just joined");
                            // Met à jour les tableaux d'utilisateurs et affiche dans l'interface                            
                            if (!Main.isNew(new_user)){// Changement de pseudo d'un utilisateur existant
                                Main.changePseudoUser(new_user);
                            } else {// Nouvel utilisateur 
                                Main.addNewUser(new_user);
                                if (Main.mainWindow.isInConversation(new_user)){
                                    Main.mainWindow.updateConversationPseudo();
                                }
                            }
                            // Renvoie son user
                            sendUnicast(main_user, new_user, Flag.CONNECTED);
                            if (!new_user.oldPseudo.equals(""))  {
                                Main.mainWindow.sendPopUp(new_user.oldPseudo+" is now "+new_user.pseudo);
                            }
                        }
                    } else if (new_user.flag==User.Flag.CONNECTED){
                        if(Main.isNew(new_user)){ //si new_user n'est pas encore dans notre vecteur d'utilisateurs
                            Main.addNewUser(new_user);
                        }
                    } else if (new_user.flag==User.Flag.DISCONNECTION){
                        //if (Main.mainWindow.destUser.id==new_user.id){ // We were talking to him
                        //    
                        //}
                        Main.removeUser(new_user);
                        System.out.println("[UDP Server] " + new_user.pseudo + " just left");
                        Main.mainWindow.sendPopUp(new_user.pseudo + " left :(");
                    } else if (new_user.flag==User.Flag.INIT_CONVERSATION){
                        // Ask to the user if he wants to start a conversation
                        int answer = JOptionPane.showConfirmDialog(null, new_user.pseudo + " wants to talk with you. Agree ? ");
                        if (answer==JOptionPane.YES_OPTION){
                            // Answer by starting a connection TCP
                            Main.startTCPClient(new_user.IPAddress.getHostAddress(), 2051);
                            Main.mainWindow.activeConversation(new_user);
                        }else {
                            sendUnicast(main_user, new_user, Flag.REFUSE_CONVERSATION);
                        }
                    } else if (new_user.flag==User.Flag.REFUSE_CONVERSATION){
                        Main.mainWindow.sendPopUp(new_user.pseudo + " doesn't want to talk with you :( Sorry");
                        Main.mainWindow.close_conversation();
                    } else if (new_user.flag==User.Flag.CLOSE_CONVERSATION){
                        Main.mainWindow.sendPopUp(new_user.pseudo + " left conversation");
                        Main.mainWindow.close_conversation();
                    }
                    receiveSocket.setSoTimeout(0); // Set as infinite
                } else { // Case answer to my message
                    switch (new_user.flag) {
                        case PSEUDO_CHANGE : // This is my message, I'm triing to connect
                            receiveSocket.setSoTimeout(100); // set a timeout for getting an answer
                            break;
                        case PSEUDO_NOT_AVAILABLE :
                            Main.clearListUser();
                            Main.mainWindow.raisePseudoAlreadyUsed();
                            break;
                        case DISCONNECTION :
                        
                    }
                    // Vérifie son flag pour savoir si il est déja utilisé
                    if (new_user.flag==Flag.PSEUDO_CHANGE) {
                        // That means that i'm triying to connect
                        receiveSocket.setSoTimeout(100);
                    } else if (new_user.flag==Flag.PSEUDO_NOT_AVAILABLE) {
                        
                    } else if (new_user.flag==Flag.DISCONNECTION) { // This is my message
                        // I'm leaving
                        break;
                    } else {
                        //receiveSocket.setSoTimeout(0); // Set as infinite
                        System.out.println("[UDP Server] Unknow flag received : " + new_user.flag);
                    }
                }
            }
        } catch (SocketTimeoutException e) {
            System.out.println("[UDP Server] Timeout : Nobody is on the network");
            receiveSocket.close();
            this.run();
        } catch (ClassNotFoundException e) {
            System.out.println("[UDP Server] Error when desencapsulating the user received");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[UDP Server] Error while receveing a broadcast");
            e.printStackTrace();
        }
    }
}