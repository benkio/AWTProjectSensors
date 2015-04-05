/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.controller;

import it.unibo.aswProject.enums.SensorEventType;
import it.unibo.aswProject.enums.SensorState;
import it.unibo.aswProject.libraries.bean.Actuator;
import it.unibo.aswProject.libraries.interfaces.IActuatorListener;
import it.unibo.aswProject.libraries.interfaces.ISensorEventsListener;
import it.unibo.aswProject.libraries.interfaces.ISensorListener;
import it.unibo.aswProject.util.SensorListFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author enricobenini
 */
public class EventDispatcher implements ISensorListener, IActuatorListener {

    private static EventDispatcher instance;

    private ISensorEventsListener listener;
    private SensorsController sc;

    public static EventDispatcher getInstance() {
        if (instance == null) {
            synchronized (EventDispatcher.class) {
                if (instance == null) {
                    instance = new EventDispatcher();
                }
            }
        }
        return instance;
    }

    public EventDispatcher() {
        sc = SensorsController.getInstance();
        sc.setListener(this);
    }

    public void setListener(ISensorEventsListener listener, SensorListFile slf) {
        this.listener = listener;
        sc.setSensorListFile(slf);
    }

    @Override
    public void update(SensorEventType event) {
        if (this.listener != null) {
            listener.newEvent(event);
        }
    }

    @Override
    public void actuatorUpdated(Actuator act) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    sc.computeSensorValues(act);
                } catch (Exception ex) {
                    Logger.getLogger(EventDispatcher.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }
}
