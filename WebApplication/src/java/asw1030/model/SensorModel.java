/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.model;

import asw1030.beans.enums.ModelEventType;
import asw1030.beans.Sensor;
import asw1030.beans.enums.SensorEventType;
import asw1030.beans.enums.SensorState;
import asw1030.beans.interfaces.ISensorEventsListener;
import asw1030.dal.SensorListFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;

/**
 *
 * @author Thomas
 */
public class SensorModel implements ISensorEventsListener{
        
    private static SensorModel instance;
    private HashMap<Integer,Sensor> sensors;
    
    private SensorListFile sList;
    
    private static final Object locker= new Object();
    
    private List<IModelEventsListener> listeners;
        
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
            sList = SensorListFile.getInstance(servletContext);
                    //sensors= dal.fetchRecords();
                    } catch (Exception ex) {
            Logger.getLogger(SensorModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            sensors= sList.readFile().sensors;
        } catch (Exception ex) {
            Logger.getLogger(SensorModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        listeners = new ArrayList<>();        
    }
    
    public synchronized int addSensor(Sensor s) throws Exception{
        int index = sList.addSensor(s);
        sensors.put(index, s);
                
        listeners.stream().forEach((listener) -> {
            listener.modelEventHandler(ModelEventType.SENSORADDED, index);
        });
        
        s.addListener(this);
        
        return index;
    }
    
    public synchronized void removeSensor(int id) throws Exception{
        sList.deleteSensor(id);
        sensors.remove(id);
        
        listeners.stream().forEach((listener) -> {
            listener.modelEventHandler(ModelEventType.SENSORREMOVED, id);
        });
    }
    
    public synchronized Sensor getSensor(int id)
    {
        return sensors.get(id);
    }
    
    public synchronized void enableSensor(int id)
    {
        sensors.get(id).setStatus(SensorState.Active);
        
        listeners.stream().forEach((listener) -> {
            listener.modelEventHandler(ModelEventType.SENSORENABLED, id);
        });
    }
    
    public synchronized void disableSensor(int id)
    {
        sensors.get(id).setStatus(SensorState.Disabled);
        
                listeners.stream().forEach((listener) -> {
            listener.modelEventHandler(ModelEventType.SENSORDISABLED, id);
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

    public List<Sensor> getSensorList()
    {
        return new ArrayList<>(sensors.values());
    }
    
    @Override
    public void newEvent(SensorEventType se, Object arg) {
        switch(se){
            case ValueChanged:
                listeners.stream().forEach(list-> {list.modelEventHandler(ModelEventType.NEWSENSORVALUE, arg);});break;
        }
    }
}
