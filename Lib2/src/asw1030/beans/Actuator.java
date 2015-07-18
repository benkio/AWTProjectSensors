/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.beans;

import asw1030.beans.enums.ActuatorEventType;
import asw1030.beans.interfaces.IActuatorEventsListener;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Modella un attuatore
 * @author enricobenini
 */
@XmlRootElement(name="actuator")
public class Actuator{
    private int id;
    private int value;
    
    private List<IActuatorEventsListener> listeners;
    
    public Actuator(){
        this.value = 0;
        
        listeners= new ArrayList<>();
    }

    /**
     * Setta valore attuatore
     * @param value 
     */
    public void setValue(int value) {
        this.value = value;
        
        listeners.stream().forEach(l->{ l.newEvent(ActuatorEventType.NEWVALUE, value);});
    }

    /**
     * Ottiene valore attuatore
     * @return 
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Aggiunge listener eventi attuatore
     * @param list 
     */
    public void addListener(IActuatorEventsListener list){
        listeners.add(list);
    }
    
    /**
     * Rimuove listener eventi Attuatore
     * @param list 
     */
    public void removeListener(IActuatorEventsListener list){
        listeners.remove(list);
    }

    /**
     * Ritorna id sensore
     * @return 
     */
    public int getId() {
        return this.id;
    }

    /**
     * Setta id sensore
     * @param id 
     */
    public void setId(int id) {
        this.id=id;
    }
    
}
