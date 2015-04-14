/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.beans;

import asw1030.beans.interfaces.IXmlRecord;
import asw1030.enums.SensorState;
import java.util.Random;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Enrico Benini
 */
@XmlRootElement(name="sensor")
public class Sensor implements IXmlRecord{
    private String name;
    private SensorState status;
    private int value;
    private int id;
    
    public Sensor(String name) {
        this.name = name;
        this.status = SensorState.Active;
        this.value = new Random().nextInt(100) + 1;
    }
    
    public Sensor(){
        this.name = "";
        this.status = SensorState.Active;
        this.value = 1;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SensorState getStatus() {
        return status;
    }

    public void setStatus(SensorState status) {
        this.status = status;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
    
}
