/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import asw1030.beans.Sensor;
import asw1030.beans.Sensor.SensorKind;
import asw1030.libraries.http.HTTPClient;
import asw1030.libraries.xml.ManageXML;
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
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author Thomas
 */
public class SensorModelTest {
    
    static HTTPClient hc;
    static final String BASE = "http://localhost:8080/SensorsServer/";
    static ManageXML mngXML;
            
    public SensorModelTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws TransformerConfigurationException, ParserConfigurationException, MalformedURLException, TransformerException, SAXException, IOException {
        hc = new HTTPClient();
        mngXML = new ManageXML();
        
        hc.setBase(new URL(BASE));

        Document answer = hc.execute("Sensors",  mngXML.newDocument("testLogin"));
        mngXML.transform(System.out, answer);
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
    public void addSensorTest() throws TransformerException, IOException, ParserConfigurationException, SAXException{
        
        Document d = mngXML.newDocument("addSensor");
        Element sensor = d.createElement("sensor");
        Element kind = d.createElement("kind");
        kind.appendChild(d.createTextNode(SensorKind.GAS_PRESSURE.toString()));
        sensor.appendChild(kind);
        Element status = d.createElement("status");
        status.appendChild(d.createTextNode("Running"));
        sensor.appendChild(status);
        d.getDocumentElement().appendChild(sensor);
        
        mngXML.transform(System.out, hc.execute("Sensors", d));
    }
    
    @Test
    public void removeSensorTest() throws TransformerException, IOException, ParserConfigurationException, SAXException{
        
        Document d = mngXML.newDocument("removeSensor");
        Element sensorId = d.createElement("sensorId");
        sensorId.appendChild(d.createTextNode("1"));
        d.getDocumentElement().appendChild(sensorId);
        
        mngXML.transform(System.out, hc.execute("Sensors", d));
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}