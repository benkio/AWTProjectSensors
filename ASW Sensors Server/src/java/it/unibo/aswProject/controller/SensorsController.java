/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.controller;

import it.unibo.aswProject.enums.SensorEventType;
import it.unibo.aswProject.libraries.bean.Actuator;
import it.unibo.aswProject.libraries.bean.Sensor;
import it.unibo.aswProject.libraries.bean.SensorList;
import it.unibo.aswProject.util.SensorListFile;
import java.util.List;

/**
 *
 * @author enricobenini
 */
public class SensorsController {
    
    private static SensorsController instance;
    private SensorListFile slf;    
    private EventDispatcher eventDispatcher;
    
    static SensorsController getInstance() {
        if (instance == null) {
            synchronized (SensorsController.class) {
                if (instance == null) {
                    instance = new SensorsController();
                }
            }
        }
        return instance;
    }
    
    public void setSensorListFile(SensorListFile slf){
        this.slf = slf; 
    }
    
    public void setListener(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    public void computeSensorValues(Actuator act) throws Exception {
        List<Sensor> tempList = slf.readFile().sensors;
        for (Sensor s: tempList){
            int sensorNewValue = s.Value;

            if(act.id % 2 == 0){
                sensorNewValue = sensorNewValue * (sensorNewValue/act.value);
            }else{
                sensorNewValue = sensorNewValue / (sensorNewValue/act.value);
            }
            do{    
                sensorNewValue = sensorNewValue < 0 ? sensorNewValue + 25 : sensorNewValue - 25;
            }while(sensorNewValue> 0 && sensorNewValue< 100);
            
            slf.setValue(s, sensorNewValue);
        }
        eventDispatcher.update(SensorEventType.ValueChanged);
    }
    
}
