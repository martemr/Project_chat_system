package Network;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JOptionPane;
import Conversation.User;
import Conversation.User.Flag;
import GUI.Interface;
import Main.Main;
public class ServerUDP extends Thread {

	private final AtomicBoolean running = new AtomicBoolean(false); // For stopping the thread

    DatagramSocket receiveSocket;

    final int sendPort=1400;
    final int receivePort=1400;

    public ServerUDP() throws IOException {}

    public void closeServer(){
        System.out.println("[UDP Server] Closing server");
        running.set(false);
        if (receiveSocket != null)
            this.receiveSocket.close();
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
        running.set(true);

        User new_user;
        try{
            receiveSocket = new DatagramSocket(receivePort);
			while(running.get()) {

                /* Wait a cast */
                byte[] incomingData = new byte[65535];
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                System.out.println("[UDP Server] Wait on port " + receiveSocket.getLocalPort());
                try {
                    receiveSocket.receive(incomingPacket);
                } catch (SocketException e) {
                    break;
                }
                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
                /* When smth received */
                // D??sencapsule le user
                new_user = (User) is.readObject();
                System.out.println("[UDP Server] Receive : " + new_user.pseudo + " - FLAG:" + new_user.flag);
                
                // V??rifie que c'est pas soi m??me
                if (new_user.id != Main.getMainUser().id){ // Case message from an other user
                    //Si c'est une connexion ou une demande de changement de pseudo
                    if(new_user.flag==User.Flag.PSEUDO_CHANGE){ 
                            // Pseudo identique au sien = r??pond avec un signal d'erreur
                        if (new_user.pseudo.equals(Main.getMainUser().pseudo)){
                            // Renvoie le m??me user avec le flag PSEUDO_NOT_AVAILABLE
                            Main.removeUser(new_user);
                            sendUnicast(new_user, new_user, Flag.PSEUDO_NOT_AVAILABLE);
                        // Pseudo diff??rent = r??pond avec son user
                        } else {
                            System.out.println("[UDP Server] " + new_user.pseudo + " just joined");
                            // Met ?? jour les tableaux d'utilisateurs et affiche dans l'interface                            
                            if (!Main.isNew(new_user)){// Changement de pseudo d'un utilisateur existant
                                Main.changePseudoUser(new_user);
                                if (Main.mainWindow.isInConversation(new_user)){
                                    Main.mainWindow.updateConversationPseudo(new_user);
                                }
                            } else {// Nouvel utilisateur 
                                Main.addNewUser(new_user);
                            }
                            // Renvoie son user
                            sendUnicast(Main.getMainUser(), new_user, Flag.CONNECTED);
                            if (!new_user.oldPseudo.equals(""))  {
                                Interface.sendPopUp(new_user.oldPseudo+" is now "+new_user.pseudo);
                            }
                        }
                    } else if (new_user.flag==User.Flag.CONNECTED){
                        if(Main.isNew(new_user)){ //si new_user n'est pas encore dans notre vecteur d'utilisateurs
                            Main.addNewUser(new_user);
                        }
                    } else if (new_user.flag==User.Flag.DISCONNECTION){
                        Main.removeUser(new_user);
                        System.out.println("[UDP Server] " + new_user.pseudo + " just left");
                        Interface.sendPopUp(new_user.pseudo + " left :(");
                        Main.mainWindow.close_conversation();
                    } else if (new_user.flag==User.Flag.INIT_CONVERSATION){
                        // Ask to the user if he wants to start a conversation
                        int answer = JOptionPane.showConfirmDialog(null, new_user.pseudo + " wants to talk with you. Agree ? ");
                        if (answer==JOptionPane.YES_OPTION){
                            // Answer by starting a connection TCP
                            System.out.println("[Main] Starting client TCP");
                            Main.mainWindow.tcpClient = new ClientTCP(new_user.IPAddress.getHostAddress(), 2051);
                            Main.mainWindow.tcpClient.start();
                            Main.mainWindow.activeConversation(new_user);
                        }else {
                            sendUnicast(Main.getMainUser(), new_user, Flag.REFUSE_CONVERSATION);
                        }
                    } else if (new_user.flag==User.Flag.REFUSE_CONVERSATION){
                        Interface.sendPopUp(new_user.pseudo + " doesn't want to talk with you :( Sorry");
                        if (Main.mainWindow.tcpServer != null) {
                            Main.mainWindow.tcpServer.close();
                        }
                        if (Main.mainWindow.tcpClient != null) {
                            Main.mainWindow.tcpClient.close();
                        }
                        //Main.mainWindow.close_conversation();
                    } else if (new_user.flag==User.Flag.CLOSE_CONVERSATION){
                        Interface.sendPopUp(new_user.pseudo + " left conversation");
                        if (Main.mainWindow.tcpServer != null) {
                            Main.mainWindow.tcpServer.close();
                        }
                        if (Main.mainWindow.tcpClient != null) {
                            Main.mainWindow.tcpClient.close();
                        }
                        //Main.mainWindow.close_conversation();
                    }
                    receiveSocket.setSoTimeout(0); // Set as infinite
                } else { // Case answer to my message
                    // V??rifie son flag pour savoir si il est d??ja utilis??
                    if (new_user.flag==Flag.PSEUDO_CHANGE) {
                        // That means that i'm triying to connect
                        receiveSocket.setSoTimeout(100);
                    } else if (new_user.flag==Flag.PSEUDO_NOT_AVAILABLE) {
                        Main.clearListUser();
                        Interface.raisePseudoAlreadyUsed();
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