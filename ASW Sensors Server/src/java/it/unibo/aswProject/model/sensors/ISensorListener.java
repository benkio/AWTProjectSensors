/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.model.sensors;

/**
 *
 * @author Thomas
 */
public interface ISensorListener {
    public void update(SensorEventType event, Object state);
}
