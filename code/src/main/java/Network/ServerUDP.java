package Network;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import GUI.*;
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

    public void changePseudoReceived(User received_user, int old_user_index) {
            //Main.connectedUsers.remove(old_user_index);
            Main.connectedUsers.remove(received_user);
            Main.connectedUsers.add(received_user);

            
        //if (!received_user.pseudo.equals(Main.connectedUsers.get(old_user_index).pseudo)){
            // Received a changement of pseudo user
            

            // TODO : Notify interface
        
    }


    private boolean isNew(User new_user){
        for (int i = 0; i <Main.connectedId.length; i++){
            if (Main.connectedId[i]==new_user.id){
                return false;
            }
        }
        return true;
    }

    public void sendUnicast(User userToSend, User recipient) throws Exception{
        socket.setBroadcast(false);
        outputStream = new ByteArrayOutputStream();
        os = new ObjectOutputStream(outputStream);
        os.writeObject(userToSend); // Answer with my user
        byte[] outgoingData = outputStream.toByteArray();
        DatagramPacket replyPacket = new DatagramPacket(outgoingData, outgoingData.length, recipient.IPAddress, 1234);
        socket.send(replyPacket);
        System.out.println("[UDP Server] Answer message sent");
    }

    public void notifyPseudoNotAvailable(User new_user) throws Exception{
        new_user.setFlag(User.Flag.PSEUDO_NOT_AVAILABLE);
        sendUnicast(new_user, new_user);
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

                    //new_user.IPAddress = incomingPacket.getAddress();
                    if (new_user.id != Main.getMainUser().id){
                        System.out.println("[UDP Server] " + new_user.pseudo + " just joined");
                        
                        if (!isNew(new_user)){ // Vérifie si l'utilsateur est dejà sur le réseau
                            if (Main.isPseudoFree(new_user.pseudo)){ // Vérifie si le pseudo est dispo
                                changePseudoReceived(new_user, Main.connectedUsers.indexOf(new_user));
                                Main.updateConnectedUsers();
                            } else {
                                notifyPseudoNotAvailable(new_user);
                            }
                        } else { // Nouvel utilisateur
                            if (Main.isPseudoFree(new_user.pseudo)){ // Vérifie si le pseudo est dispo
                                Main.connectedUsers.add(new_user);
                                Main.updateConnectedUsers();
                                sendUnicast(Main.getMainUser(), new_user);
                            } else {
                                notifyPseudoNotAvailable(new_user);
                            }
                        }
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