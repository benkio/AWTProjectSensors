/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.libraries.interfaces;

import asw1030.enums.SensorEventType;

/**
 *
 * @author Thomas
 */
public interface ISensorListener {
    public void update(SensorEventType event);
}
