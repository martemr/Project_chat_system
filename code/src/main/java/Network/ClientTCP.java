package Network;

import Conversation.*;

import java.net.*;
import java.io.*;

public class ClientTCP {

	protected int port;
   protected String serverName;
   protected Socket client;

   /** Constructor for Client 
    *  Se connecte au server sur le port donné
    */
   public ClientTCP(String serverName, int port) { // serverName est l'adresse ip du destinataire
      this.serverName = serverName; this.port = port;

      System.out.println("[TCP Client] Connecting to " + serverName + " on port " + port);
      try {
         client = new Socket(serverName, port); // Create socket
         System.out.println("[TCP Client] Connected");
      } catch (IOException e){
         System.out.println("[TCP Client] Error in socket creation");
         e.printStackTrace();
      } 

      // Lance le server en ecoute de 3070
      Main.Main.startTCPServer(1111);
   }

   /** Envoie un message
	 * 
	**/
	public void sendMessage(Message msg) {     
      try {
         OutputStream outToServer = client.getOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(outToServer);
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
			client.close();
		} catch (Exception e) {
			System.out.println("[TCP Server] Error while closing server");
			e.printStackTrace();
		}
	}
}
