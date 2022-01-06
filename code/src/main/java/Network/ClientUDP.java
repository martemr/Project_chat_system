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
    InetAddress IPAddress;
    byte[] incomingData;
    User user;

    public ClientUDP() throws IOException {
        socket = new DatagramSocket();
        IPAddress = InetAddress.getByName("10.1.255.255");
        incomingData = new byte[1024];
        user = Main.getMainUser();
    }
    
    /** Function who will send the user as broadcast */
    public void sendBroadcast(){
        try{
            socket.setBroadcast(true); // Send a broadcast
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(user);
            incomingData = outputStream.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(incomingData, incomingData.length, IPAddress, 1234);
            socket.send(sendPacket);
            System.out.println("[UDP Client] Broadcast message sent");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
       
            /*
            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            Socket.receive(incomingPacket);
            String response = new String(incomingPacket.getData());
            System.out.println("Response from server:" + response);
            Thread.sleep(2000);
            */
           

/*
        // Create the socket object for carrying the data.
        DatagramSocket datagramReceiveSocket = new DatagramSocket();
    
        byte buf[] = null;
        buf = "Hello World !".getBytes(); // convert the String input into the byte array.

        // Create the datagramPacket for sending the data.
        InetAddress ip = InetAddress.getLocalHost(); // InetAddress.getHostbyname("localhost");
        DatagramPacket broadcastPacket = new DatagramPacket(buf, buf.length, ip, 1234);

        // invoke the send call to actually send the data.
        datagramReceiveSocket.send(broadcastPacket);
*/
