package Network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

import GUI.User;
import GUI.User.Flag;
import Main.Main;

public class ClientUDP {

    User user;
    final int sendPort=1400;
    final int receivePort=1450;
    DatagramSocket receiveSocket;

    /** Constructor */
    public ClientUDP() {
        user = Main.getMainUser();
    }
    
    public void sendBroadcast() {
        try {
            DatagramSocket sendSocket = new DatagramSocket();
            byte[] outgoingData = new byte[1024];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            sendSocket.setBroadcast(true); // Send a broadcast
            os.writeObject(user);
            outgoingData = outputStream.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(outgoingData, outgoingData.length, user.IPAddressBroadcast, sendPort);
            sendSocket.send(sendPacket);
            sendSocket.close();
            System.out.println("[UDP Client] Broadcast message sent from " + user.pseudo + " on " + user.IPAddressBroadcast.toString() + ":" + sendPort);
        } catch (IOException e) {
            System.out.println("[UDP Client] Error when sending a broadcast");
            e.printStackTrace();
        }
        
    }

    /**
     * Function who will try to change pseudo and check if it is already used
     * @return true if pseudo unique, false if the pseudo can't be taken by user
     */
    public boolean isUniquePseudoOnNetwork(){
        int TIMEOUT = 100;
        try {
            // Send the broadcast
            sendBroadcast();
            
            // TODO : Ne fonctionne que pour 2 utilisateurs, à adapter avec multiples
            /* Wait an answer */
            receiveSocket= new DatagramSocket(receivePort);
            receiveSocket.setSoTimeout(TIMEOUT); // Timeout de 1 seconde pour le cas ou on est seul sur le réseau
            byte[] incomingData= new byte[65535];
            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            receiveSocket.receive(incomingPacket);
            byte[] data = incomingPacket.getData();
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);

            System.out.println("[UDP Client] Wait for answer on port " + receiveSocket.getPort());
            // Désencapsule le user
            User new_user = (User) is.readObject();
            receiveSocket.close();
            System.out.println(new_user.pseudo + " Flag="+ new_user.flag.toString());
            // Vérifie son flag pour savoir si il est déja utilisé
            if (new_user.flag==Flag.CONNECTED){ //si notre pseudo n'est pas utilisé par new_user
                if(Main.isNew(new_user)){ //si new_user n'est pas encore dans notre vecteur d'utilisateurs
                    Main.addNewUser(new_user);
                }
                return true;
            } else {
                return false;
            }

        } catch (SocketTimeoutException t){
            receiveSocket.close();
            return true;
        } catch (ClassNotFoundException e) {
            System.out.println("[UDP Client] Error when desencapsulating the user received");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println("[UDP Client] Error while exchanging for pseudo");
            e.printStackTrace();
            return false;
        }
    }
}