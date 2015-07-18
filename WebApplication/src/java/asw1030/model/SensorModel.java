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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;

/**
 * Classe che implementa il modello dei sensori
 * @author Thomas
 */
public class SensorModel implements ISensorEventsListener, IModelEventsListener{
        
    private static SensorModel instance;
    private HashMap<Integer,Sensor> sensors;
    
    private SensorListFile sList;
    
    private static final Object locker= new Object();
    
    private List<IModelEventsListener> listeners;
    
    /**
     * Ottiene un sigleton della classe
     * @param servletContext
     * @return
     * @throws Exception 
     */
    public static SensorModel getInstance(ServletContext servletContext) throws Exception{
        synchronized(locker){
            if (instance == null) {
                    instance = new SensorModel(servletContext);
            }
            return instance;
        }
    }
    private SensorModel(ServletContext servletContext) throws Exception {
        
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
        
        ActuatorModel.getInstance(servletContext).addListener(this);
        
        listeners = new ArrayList<>();        
    }
    
    /**
     * Aggiunge un sensore al model e al DAL
     * @param s 
     * @return
     * @throws Exception 
     */
    public synchronized int addSensor(Sensor s) throws Exception{
        int index = sList.addSensor(s);
        sensors.put(index, s);
                
        listeners.stream().forEach((listener) -> {
            listener.modelEventHandler(ModelEventType.SENSORADDED, index);
        });
        
        s.addListener(this);
        
        return index;
    }
    
    /**
     * Rimuove un sensore da model e DAL
     * @param id
     * @throws Exception 
     */
    public synchronized void removeSensor(int id) throws Exception{
        sList.deleteSensor(id);
        sensors.remove(id);
        
        listeners.stream().forEach((listener) -> {
            listener.modelEventHandler(ModelEventType.SENSORREMOVED, id);
        });
    }
    
    /**
     * Ottiene un sensore in base al suo id
     * @param id of the sensor
     * @return Sensor
     */
    public synchronized Sensor getSensor(int id)
    {
        return sensors.get(id);
    }
    
    /**
     * Abilita un sensore
     * @param id 
     */
    public synchronized void enableSensor(int id)
    {
        sensors.get(id).setStatus(SensorState.Active);
        
        listeners.stream().forEach((listener) -> {
            listener.modelEventHandler(ModelEventType.SENSORENABLED, id);
        });
    }
    
    /**
     * Disabilita un sensore
     * @param id 
     */
    public synchronized void disableSensor(int id)
    {
        sensors.get(id).setStatus(SensorState.Disabled);
        
                listeners.stream().forEach((listener) -> {
            listener.modelEventHandler(ModelEventType.SENSORDISABLED, id);
        });
    }
    
    /**
     * Aggiunge un listener
     * @param list 
     */
    public synchronized void addListener(IModelEventsListener list)
    {
        listeners.add(list);
    }
    
    /**
     * Rimuove un listener
     * @param list 
     */
    public synchronized void removeListener(IModelEventsListener list)
    {
        listeners.remove(list);
    }

    /**
     * Ottiene la lista dei sensori
     * @return 
     */
    public List<Sensor> getSensorList()
    {
        return new ArrayList<>(sensors.values());
    }
    
    @Override
    public void newEvent(SensorEventType se, Object arg) {
        switch(se){
            case ValueChanged:
                listeners.stream().forEach(list-> {list.modelEventHandler(ModelEventType.NEWSENSORVALUE, arg);
                });break;
        }
    }
    @Override
    public void modelEventHandler(ModelEventType type, Object arg) {
        
        if(type==ModelEventType.NEWACTUATORVALUE){
            sensors.values().stream().filter(s-> s.getStatus()==SensorState.Active).forEach(s-> {
                s.setValue(new Random().nextInt(100) + 1);
                listeners.stream().forEach((listener) -> {listener.modelEventHandler(ModelEventType.NEWSENSORVALUE, s.getId());
        });
            });
        }
    }
}
