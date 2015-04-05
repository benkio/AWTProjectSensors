/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.libraries.bean;

import it.unibo.aswProject.enums.SensorState;
import java.util.Random;

/**
 *
 * @author Enrico Benini
 */
public class Sensor {
    public String Name;
    public SensorState Status;
    public int Value;
    
    public Sensor(String name) {
        this.Name = name;
        this.Status = SensorState.Active;
        this.Value = new Random().nextInt(100) + 1;
    }
    
    public Sensor(){
        this.Name = "";
        this.Status = SensorState.Active;
        this.Value = 1;
    }
}
