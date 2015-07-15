/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.model;

import asw1030.beans.Actuator;
import asw1030.beans.enums.ActuatorEventType;
import asw1030.beans.interfaces.IActuatorEventsListener;
import asw1030.dal.IXMLTable;
import asw1030.dal.XMLTable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class ActuatorModel implements IActuatorEventsListener{
    private static ActuatorModel instance;
    private IXMLTable<Actuator> dal;
    private final static Object locker= new Object();
    private HashMap<Integer,Actuator> actuators;
    private final ArrayList<IModelEventsListener> listeners;

    public static ActuatorModel getInstance(ServletContext servletContext){
        synchronized(locker){
            if (instance == null) {
                    instance = new ActuatorModel(servletContext);
            }
            return instance;
        }
    }
    
    public ActuatorModel(ServletContext servletContext) {
        try {
            dal = XMLTable.getInstance(servletContext.getRealPath("/")+ "WEB-INF/xml/actuators.xml");
        } catch (JAXBException | TransformerConfigurationException | ParserConfigurationException | IOException | SAXException ex) {
            Logger.getLogger(SensorModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        actuators= dal.fetchRecords();
        listeners = new ArrayList<>();
    }

    public List<Actuator> getActuators() {
        return new ArrayList<>(actuators.values());
    }
    
    public int addActuator(Actuator act)
    {
        int index = dal.addRecord(act);
        actuators.put(index, act);
        
        listeners.stream().forEach((listener) -> {
            listener.modelEventHandler(ModelEventType.ACTUATORADDED, index);
        });
        
        act.addListener(this);
        
        return index;
    }
    
    public void removeActuator(int id){
        dal.removeRecord(id);
        actuators.remove(id);
        
        listeners.stream().forEach((listener) -> {
            listener.modelEventHandler(ModelEventType.ACTUATORREMOVED, id);
        });
    }
    
    public synchronized void addListener(IModelEventsListener list)
    {
        listeners.add(list);
    }
    
    public synchronized void removeListener(IModelEventsListener list)
    {
        listeners.remove(list);
    }

    public void setActuatorValue(int id, int val){
        actuators.get(id).setValue(val);
    }
    
    @Override
    public void newEvent(ActuatorEventType ae, Object arg) {
        switch(ae){
            case NEWVALUE:
                listeners.stream().forEach(list-> {list.modelEventHandler(ModelEventType.NEWACTUATORVALUE, arg);});break;
        }
    }
}
