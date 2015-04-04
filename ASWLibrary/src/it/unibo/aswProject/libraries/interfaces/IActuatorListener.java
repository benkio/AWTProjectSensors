/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.libraries.interfaces;

import it.unibo.aswProject.libraries.bean.Actuator;


/**
 *
 * @author Thomas
 */
public interface IActuatorListener {
    void actuatorUpdated(Actuator act);
}
