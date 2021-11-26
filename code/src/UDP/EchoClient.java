package code.UDP;

import java.net.*;

public class EchoClient {
    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf;

    public EchoClient() {
      try{
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
      } catch(Exception e){
        System.out.println("Error creation Socket\n");
      }
        
    }

    public String sendEcho(String msg) {
      String received="";
      try{
        buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);       
        socket.send(packet);
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        received = new String(packet.getData(), 0, packet.getLength());  
      } catch(Exception e){
        System.out.println("Error creation Socket\n");   
      }
      return received;
    }

    public void close() {
        socket.close();
    }
}