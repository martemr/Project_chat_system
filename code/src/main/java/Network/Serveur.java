package Network;

import java.io.*;
import java.net.*;
import Conversation.Message;

public class Serveur implements Runnable {
   static final int port = 8080;
   int clientNumber = 0;

   Socket inputSocket;
	InputStream inputStream;
	ObjectInputStream objectInputStream;

	volatile public boolean active = false;

	Serveur (Socket socket) {
		this.inputSocket = socket;
		active = true;
	}

	public void close(){
		try{
			this.close(false);
		}catch(Exception e){}
	}

	public void close(boolean force) throws IOException{
		System.out.println("Stopping receiver.");
		
		if(force && active){
			System.out.println("Closing streams..");
			objectInputStream.close();
			inputStream.close();
			inputSocket.close();
		}
		this.active = false;
	}
	
	public void run(){


		try{
			inputStream = inputSocket.getInputStream();
			objectInputStream = new ObjectInputStream(inputStream);


			while(active){

				Message object = (Message) objectInputStream.readObject();
	       		object.start();

			}	

			System.out.println("Closing streams..");
			objectInputStream.close();
			inputStream.close();
			inputSocket.close();		
			

		}catch(Exception e){
       		e.printStackTrace();
       	}
   }
}
