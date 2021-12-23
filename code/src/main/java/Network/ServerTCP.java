package Network;

import java.io.*;
import java.net.*;

import Conversation.Message;
import GUI.*;

public class ServerTCP extends Thread {
   
	private ServerSocket serverSocket; // Socket Server
	protected int port;
	protected Socket server;
   
	// Constructor for server
	public ServerTCP(int port) throws IOException {
    	serverSocket = new ServerSocket(port);
    	//serverSocket.setSoTimeout(10000); // Timeout for connection
    }

	/* Initialise le server TCP : Se connecte au client.
	**/
	public void init() throws IOException {
		port = serverSocket.getLocalPort();
		System.out.println("[TCP Server] Waiting for client on port " + port + "...");
		server = serverSocket.accept(); // Wait for the client to connect
		System.out.println("[TCP Server] Successfully connected to " + server.getRemoteSocketAddress());	
	}

    public void run() {
        while(true) {
        	try {
				DataInputStream in = new DataInputStream(server.getInputStream());
				while (true) {
					//System.out.println(in.readUTF());
					Interface.printMessage(in.readUTF());
				}
			} catch (EOFException eof){
				this.close();
				System.out.println("[TCP Server] Successfully closed");
				break;
        	} catch (IOException e) {
        	    e.printStackTrace();
        	    break;
        	}
        }
    }

	public void close(){
		try {
			server.close();
		} catch (Exception e) {
			System.out.println("[TCP Server] Error while closing server");
			e.printStackTrace();
		}
	}

}
