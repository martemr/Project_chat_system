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
   
	// Constructor for server
	public ServerTCP(int port) throws IOException {
    	serverSocket = new ServerSocket(port);
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
	}

	/** Run a server TCP, waiting messages for the client */
    public void run() {

		// Initialise the server
		try{
			this.init();
		} catch (IOException e) {
			System.out.println("Error on Server TCP init");
			e.printStackTrace();
		}
        
		// Receive and print the message
		while(true) {
			try {
				ObjectInputStream in = new ObjectInputStream(server.getInputStream());
				Object received = in.readObject(); // Read the socket
				Message msg = (Message)received; // Convert the object receive into Message
				Interface.printMessage(msg); // Print it on interface
			} catch (ClassCastException e){
				System.out.println("[TCP Server] Received something that is not a Message");
				this.close();
				e.printStackTrace();
			} catch (EOFException eof){
				this.close();
				System.out.println("[TCP Server] Successfully closed");
				break;
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
			server.close();
		} catch (Exception e) {
			System.out.println("[TCP Server] Error while closing server");
			e.printStackTrace();
		}
	}

}
