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
public class Sensor extends Thread{
    private int value;
    private Random rnd;
    private boolean go;
    private ISensorListener list;
    private int number;
    private String state;

    public Sensor(ISensorListener list, int number) {
        this.list= list;
        rnd= new Random();
        
        value = rnd.nextInt(101);
        go = true;
        this.number = number;
        this.state = "Active";
    }
    
    public synchronized int getValue(){
        return this.value;
    }
    
    public synchronized void setValue(int val){
        this.value=val;
    }

    @Override
    public void run() {
        while (go){
            try {
                //Thread.sleep((int)((rnd.nextDouble()+1000)*10));
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Sensor.class.getName()).log(Level.SEVERE, null, ex);
            }
            synchronized(this){
                value= rnd.nextInt(101);
                this.list.update(this);
            }   
        }
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getSensorState() {
        return state;
    }

    public void setSensorState(String state) {
        this.state = state;
    }
    
    public synchronized void dispose()
    {
        this.go=false;
    }
}
