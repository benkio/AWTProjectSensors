/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.beans.interfaces;

import asw1030.beans.enums.ActuatorEventType;


/**
 * Modella listener degli eventi di un attuatore
 * @author Thomas
 */
public interface IActuatorEventsListener {
    void newEvent(ActuatorEventType ae,Object arg);
}
