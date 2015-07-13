/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.model;

import asw1030.dal.IXMLTable;
import asw1030.dal.XMLTable;
import asw1030.beans.Sensor;
import com.sun.faces.util.CollectionsUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Thomas
 */
public class SensorModel {
        
    private static SensorModel instance;
    private HashMap<Integer,Sensor> sensors;
    private IXMLTable<Sensor> dal;
    private static final Object locker= new Object();
    
    
    public static SensorModel getInstance(ServletContext servletContext){
        synchronized(locker){
            if (instance == null) {
                    instance = new SensorModel(servletContext);
            }
            return instance;
        }
    }
    private SensorModel(ServletContext servletContext) {
        try {
            dal = XMLTable.getInstance(servletContext.getRealPath("/")+ "WEB-INF/xml/sensors.xml");
        } catch (JAXBException | TransformerConfigurationException | ParserConfigurationException | IOException | SAXException ex) {
            Logger.getLogger(SensorModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        sensors= dal.fetchRecords();
        
    }
    
    public synchronized int addSensor(Sensor s){
        int index = dal.addRecord(s);
        sensors.put(index, s);
        
        return index;
    }
    
    public synchronized void removeSensor(int id){
        dal.removeRecord(id);
        sensors.remove(id);
    }
    
    public synchronized Sensor getSensor(int id)
    {
        return sensors.get(id);
    }
}
