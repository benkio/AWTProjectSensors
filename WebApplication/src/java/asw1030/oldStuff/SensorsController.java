/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.oldStuff;

import asw1030.enums.SensorEventType;
import asw1030.libraries.bean.Actuator;
import asw1030.libraries.bean.Sensor;
import asw1030.libraries.bean.SensorList;
import asw1030.oldStuff.SensorDB;
import java.util.List;
import java.util.Random;

/**
 *
 * @author enricobenini
 */
public class SensorsController {

    private static SensorsController instance;
    private SensorDB slf;

    static SensorsController getInstance() {
        if (instance == null) {
            synchronized (SensorsController.class) {
                if (instance == null) {
                    instance = new SensorsController();
                }
            }
        }
        return instance;
    }

    public void setSensorListFile(SensorDB slf) {
        this.slf = slf;
    }

    public void computeSensorValues(Actuator act) throws Exception {
        if (slf != null) {
            List<Sensor> tempList = slf.readFile().sensors;
            int actuatorInfluence = new Random().nextInt(((act.value +20)-(act.value-20)) + 1) +(act.value -20);
            for (Sensor s : tempList) {
                int sensorNewValue = s.Value;
                
                sensorNewValue = (act.id % 2 == 0) ? sensorNewValue + actuatorInfluence : sensorNewValue - actuatorInfluence;
                
                do {
                    sensorNewValue = sensorNewValue < 0 ? sensorNewValue + actuatorInfluence : sensorNewValue - actuatorInfluence;
                } while (!(sensorNewValue > 0 && sensorNewValue < 100));

                slf.setValue(s, sensorNewValue);
            }
        }
    }

}
