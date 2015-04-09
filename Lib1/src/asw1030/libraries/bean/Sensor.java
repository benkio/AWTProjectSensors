/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.libraries.bean;

import asw1030.enums.SensorState;
import java.util.Random;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Enrico Benini
 */
@XmlRootElement
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
