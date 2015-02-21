/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.model.actuators;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Thomas
 */
@XmlRootElement
public class Actuator {
    private int id;
    private int value;
    private IActuatorListener list;

    public Actuator(){
        this.id = 0;
        this.value = 0;
        this.list = null;
    }
    
    public Actuator(int id, int value, IActuatorListener list) {
        this.id = id;
        this.value = value;
        this.list = list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        list.update(this);
    }

    public IActuatorListener getList() {
        return list;
    }
}
