package Main;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.*;
import Main.Main;
import GUI.User;


public class Tools {

    public static void lire_config_xml(){ 
        try {
            //Obtenir la configuration du sax parser
            SAXParserFactory spfactory = SAXParserFactory.newInstance();
            //Obtenir une instance de l'objet parser
            SAXParser saxParser = spfactory.newSAXParser();
     
            /*les trois méthodes sont déclarées dans le
             corp du DefaltHandler*/
            DefaultHandler handler = new DefaultHandler() {
                boolean ip_local = false;
                boolean ip_broadcast = false;
         
                /*cette méthode est invoquée à chaque fois que parser rencontre
                  une balise ouvrante '<' */
                public void startElement(String uri, String localName,String qName,Attributes attributes) throws SAXException{
                   if (qName.equalsIgnoreCase("ip_local")) {
                     ip_local = true;
                   } else if (qName.equalsIgnoreCase("ip_broadcast")) {
                     ip_broadcast = true;
                   }
                }
         
                /*cette méthode est invoquée à chaque fois que parser rencontre
                  une balise fermante '>' */
                public void endElement(String uri, String localName,String qName) throws SAXException {    
                   if (qName.equalsIgnoreCase("ip_local")) {
                     ip_local = false;
                   } else if (qName.equalsIgnoreCase("ip_broadcast")) {
                     ip_broadcast = false;
                   }
                }
        
                public void characters(char ch[], int start,int length) throws SAXException {
                   if (ip_local) {
                    Main.getMainUser().set_ip_broadcast(new String(ch, start, length));
                     ip_local = false;
                   } else if (ip_broadcast) {
                     Main.getMainUser().set_ip_broadcast(new String(ch, start, length));
                     ip_broadcast = false;
                   }
                }
            };
          saxParser.parse("config.xml", handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

