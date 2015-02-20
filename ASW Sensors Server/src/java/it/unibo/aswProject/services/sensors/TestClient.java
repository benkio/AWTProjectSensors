/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.services.sensors;

import it.unibo.aswProject.libraries.http.HTTPClient;
import it.unibo.aswProject.libraries.xml.ManageXML;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Session;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

        Document answer = hc.execute("Sensors",  mngXML.newDocument("login"));
        mngXML.transform(System.out, answer);
        System.in.read();
        
        answer = hc.execute("Sensors",  mngXML.newDocument("GetSensors"));
        mngXML.transform(System.out, answer);
        System.in.read();
        
        new Thread(new Runnable(){                    
            @Override
            public void run() {
                while(true){
                    try {
                        mngXML.transform(System.out, hc.execute("Sensors",  mngXML.newDocument("GetValues")));
                    } catch (TransformerException | ParserConfigurationException | SAXException | IOException ex) {
                        Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        ).start();        
              
//        answer = hc.execute("Sensors", subscriptionRequest(1));
//        mngXML.transform(System.out, answer);
//        System.in.read();
        
//        answer = hc.execute("Sensors",  mngXML.newDocument("GetValues"));
//        mngXML.transform(System.out, answer);
        
    }
    
    static private Document subscriptionRequest(int snum) throws TransformerConfigurationException, ParserConfigurationException{
        ManageXML mxml= new ManageXML();
        Document request = mxml.newDocument("Subscribe");
        Element sensor = request.createElement("Sensor");
        sensor.appendChild(request.createTextNode("1"));
        request.getDocumentElement().appendChild(sensor);
        
        return request;
    }
    
}
