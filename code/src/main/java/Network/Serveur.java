package Network;

import java.io.*;
import java.net.*;

public class Serveur implements Runnable {
   static final int port = 8080;
   int clientNumber = 0;

   public void run() {
      System.out.println("Hello, I am ");
      }

   public static void main(String[] args) throws Exception {
        ServerSocket s = new ServerSocket(port);
        Socket soc = s.accept();

        Thread t1;
        t1 = new Thread ("Thread #1");
        t1.start();


        // Un BufferedReader permet de lire par ligne.
        BufferedReader buf = new BufferedReader(
                               new InputStreamReader(soc.getInputStream())
                              );

        // Un PrintWriter possède toutes les opérations print classiques.
        // En mode auto-flush, le tampon est vidé (flush) à l'appel de println.
        PrintWriter print = new PrintWriter(
                             new BufferedWriter(
                                new OutputStreamWriter(soc.getOutputStream())), 
                             true);

        while (true) {
           String str = buf.readLine();          // lecture du message
           if (str.equals("END")) break;
           System.out.println("ECHO = " + str);   // trace locale
           print.println(str);                     // renvoi d'un écho
        }
        buf.close();
        print.close();
        soc.close();
        s.close();
   }
}
