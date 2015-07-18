package asw1030.beans;

import asw1030.beans.enums.SensorKind;
import asw1030.beans.enums.SensorState;
import asw1030.beans.interfaces.ISensorEventsListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe sensore che verra' serializzata, singolo record del DB.
 * @author benkio
 */
@XmlRootElement(name="sensor")
public class Sensor{
    private SensorKind kind;
    private SensorState status;
    private int value;
    private int id;
    
    private List<ISensorEventsListener> listeners;
    
    /**
     * Costruttore, setta il defaul per il sensore.
     */
    public Sensor(){
        this.status = SensorState.Active;
        this.value = new Random().nextInt(100) + 1;
        this.kind= SensorKind.TEMPERATURE;
        listeners= new ArrayList<>();
    }
    
    /**
     * Sensore Apposito nel caso sia presente il tipo.
     * @param kind 
     */
    public Sensor(SensorKind kind) {
        this();
        this.kind = kind;
    }
    
    /**
     * Id Getter.
     * @return SensorId
     */
    public int getId() {
        return id;
    }

    /**
     * Id Setter.
     * @param id 
     */
    public void setId(int id) {
        this.id=id;
    }

    /**
     * Kind Getter.
     * @return Sensor Kind
     */
    public SensorKind getKind() {
        return kind;
    }

    /**
     * Kind Setter.
     * @param kind 
     */
    public void setKind(SensorKind kind) {
        this.kind = kind;
    }

    /**
     * Sensor Status Getter.
     * @return Sensor Status
     */
    public SensorState getStatus() {
        return status;
    }

    /**
     * Sensor Status Setter.
     * @param status 
     */
    public void setStatus(SensorState status) {
        this.status = status;
    }

    /**
     * Sensor Value Getter.
     * @return Sensor Value
     */
    public int getValue() {
        return value;
    }

    /**
     * Sensor Value Setter.
     * @param value 
     */
    public void setValue(int value) {
        this.value = value;
    }
    
    /**
     * Pattern Observer Add Listener.
     * @param list 
     */
    public void addListener(ISensorEventsListener list)
    {
        listeners.add(list);
    }
    /**
     * Pattern Observer Remove Listener
     * @param list 
     */
    public void removeListener(ISensorEventsListener list)
    {
        listeners.remove(list);
    }
}


