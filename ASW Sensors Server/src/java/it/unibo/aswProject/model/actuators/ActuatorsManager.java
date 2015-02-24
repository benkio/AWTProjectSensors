/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.model.actuators;

import it.unibo.aswProject.model.sensors.SensorManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Thomas
 */
public class ActuatorsManager {
    
    private static ActuatorsManager instance;
    
    private HashMap<Integer,Actuator> list;
    private IActuatorListener listener;
    
    private ActuatorsManager(){
        list = new HashMap<>();
    }
    
    public static synchronized ActuatorsManager getInstance(){
        if (instance == null)
        {
            instance = new ActuatorsManager();
        }

        return instance;  
    }

    public synchronized ArrayList<Actuator> getList() {
        return new ArrayList<>(list.values());
    }
    
    public synchronized void addActuator(Actuator act){
        this.list.put(act.getId(), act);
    }
    
    public synchronized void setActuatorValue(int id, int value){
        list.get(id).setValue(value);
        
        if(listener!= null){
            new Thread(() -> {
                this.listener.actuatorUpdated(list.get(id));
            }).start();
        }
    }

    public void setListener(IActuatorListener listener) {
        this.listener = listener;
    }
    
    
}
