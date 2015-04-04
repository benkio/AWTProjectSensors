/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.libraries.bean;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Enrico Benini
 */
public class SensorList {
    public List<Sensor> sensors;
    
    public SensorList(){
        sensors = new ArrayList<>();
    }
    
}
