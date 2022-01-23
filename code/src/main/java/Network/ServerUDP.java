package Network;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import GUI.*;
import GUI.User.Flag;
import Main.Main;

public class ServerUDP extends Thread {

    DatagramSocket receiveSocket;
    //byte[] incomingData;
    //DatagramPacket incomingPacket;

    final int sendPort=1450;
    final int receivePort=1400;

    public ServerUDP() throws IOException {
        //receiveSocket = new DatagramSocket(receivePort);
        //incomingData = new byte[65535];
        //incomingPacket = null;
    }

    public void closeServer(){
        if (receiveSocket != null)
            this.receiveSocket.close();
    }

    public void sendUnicast(User userToSend, User recipient){
        DatagramSocket sendSocket = null;
        try {
            sendSocket = new DatagramSocket();
            sendSocket.setBroadcast(false);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(userToSend); // Answer with my user
            byte[] outgoingData = outputStream.toByteArray();
            DatagramPacket replyPacket = new DatagramPacket(outgoingData, outgoingData.length, recipient.IPAddress, sendPort);
            sendSocket.send(replyPacket);
        } catch (IOException e) {
            System.out.println("[UDP Server] Error while sending unicast to " + userToSend.pseudo + "on" + userToSend.IPAddress.getHostAddress());
            e.printStackTrace();
        } finally {
            if (sendSocket != null)
                sendSocket.close();
            System.out.println("[UDP Server] Answer message sent to " + recipient.pseudo + " on " + recipient.IPAddressBroadcast.toString() + ":" + sendPort);
        }
    }
     
    @Override
    public void run() {
        User main_user=Main.getMainUser();
        User new_user;
        try{
            receiveSocket = new DatagramSocket(receivePort);
            while (true)
            {
                /* Wait a broadcast */
                byte[] incomingData = new byte[65535];
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                System.out.println("[UDP Server] Wait a broadcast on port " + receiveSocket.getLocalPort());
                receiveSocket.receive(incomingPacket);
                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);

                /* When broadcast received */
                // Désencapsule le user
                new_user = (User) is.readObject();
                // Vérifie que c'est pas soi même
                if (new_user.id != main_user.id){
                    //Si c'est une connexion ou une demande de changement de pseudo
                    if(new_user.flag==User.Flag.PSEUDO_CHANGE){ 
                            // Pseudo identique au sien = répond avec un signal d'erreur
                        if (new_user.pseudo.equals(main_user.pseudo)){
                            // Renvoie le même user avec le flag PSEUDO_ALREADY_USED
                            new_user.setFlag(Flag.PSEUDO_NOT_AVAILABLE);
                            sendUnicast(new_user, new_user);
                        // Pseudo différent = répond avec son user
                        } else {
                            System.out.println("[UDP Server] " + new_user.pseudo + " just joined");
                            // Met à jour les tableaux d'utilisateurs et affiche dans l'interface                            
                            if (!Main.isNew(new_user)){// Nouvel utilisateur
                                Main.changePseudoUser(new_user);
                            } else {// Changement de pseudo d'un utilisateur existant
                                Main.addNewUser(new_user);
                            }
                            // Renvoie son user
                            new_user.setFlag(Flag.CONNECTED);
                            sendUnicast(main_user, new_user);
                        }
                    } else if (new_user.flag==User.Flag.DISCONNECTION){
                        Main.mainWindow.removeUserFromList(new_user);
                        Main.mainWindow.sendPopUp(new_user + " left :(");
                    }                         
                }           
            }
        } catch (ClassNotFoundException e) {
            System.out.println("[UDP Server] Error when desencapsulating the user received");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[UDP Server] Error while receveing a broadcast");
            e.printStackTrace();
        }
    }
}