/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.model.sensors;

import it.unibo.aswProject.libraries.http.HTTPClient;
import it.unibo.aswProject.libraries.xml.ManageXML;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class SensorManager implements ISensorListener {
    private static SensorManager instance;
    
    private HashMap<Integer,Sensor> sensorList;
    private HTTPClient hc;
    private String BASE = "http://localhost:8080/SensorsServer/";
    
    private SensorManager(){
        sensorList= new HashMap<>();
        
        for(int cont =0; cont <5; cont++){
            sensorList.put(cont, new Sensor(this, cont));
        }
        
        hc = new HTTPClient();
        try {
            hc.setBase(new URL(BASE));
        } catch (MalformedURLException ex) {
            Logger.getLogger(SensorManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.startSensors();
    }
    
    public static SensorManager getInstance(){
        if (instance == null)
        {
            instance = new SensorManager();
        }

        return instance;  
    }
    
    public void addSensor(Sensor s){
        sensorList.put(s.getNumber(), s);
    }
    
    public Sensor getSensor(int num){
        return sensorList.get(num);
    }

    public HashMap<Integer, Sensor> getSensorList() {
        return sensorList;
    }
    
    public synchronized void startSensors(){
        for (Sensor s : this.sensorList.values()) {
            s.start();
        }
    }
    
    public synchronized void stopSensors(){
        for (Sensor s : this.sensorList.values()) {
            s.dispose();
        }
    }

    @Override
    public void update(Sensor s) {
        ManageXML mngXML= null;
        
        try {
            mngXML = new ManageXML();
        } catch (TransformerConfigurationException | ParserConfigurationException ex) {
            Logger.getLogger(SensorManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Document req = mngXML.newDocument("Notify");
        req.getDocumentElement().setAttribute("from", "sensor");
        req.getDocumentElement().setAttribute("number", Integer.toString(s.getNumber()));
        req.getDocumentElement().setAttribute("kind", "value");
        Element msg = req.createElement("message");
        msg.appendChild(req.createTextNode(Integer.toString(s.getValue())));
        req.getDocumentElement().appendChild(msg);
        
        Document answer;
        try {
            answer = hc.execute("Sensors",  req);
            //mngXML.transform(System.out, answer);
        } catch (TransformerException | ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(SensorManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        //System.out.println("Notify");
    }
    
}
