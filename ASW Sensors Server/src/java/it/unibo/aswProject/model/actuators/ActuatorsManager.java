/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.model.actuators;

import it.unibo.aswProject.model.sensors.SensorManager;
import java.util.LinkedList;

/**
 *
 * @author Thomas
 */
public class ActuatorsManager {
    
    private static ActuatorsManager instance;
    private LinkedList<Actuator> list;
    
    
    private ActuatorsManager(){
        list = new LinkedList<>();
    }
    
    public synchronized ActuatorsManager getInstance(){
        if (instance == null)
        {
            instance = new ActuatorsManager();
        }

        return instance;  
    }

    public synchronized LinkedList<Actuator> getList() {
        return list;
    }
    
    public synchronized void addActuator(Actuator act){
        this.list.add(act);
    }
    
    
}
