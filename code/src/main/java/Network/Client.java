package Network;

import java.io.*;
import java.net.*;
import Conversation.Message;

/** Le processus client se connecte à l'adresse fournie dans la commande
 *   d'appel en premier argument et utilise le port distant 8080.
 */
public class Client {
   static final int port = 8080;

    Socket outputSocket;
	OutputStream outputStream;
	ObjectOutputStream objectOutputStream;
	Message toSend;
	public boolean active = false;

    Client(Socket socket) throws IOException{
		outputSocket = socket;
		outputStream = outputSocket.getOutputStream();
		objectOutputStream = new ObjectOutputStream(outputStream);
		active = true;
	}

        void close() throws IOException{
            if(active){
                objectOutputStream.close();
                outputStream.close();
                outputSocket.close();
            }
            active = false;
        }
    
        synchronized void sendMessageObject(Message obj){
            if(active){
                try{
                    objectOutputStream.writeObject(obj);
                    objectOutputStream.flush();
                }catch(IOException e){
                    active =  false;
                    e.printStackTrace();
                }
            }
        }
   }