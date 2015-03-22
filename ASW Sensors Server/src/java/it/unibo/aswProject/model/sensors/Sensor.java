package it.unibo.aswProject.model.sensors;

/**
 *
 * @author Thomas
 */
public class Sensor{
    private int id;
    private int value;
    private ISensorListener list;
    private SensorState state;

    public Sensor(ISensorListener list, int number) {
        this.list= list;
        this.id = number;
        this.state = SensorState.Active;
    }
    
    public synchronized int getValue(){
        return this.value;
    }
    
    public synchronized void setValue(int val){
        this.value=val;
        
        if(list!= null){
            new Thread(() -> {
                this.list.update(SensorEventType.ValueChanged,val);
            }).start();
        }
    }

    public synchronized int getId() {
        return id;
    }

    public synchronized void setId(int number) {
        this.id = number;
    }

    public synchronized SensorState getState() {
        return state;
    }

    public synchronized void setState(SensorState state) {
        this.state = state;
    }
}
