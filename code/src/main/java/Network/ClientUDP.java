package Network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import GUI.User;
import GUI.User.Flag;
import Main.Main;

public class ClientUDP {

    DatagramSocket socket;
    byte[] outgoingData;
    User user;

    final int sendPort=1400;
    final int receivePort=1450;

    /** Constructor */
    public ClientUDP() throws IOException {
        socket = new DatagramSocket();
        outgoingData = new byte[1024];
        user = Main.getMainUser();
    }
    
    public void sendBroadcast() throws IOException {
        socket.setBroadcast(true); // Send a broadcast
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(outputStream);
        os.writeObject(user);
        outgoingData = outputStream.toByteArray();
        DatagramPacket sendPacket = new DatagramPacket(outgoingData, outgoingData.length, user.IPAddressBroadcast, sendPort);
        socket.send(sendPacket);
        System.out.println("[UDP Client] Broadcast message sent from " + user.pseudo + " on " + user.IPAddressBroadcast.toString() + ":" + sendPort);
    }

    /**
     * Function who will try to change pseudo and check if it is already used
     * @return true if pseudo unique, false if the pseudo can't be taken by user
     */
    public boolean isUniquePseudoOnNetwork(){
        try {
            // Send the broadcast
            sendBroadcast();
            // Wait 1000ms to see answers on the network
            while (true)
            {
                // TODO : Ne fonctionne que pour 2 utilisateurs, à adapter avec multiples
                /* Wait an answer */
                DatagramSocket socket= new DatagramSocket(receivePort);
                byte[] incomingData= new byte[65535];
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);
                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);

                System.out.println("Wait for answer on port " + socket.getPort());
                // Désencapsule le user
                User new_user = (User) is.readObject();
                socket.close();
                System.out.println(new_user.pseudo + " Flag="+ new_user.flag.toString());
                // Vérifie son flag pour savoir si il est déja utilisé
                if (new_user.flag==Flag.CONNECTED){
                    Main.addNewUser(new_user);
                    return true;
                } else {
                    return false;
                }
            }
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