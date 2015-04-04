/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.model.sensors;

import it.unibo.aswProject.enums.SensorEventType;
import it.unibo.aswProject.libraries.interfaces.ISensorListener;
import it.unibo.aswProject.libraries.interfaces.ISensorEventsListener;
import it.unibo.aswProject.model.actuators.ActuatorWrapper;
import it.unibo.aswProject.libraries.interfaces.IActuatorListener;
import java.util.HashMap;
import java.util.Random;

public class SensorManager implements ISensorListener , IActuatorListener {
    
    private static SensorManager instance;
    private ISensorEventsListener listener;
    private HashMap<Integer,Sensor> sensorList;
    
    
    private SensorManager(){
        sensorList= new HashMap<>();
        
        for(int cont =0; cont <5; cont++){
            sensorList.put(cont, new Sensor(this, cont));
        }
    }
    
    public static SensorManager getInstance(){
        if (instance == null)
        {
            instance = new SensorManager();
        }

        return instance;  
    }
    
    public synchronized void addSensor(Sensor s){
        sensorList.put(s.getId(), s);
        listener.newEvent(null);
    }
    
    public synchronized Sensor getSensor(int num){
        return sensorList.get(num);
    }

    public synchronized HashMap<Integer, Sensor> getSensorList() {
        return sensorList;
    }

    public void setListener(ISensorEventsListener listener) {
        this.listener = listener;
    }

    @Override
    public synchronized void actuatorUpdated(ActuatorWrapper act) {
        for (Sensor s : sensorList.values()) {
            s.setValue(new Random().nextInt(101));
        }
    }

    @Override
    public void update(SensorEventType event, Object state) {
        
    }
}
