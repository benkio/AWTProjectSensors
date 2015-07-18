/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import asw1030.libraries.http.HTTPClient;
import asw1030.libraries.xml.ManageXML;
import asw1030.model.ActuatorModel;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author Thomas
 */
public class ActuatorServiceTest {
    static HTTPClient hc;
    static final String BASE = "http://localhost:8080/SensorsServer/";
    static ManageXML mngXML;
    
    public ActuatorServiceTest() throws TransformerConfigurationException, ParserConfigurationException, MalformedURLException, TransformerException, SAXException, IOException {


        //Document answer = hc.execute("actuators",  mngXML.newDocument("testLogin"));
        //mngXML.transformIndented(System.out, answer);
    }
    
    @BeforeClass
    public static void setUpClass() throws TransformerConfigurationException, ParserConfigurationException, MalformedURLException {
        hc = new HTTPClient();
        mngXML = new ManageXML();
        hc.setBase(new URL(BASE));
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

     @Test
     public void getActuatorTest() throws TransformerException, IOException, ParserConfigurationException, SAXException {
        Document d = mngXML.newDocument("getActuators");
        
        mngXML.transformIndented(System.out, hc.execute("Actuators", d));
     }
     
    @Test
     public void setActuatorValueTest() throws TransformerException, IOException, ParserConfigurationException, SAXException {
        Document d = mngXML.newDocument("setValue");
        
        Element id = d.createElement("id");
        id.appendChild(d.createTextNode("0"));
        d.getDocumentElement().appendChild(id);
        
        Element value = d.createElement("value");
        value.appendChild(d.createTextNode("10"));
        d.getDocumentElement().appendChild(value);
        
        d.getDocumentElement().appendChild(id);
        d.getDocumentElement().appendChild(value);
        
        mngXML.transformIndented(System.out, hc.execute("Actuators", d));
     }
}
