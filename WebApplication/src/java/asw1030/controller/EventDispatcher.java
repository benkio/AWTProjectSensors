/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.controller;

import asw1030.enums.SensorEventType;
import asw1030.enums.SensorState;
import asw1030.libraries.bean.Actuator;
import asw1030.libraries.interfaces.IActuatorListener;
import asw1030.libraries.interfaces.ISensorEventsListener;
import asw1030.libraries.interfaces.ISensorListener;
import asw1030.xmlDB.SensorDB;
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
    }

    public void setListener(ISensorEventsListener listener, SensorDB slf) {
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
        try {
            sc.computeSensorValues(act);
        } catch (Exception ex) {
            Logger.getLogger(EventDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
