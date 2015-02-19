/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.services.sensors;

import java.util.HashMap;

/**
 *
 * @author Thomas
 */
public class SensorManager {
    private static SensorManager instance;
    
    private HashMap<Integer,Sensor> sensorList;
    
    private SensorManager(){
        sensorList= new HashMap<>();
        
        for(int cont =0; cont <5; cont++){
            sensorList.put(cont, new Sensor(null, cont));
        }
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
    
}
