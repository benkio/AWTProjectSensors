/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.model.sensors;

import it.unibo.aswProject.model.actuators.Actuator;
import it.unibo.aswProject.model.actuators.IActuatorListener;
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
        sensorList.put(s.getNumber(), s);
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
    public void update(SensorEventType event) {
        if(this.listener != null)
            listener.newEvent(event);
    }

    @Override
    public synchronized void actuatorUpdated(Actuator act) {
        for (Sensor s : sensorList.values()) {
            s.setValue(new Random().nextInt(101));
        }
    }
}
