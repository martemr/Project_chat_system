package Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientUDP {


    public ClientUDP() throws IOException {
        // Create the socket object for carrying the data.
        DatagramSocket datagramReceiveSocket = new DatagramSocket();
    
        byte buf[] = null;
        buf = "Hello World !".getBytes(); // convert the String input into the byte array.

        // Create the datagramPacket for sending the data.
        InetAddress ip = InetAddress.getLocalHost(); // InetAddress.getHostbyname("localhost");
        DatagramPacket broadcastPacket = new DatagramPacket(buf, buf.length, ip, 1234);

        // invoke the send call to actually send the data.
        datagramReceiveSocket.send(broadcastPacket);

    }

}