package Network;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import GUI.User;


public class ServerUDP extends Thread {

    DatagramSocket socket;
    byte[] incomingData;
    DatagramPacket incomingPacket;

    public ServerUDP(int port) throws IOException {
        socket = new DatagramSocket(1234);
        incomingData = new byte[65535];
        incomingPacket = null;
    }

    public void run() {
        try{
            System.out.println("[UDP Server] Starting server UDP ");
            while (true)
            {
                incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);
                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
                try {
                    User user = (User) is.readObject();
                    System.out.println("[UDP Server] Broadcast received from " + user.pseudo);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
            /*
            InetAddress IPAddress = incomingPacket.getAddress();
            int port = incomingPacket.getPort();
            String reply = "Thank you for the message";
            byte[] replyBytea = reply.getBytes();
            DatagramPacket replyPacket =
            new DatagramPacket(replyBytea, replyBytea.length, IPAddress, port);
            socket.send(replyPacket);
            Thread.sleep(2000);
            System.exit(0);
            }
            // Step 2 : create a DatgramPacket to receive the data.
            DpReceive = new DatagramPacket(receive, receive.length);
            
            // Step 3 : revieve the data in byte buffer.
            ds.receive(DpReceive);
            
            System.out.println("Client:-" + data(receive));
            
            // Clear the buffer after every message.
            receive = new byte[65535];
        }
        */  