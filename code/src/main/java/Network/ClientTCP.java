package Network;

import java.io.*;
import java.net.*;
import Conversation.Message;
import GUI.User;

/** Le processus client se connecte à l'adresse fournie dans la commande
 *   d'appel en premier argument et utilise le port distant 8080.
 */
public class ClientTCP implements Runnable {
    private Socket chatSocket;
	private final Message message;
	private final String host;
	private PrintWriter output;
	private final int port = 8080;
    private User user;


	public ClientTCP(String host, Message message) throws IOException {
		this.host = host;
		this.message = message;
	}


	public void run() {
		try {
				/* Request a connection to the given User  */
				System.out.println("connecting to port " + port + " and host " + host);
				chatSocket = new Socket(host, port);
				/* Initialization of the output channel */
				this.output = new PrintWriter(chatSocket.getOutputStream());

				/* Send the message...*/
				output.println(message.toString() + ":" + user.pseudo + ":" + Integer.toString(port));
				output.flush();
				/* Close the socket */
				chatSocket.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}