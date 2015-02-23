/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.model.sensors;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas
 */
public class Sensor{
    private int value;
    private Random rnd;
    private ISensorListener list;
    private int id;
    private String state;

    public Sensor(ISensorListener list, int number) {
        this.list= list;
        rnd= new Random();
        value = rnd.nextInt(101);
        this.id = number;
        this.state = "Active";
    }
    
    public synchronized int getValue(){
        return this.value;
    }
    
    public synchronized void setValue(int val){
        this.value=val;
    }

    public int getNumber() {
        return id;
    }

    public void setNumber(int number) {
        this.id = number;
    }

    public String getSensorState() {
        return state;
    }

    public void setSensorState(String state) {
        this.state = state;
    }
}
