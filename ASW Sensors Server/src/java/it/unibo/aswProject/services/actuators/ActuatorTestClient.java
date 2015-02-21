/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.services.actuators;

import it.unibo.aswProject.libraries.http.HTTPClient;
import it.unibo.aswProject.libraries.xml.ManageXML;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Thomas
 */
public class ActuatorTestClient {

    static HTTPClient hc = new HTTPClient();
    static final String BASE = "http://localhost:8080/SensorsServer/";
    
    public static void main(String[] args) throws TransformerConfigurationException, ParserConfigurationException, MalformedURLException, TransformerException, IOException, SAXException {
        
        ManageXML mngXML = new ManageXML();
        
        hc.setBase(new URL(BASE));

        Document answer = hc.execute("actuators",  mngXML.newDocument("getActuators"));
        mngXML.transform(System.out, answer);
        System.in.read();
    }
    
}
