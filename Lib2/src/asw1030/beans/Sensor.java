package asw1030.beans;

import asw1030.beans.enums.SensorKind;
import asw1030.beans.interfaces.IXmlRecord;
import asw1030.beans.enums.SensorState;
import asw1030.beans.interfaces.ISensorEventsListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="sensor")
public class Sensor{
    private SensorKind kind;
    private SensorState status;
    private int value;
    private int id;
    
    private List<ISensorEventsListener> listeners;
    
    public Sensor(){
        this.status = SensorState.Active;
        this.value = new Random().nextInt(100) + 1;
        this.kind= SensorKind.TEMPERATURE;
        listeners= new ArrayList<>();
    }
    
    public Sensor(SensorKind kind) {
        this();
        this.kind = kind;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public SensorKind getKind() {
        return kind;
    }

    public void setKind(SensorKind kind) {
        this.kind = kind;
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
    
    public void addListener(ISensorEventsListener list)
    {
        listeners.add(list);
    }
    
    public void removeListener(ISensorEventsListener list)
    {
        listeners.remove(list);
    }
}


