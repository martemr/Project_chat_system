package Network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import GUI.User;
import Main.Main;

public class ClientUDP {

    DatagramSocket socket;
    byte[] outgoingData;
    User user;

    public ClientUDP() throws IOException {
        socket = new DatagramSocket();
        outgoingData = new byte[1024];
        user = Main.getMainUser();
    }
    
    /** Function who will send the user as broadcast */
    public void sendBroadcast(){
        try{
            socket.setBroadcast(true); // Send a broadcast
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(user);
            outgoingData = outputStream.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(outgoingData, outgoingData.length, user.IPAddressBroadcast, 1234);
            socket.send(sendPacket);
            System.out.println("[UDP Client] Broadcast message sent from " + user.pseudo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}