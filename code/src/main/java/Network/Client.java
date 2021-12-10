package Network;

import java.io.*;
import java.net.*;
/** Le processus client se connecte à l'adresse fournie dans la commande
 *   d'appel en premier argument et utilise le port distant 8080.
 */
public class Client {
   static final int port = 8080;

   public static void main(String[] args) throws Exception {
        Socket socket = new Socket(args[0], port);
        System.out.println("SOCKET = " + socket);

        BufferedReader buf = new BufferedReader(
                               new InputStreamReader(socket.getInputStream())
                               );

        PrintWriter print = new PrintWriter(
                             new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream())),
                             true);

        String str = "bonjour";
        for (int i = 0; i < 10; i++) {
           print.println(str);          // envoi d'un message
           str = buf.readLine();      // lecture de l'écho
        }
        
        System.out.println("END");     // message de terminaison
        print.println("END") ;
        buf.close();
        print.close();
        socket.close();
   }
}
