/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.libraries.bean;

/**
 *
 * @author Enrico Benini
 */
public class Sensor {
    public String Name;
    public SensorState Status;
    public int Value;
    
    private enum SensorState{
        Active,
        Disabled,
        Warning,
        Critical
    }
}