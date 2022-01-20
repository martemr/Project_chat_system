package Network;

import java.io.*;
import java.net.*;

import Conversation.Message;
import GUI.*;

/** TCP Server, use for exchange using tcp.
 * 
 */
public class ServerTCP extends Thread {
   
	private ServerSocket serverSocket; // Socket Server
	protected int port;
	protected Socket server;
   
	ObjectInputStream  in ;
	ObjectOutputStream out ;

	// Constructor for server
	public ServerTCP(int port) throws IOException {
    	serverSocket = new ServerSocket(port);
		// Initialise the server = attend une connection
		try{
			this.init();
		} catch (IOException e) {
			System.out.println("Error on Server TCP init");
			e.printStackTrace();
		}
    }

	/** Initialise le server TCP : Se connecte au client.
	 * 	Peut etre appelé que par la méthode run pour etre exécuté en background
	 *  @throws IOException
	**/
	private void init() throws IOException {
		port = serverSocket.getLocalPort();
		System.out.println("[TCP Server] Waiting for client on port " + port + "...");
		server = serverSocket.accept(); // Wait for the client to connect
		System.out.println("[TCP Server] Successfully connected to " + server.getRemoteSocketAddress());	
		try {
			out = new ObjectOutputStream(server.getOutputStream());
		}catch (Exception e){
			e.printStackTrace();
		} 
	}

	public void sendTCPMsg(Message msg){
		try{
			//while(null){}
			//System.out.println("Sending "+ msg);
			out.writeObject(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Run a server TCP, waiting messages for the client */
    public void run() {
		// A partir de là, connecté à l'autre machine
		// Quand quelqu'un s'est connecté au server : Lance un client en 3070
		//Main.Main.initTCPClient(server.getInetAddress().getHostAddress(), 3070);
        
		// Ecoute le 3070
		// Receive and print the message
		while(true) {
			try {
				in  = new ObjectInputStream(server.getInputStream());
				Message msg = (Message)in.readObject(); // Convert the object receive into Message
				sendTCPMsg(new Message("Bien recu"));
				//Message msg = (Message)in.readObject(); // Convert the object receive into Message
				System.out.println("[TCP Server] Received a message " + msg.msg);
				Interface.printMessage(msg); // Print it on interface
			} catch (ClassCastException e){
				System.out.println("[TCP Server] Received something that is not a Message");
				this.close();
				e.printStackTrace();
			} catch (EOFException eof){
				//this.close();
				//System.out.println("[TCP Server] Successfully closed");
				//break;
        	} catch (Exception e) {
				this.close();
				System.out.println("[TCP Server] Error on TCP Server running, closing server");
        	    e.printStackTrace();
				break;
        	}
        }
    }

	/** Closing function for the server */
	public void close(){
		try {
			if (server != null)
				server.close();
		} catch (Exception e) {
			System.out.println("[TCP Server] Error while closing server");
			e.printStackTrace();
		}
	}

}
