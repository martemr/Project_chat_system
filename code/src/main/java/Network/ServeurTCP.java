package Network;

import java.io.*;
import java.net.*;
import Conversation.Message;
import GUI.Interface;
import GUI.User;

public class ServeurTCP implements Runnable {

	private ServerSocket serverSocket = null;
	private Socket chatSocket;
	private Interface inter;
	private boolean running = true;
	@SuppressWarnings("unused")
//	private static History history;
	private User user;
	private int port;

	public ServeurTCP (Interface inter) throws IOException {	
		this.inter = inter;
	// TODO :	this.history = History.getInstance();
		this.serverSocket = new ServerSocket(port);
	//TODO :	this.inter.getUser().setPort(this.serverSocket.getLocalPort());
	}

	public void terminate() throws IOException {
		running = false;
		this.serverSocket.close();
	}
	
	public void run() {
		try {
			while (running) {
				System.out.println("[TCP] " + user.pseudo + " is listening by TCP at port " + Integer.toString(port) + "...");
				this.chatSocket = this.serverSocket.accept();

				/* Receive the message */
				InputStream input = chatSocket.getInputStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(input));
				String msg = in.readLine();

				System.out.println(msg);
                //TODO : Interface client = new Interface(new User(chatSocket.getInetAddress().getHostAddress()));
				/* Write the message on the chat window between this inter and client */
				//Interface client = new Interface (new User(chatSocket.getInetAddress().getHostAddress()));
                if (msg.charAt(0) == "[".charAt(0)) {
                    /* Write the message on the chat window between this inter and client */

                    String seg[] = msg.split(":");
                    System.out.println("TCP Server seg[0] :" +seg[0] );
                    System.out.println("TCP Server seg[1] :" +seg[1] );
                    System.out.println("TCP Server seg[2] :" +seg[2] );
                    System.out.println("TCP Server seg[3] :" +seg[3] );
                   
                  /* client.getUser().setPseudo(seg[2]);
                    System.out.println("TCP Server run client.getUser.getHost" + client.getUser().getHost());
                    if (!this.inter.getChatWindowForUser(client.getUser().getHost()).isVisible()) {
                        this.inter.updateOnlineList(new User(client.getUser().getHost(),client.getUser().getPseudo(),  true));
                        this.inter.updateHome();
                    }

                    if (message != null) {
                        this.inter.getChatWindowForUser(client.getUser().getHost()).write(seg[0] + seg[1]);
                        this.inter.getChatWindowForUser(client.getUser().getHost()).setTitle(client.getUser().getPseudo() + ": Chat");
                        System.out.println(seg[0] + seg[1]);
                    }
                } else {
                    String seg[] = message.split(":", 4);
                    System.out.println(seg[3]);
                    this.inter.getChatWindowForUser(client.getUser().getHost()).write("[" + seg[1] + "]" + " " + seg[3]);
                    BufferedImage img = decodeToImage(seg[0]);

                    initializeElements(seg[3], seg[1], img, seg[2]);

                }*/
			
			}
		}
	}
		catch (IOException e) {
            e.printStackTrace();
        }
	}
}