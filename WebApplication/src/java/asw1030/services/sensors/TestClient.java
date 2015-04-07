/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.services.sensors;

import asw1030.libraries.http.HTTPClient;
import asw1030.libraries.xml.ManageXML;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Thomas
 */
public class TestClient {

    static HTTPClient hc = new HTTPClient();
    static final String BASE = "http://localhost:8080/SensorsServer/";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws TransformerConfigurationException, ParserConfigurationException, TransformerException, SAXException, IOException {
        ManageXML mngXML = new ManageXML();
        
        hc.setBase(new URL(BASE));

        Document answer = hc.execute("Sensors",  mngXML.newDocument("testLogin"));
        mngXML.transform(System.out, answer);
        
        System.in.read();
        
        Thread t = new Thread(new Runnable() {
            public void run() {

                try {
                    while(true){
                        mngXML.transform(System.out, hc.execute("Sensors",  mngXML.newDocument("waitEvents")));
                        //mngXML.transform(System.out, hc.execute("Sensors",  mngXML.newDocument("getSensors")));
                    }
                } catch (TransformerException ex) {
                    Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SAXException ex) {
                    Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        t.start();
        System.in.read();
        t.stop();
        
    }    
}
