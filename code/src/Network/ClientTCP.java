package Network;

import Conversation.*;

import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.*;

public class ClientTCP extends Thread {

	private final AtomicBoolean running = new AtomicBoolean(false); // For stopping the thread

	protected int port;
   protected String serverName;
   protected Socket client;
   ObjectOutputStream out ;

   /** Constructor for Client 
    *  Se connecte au server sur le port donné
    */
   public ClientTCP(String serverName, int port) { // serverName est l'adresse ip du destinataire
      // Définit les paramètres
      this.serverName = serverName; this.port = port;
      // Initialise la connection
      try {
         System.out.println("[TCP Client] Connecting to " + serverName + " on port " + port);
         client = new Socket(serverName, port); // Create socket
			out = new ObjectOutputStream(client.getOutputStream());
         System.out.println("[TCP Client] Connected");
      } catch (IOException e){
         System.out.println("[TCP Client] Error in socket creation");
         e.printStackTrace();
      } 
   }

   /** Thread qui tourne pour recevoir un message de la connexion établie */
   @Override
   public void run() {
      running.set(true);

	   ObjectInputStream in;
		try {
			in  = new ObjectInputStream(client.getInputStream());
			while(running.get()) {		
            Message msg = (Message)in.readObject(); // Convert the object receive into Message
            System.out.println("[TCP Server] Received a message from " + msg.to + msg.msg);
            Main.Main.mainWindow.printMessage(msg); // Print it on interface
         }
      } catch (EOFException e) {
      } catch (SocketException e) {
         this.close();
         //Main.Main.mainWindow.removeUserFromList();
      } catch (Exception e) {
         e.printStackTrace();
      } 
      
   }

   /** Envoie un message */
	public void sendMessage(Message msg) {     
      try {
         System.out.println("send message Client" + msg.to + ":" + msg);
         out.flush();
         out.writeObject(msg);
      } catch (IOException e) {
         System.out.println("[TCP Client] Error while sending message");
         e.printStackTrace();
         this.close();
      }
   }


   /** Closing function for the server */
	public void close(){
		try {
         running.set(false);
			client.close();
		} catch (Exception e) {
			System.out.println("[TCP Server] Error while closing server");
			e.printStackTrace();
		}
	}
}
