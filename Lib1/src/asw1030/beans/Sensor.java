package asw1030.beans;

import asw1030.beans.interfaces.IXmlRecord;
import asw1030.enums.SensorState;
import java.util.Random;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="sensor")
public class Sensor implements IXmlRecord{
    private SensorKind kind;
    private SensorState status;
    private int value;
    private int id;
    
    public Sensor(SensorKind kind) {
        this.kind = kind;
        this.status = SensorState.Active;
        this.value = new Random().nextInt(100) + 1;
    }
    
    @Override
    public int getId() {
        return id;
    }

    @Override
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

    private void setValue(int value) {
        this.value = value;
    }
    
    public enum SensorKind
    {
        TEMPERATURE,
        HUMIDITY,
        GAS_PRESSURE
    }
}


