/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.model;

import asw1030.beans.enums.ModelEventType;
import asw1030.beans.Actuator;
import asw1030.beans.enums.ActuatorEventType;
import asw1030.beans.interfaces.IActuatorEventsListener;
import asw1030.dal.ActuatorListFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;

/**
 * Classe che implementa il model degli. Tutte le operations effettuabili 
 * sugli attuatori vengono esposte da tale classe. Invia notifiche se cambia lo 
 * stato interno
 * @author Thomas
 */
public class ActuatorModel implements IActuatorEventsListener{
    private static ActuatorModel instance;
    
    private final static Object locker= new Object();
    
    private ActuatorListFile alf;
    
    private HashMap<Integer,Actuator> actuators;
    
    private final ArrayList<IModelEventsListener> listeners;
    
    public static ActuatorModel getInstance(ServletContext servletContext) throws JAXBException, Exception{
        synchronized(locker){
            if (instance == null) {
                    instance = new ActuatorModel(servletContext);
            }
            return instance;
        }
    }
    
    public ActuatorModel(ServletContext servletContext) throws JAXBException, Exception {
        
        alf= ActuatorListFile.getInstance(servletContext);
        
        actuators= alf.readFile().actuators;
        listeners = new ArrayList<>();
    }

    /**
     * Ritorna lista attuatori
     * @return 
     */
    public List<Actuator> getActuators() {
        return new ArrayList<>(actuators.values());
    }
    
    /**
     * Aggiunge un attuatore
     * @param act
     * @return
     * @throws Exception 
     */
    public int addActuator(Actuator act) throws Exception
    {
        int index = alf.addActuator(act);

        actuators.put(index, act);
        
        listeners.stream().forEach((listener) -> {
            listener.modelEventHandler(ModelEventType.ACTUATORADDED, index);
        });
        
        act.addListener(this);
        
        return index;
    }
    
    /**
     * Rimuove un attuatore
     * @param id
     * @throws Exception 
     */
    public void removeActuator(int id) throws Exception{
        alf.deleteActuator(id);
        actuators.remove(id);
        
        listeners.stream().forEach((listener) -> {
            listener.modelEventHandler(ModelEventType.ACTUATORREMOVED, id);
        });
    }
    
    /**
     * Aggiunge listener eventi attuatori
     * @param list 
     */
    public synchronized void addListener(IModelEventsListener list)
    {
        listeners.add(list);
    }
    
    /**
     * Rimuove listener eventi attuatori
     * @param list 
     */
    public synchronized void removeListener(IModelEventsListener list)
    {
        listeners.remove(list);
    }

    /**
     * Setta valore di un attuatore
     * @param id
     * @param val 
     */
    public synchronized void setActuatorValue(int id, int val){
        if(actuators.containsKey(id)){
            actuators.get(id).setValue(val);
            listeners.stream().forEach(list-> {list.modelEventHandler(ModelEventType.NEWACTUATORVALUE, val);});
        }
        
    }
    
    /**
     * Notidifica un nuovo evento dagli attuatori
     * @param ae
     * @param arg 
     */
    @Override
    public void newEvent(ActuatorEventType ae, Object arg) {
        switch(ae){
            case NEWVALUE:
                listeners.stream().forEach(list-> {list.modelEventHandler(ModelEventType.NEWACTUATORVALUE, arg);});break;
        }
    }
}
