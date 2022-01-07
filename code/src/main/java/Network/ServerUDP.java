package Network;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import GUI.User;
import Main.Main;


public class ServerUDP extends Thread {

    DatagramSocket socket;
    byte[] incomingData;
    DatagramPacket incomingPacket;
    ByteArrayOutputStream outputStream;
    ObjectOutputStream os;

    public ServerUDP(int port) throws IOException {
        socket = new DatagramSocket(port);
        incomingData = new byte[65535];
        incomingPacket = null;
    }

    public void checkPseudoChangement(User received_user, int old_user_index) {
        if (!received_user.pseudo.equals(Main.connectedUsers.get(old_user_index).pseudo)){
            // Received a changement of pseudo user
            Main.connectedUsers.remove(old_user_index);
            Main.connectedUsers.add(received_user);

            // TODO : Notify interface
        }
    }

    public void run() {
        User new_user;
        try{
            while (true)
            {
                // Wait for receiving a broadcast
                incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);
                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
                try {
                    new_user = (User) is.readObject();
                    Main.connectedUsers.add(new_user);
                    //new_user.IPAddress = incomingPacket.getAddress();
                    System.out.println("[UDP Server] " + new_user.pseudo + "just joined");

                    // Answer only if it's a broadcast
                    if (!Main.connectedUsers.contains(new_user)){
                        socket.setBroadcast(false);
                        outputStream = new ByteArrayOutputStream();
                        os = new ObjectOutputStream(outputStream);
                        os.writeObject(Main.getMainUser()); // Answer with my user
                        byte[] outgoingData = outputStream.toByteArray();
                        DatagramPacket replyPacket = new DatagramPacket(outgoingData, outgoingData.length, new_user.IPAddress, 1234);
                        socket.send(replyPacket);
                        System.out.println("[UDP Server] Answer message sent");
                    } else {
                        checkPseudoChangement(new_user, Main.connectedUsers.indexOf(new_user));
                        System.out.println("[UDP Server] No answer send");
                    }


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}