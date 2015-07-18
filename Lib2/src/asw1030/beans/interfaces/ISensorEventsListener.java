/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.beans.interfaces;

import asw1030.beans.enums.SensorEventType;


/**
 * Modella listener eventi del sensore
 * @author Thomas
 */
public interface ISensorEventsListener {
    void newEvent(SensorEventType se,Object arg);
}
