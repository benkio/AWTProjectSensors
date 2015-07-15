/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.beans;

import asw1030.beans.enums.ActuatorEventType;
import asw1030.beans.interfaces.IActuatorEventsListener;
import asw1030.beans.interfaces.IXmlRecord;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author enricobenini
 */
@XmlRootElement(name="actuator")
public class Actuator implements IXmlRecord{
    private int id;
    private int value;
    
    private List<IActuatorEventsListener> listeners;
    
    public Actuator(){
        this.value = 0;
        
        listeners= new ArrayList<>();
    }

    public void setValue(int value) {
        this.value = value;
        
        listeners.stream().forEach(l->{ l.newEvent(ActuatorEventType.NEWVALUE, value);});
    }

    public int getValue() {
        return value;
    }
    
    public void addListener(IActuatorEventsListener list){
        listeners.add(list);
    }
    
    public void removeListener(IActuatorEventsListener list){
        listeners.remove(list);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id=id;
    }
    
}
