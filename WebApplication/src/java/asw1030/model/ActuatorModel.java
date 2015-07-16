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
    //private IXMLTable<Actuator> dal;
    private final static Object locker= new Object();
    
    private HashMap<Integer,Actuator> actuators;
    
    private final ArrayList<IModelEventsListener> listeners;

    private int index;
    
    public static ActuatorModel getInstance(ServletContext servletContext) throws JAXBException{
        synchronized(locker){
            if (instance == null) {
                    instance = new ActuatorModel(servletContext);
            }
            return instance;
        }
    }
    
    public ActuatorModel(ServletContext servletContext) throws JAXBException {
//        try {
//            dal =new XMLTable<Actuator>(servletContext.getRealPath("/")+ "WEB-INF/xml/actuators.xml");
//        } catch (TransformerConfigurationException | ParserConfigurationException | IOException | SAXException ex) {
//            Logger.getLogger(SensorModel.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        //actuators= dal.fetchRecords();
        actuators= new HashMap<>();
        listeners = new ArrayList<>();
        index=0;
    }

    public List<Actuator> getActuators() {
        return new ArrayList<>(actuators.values());
    }
    
    public int addActuator(Actuator act)
    {
        //int index = dal.addRecord(act);
        
        act.setId(index);
        actuators.put(index++, act);
        
        listeners.stream().forEach((listener) -> {
            listener.modelEventHandler(ModelEventType.ACTUATORADDED, index);
        });
        
        act.addListener(this);
        
        return index;
    }
    
    public void removeActuator(int id){
        //dal.removeRecord(id);
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

    public synchronized void setActuatorValue(int id, int val){
        actuators.get(id).setValue(val);
        
        listeners.stream().forEach(list-> {list.modelEventHandler(ModelEventType.NEWACTUATORVALUE, val);});
    }
    
    @Override
    public void newEvent(ActuatorEventType ae, Object arg) {
        switch(ae){
            case NEWVALUE:
                listeners.stream().forEach(list-> {list.modelEventHandler(ModelEventType.NEWACTUATORVALUE, arg);});break;
        }
    }
}
