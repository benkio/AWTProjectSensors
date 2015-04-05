/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.util;

import it.unibo.aswProject.controller.EventDispatcher;
import it.unibo.aswProject.enums.SensorEventType;
import it.unibo.aswProject.enums.SensorState;
import it.unibo.aswProject.libraries.bean.Sensor;
import it.unibo.aswProject.libraries.bean.SensorList;
import it.unibo.aswProject.libraries.xml.ManageXML;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

/**
 *
 * @author Enrico Benini
 */
public class SensorListFile {

    private volatile static SensorListFile instance = null;
    private final File sensorFile;
    private JAXBContext context;
    private ManageXML mngXML;
    private final ServletContext servletContext;

    /**
     * Return a singleton object of UserListFile
     *
     * @param servletContext
     * @return
     * @throws Exception
     */
    public static SensorListFile getInstance(ServletContext servletContext) throws Exception {
        if (instance == null) {
            synchronized (UserListFile.class) {
                if (instance == null) {
                    instance = new SensorListFile(servletContext);
                }
            }
        }
        return instance;
    }

    private SensorListFile(ServletContext servletContext) throws Exception {
        context = JAXBContext.newInstance(SensorList.class);
        this.servletContext = servletContext;
        mngXML = new ManageXML();
        String webPagesPath = servletContext.getRealPath("/");
        sensorFile = new File(webPagesPath + "WEB-INF/xml/sensors.xml"); // this only works with default config of tomcat
    }

    /**
     * Read the xml db
     *
     * @return the list of sensor
     * @throws Exception
     */
    public synchronized SensorList readFile() throws Exception {
        if (!sensorFile.exists()) {
            createFile();
        }
        InputStream in = new FileInputStream(sensorFile);
        Document sensorDoc = mngXML.parse(in);
        Unmarshaller u = context.createUnmarshaller();
        SensorList sensors = (SensorList) u.unmarshal(sensorDoc);
        return sensors;
    }

    private synchronized void writeFile(SensorList sensorList) throws Exception {
        Marshaller marsh = context.createMarshaller();
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        marsh.marshal(sensorList, doc);
        OutputStream out = new FileOutputStream(sensorFile);
        mngXML.transform(out, doc);
        out.close();
    }

    private void createFile() throws Exception {
        SensorList sl = new SensorList();
        for (int cont = 0; cont < 5; cont++) {
            sl.sensors.add(cont, new Sensor(Integer.toString(cont)));
        }
        writeFile(sl);
    }

    /**
     * @param name
     * @return sensor information
     * @throws Exception
     */
    public synchronized Sensor getSensorByName(String name) throws Exception {
        SensorList sl = readFile();
        for (Sensor s : sl.sensors) {
            if (s.Name.equals(name)) {
                return s;
            }
        }
        throw new Exception("Sensor does not exist.");
    }

    /**
     * Delete a sensor, removing its entry from the xml db
     *
     * @param name
     * @throws Exception
     */
    public synchronized void deleteSensor(String name) throws Exception {
        SensorList sl = readFile();
        for (Sensor s : sl.sensors) {
            if (name.equals(s.Name)) {
                sl.sensors.remove(s);
                writeFile(sl);
                UserListFile.getInstance(servletContext).readFile().users.stream().forEach(u -> {
                    try {
                        UserSensorListFile.getInstance(servletContext).removeSensorToUser(u, s.Name);
                    } catch (Exception ex) {
                        Logger.getLogger(SensorListFile.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                return;
            }
        }
        throw new Exception("Sensor does not exist.");
    }

    /**
     * Add a new sensor, adding a new entry in the xml db
     *
     * @param sensor
     * @throws Exception
     */
    public synchronized void addSensor(Sensor sensor) throws Exception {
        SensorList sl = readFile();
        if (!isSensorInDB(sensor, sl)) {
            sl.sensors.add(sensor);
            writeFile(sl);
            UserListFile.getInstance(servletContext).readFile().users.stream().forEach(u -> {
                try {
                    UserSensorListFile.getInstance(servletContext).addSensorToUser(u, sensor.Name);
                } catch (Exception ex) {
                    Logger.getLogger(SensorListFile.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } else {
            throw new Exception("Sensor already exist.");
        }
    }

    private boolean isSensorInDB(Sensor sensor, SensorList sl) {
        return sl.sensors.stream().anyMatch((s) -> (s.Name.equals(sensor.Name)));
    }

    public synchronized int getValue(Sensor sensor) throws Exception {
        return getSensorByName(sensor.Name).Value;
    }

    public synchronized SensorState getStatus(Sensor sensor) throws Exception {
        return getSensorByName(sensor.Name).Status;
    }

    public synchronized void setValue(Sensor sensor, int newValue) throws Exception {
        SensorList sl = readFile();
        if (isSensorInDB(sensor, sl)) {
            int index = sl.sensors.indexOf(sensor);
            sensor.Value = newValue;
            sl.sensors.set(index, sensor);
            writeFile(sl);
            EventDispatcher.getInstance().update(SensorEventType.ValueChanged);
        } else {
            throw new Exception("Actuator does not exist.");
        }
    }
}
