/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.util;


import it.unibo.aswProject.controller.EventDispatcher;
import it.unibo.aswProject.enums.SensorEventType;
import it.unibo.aswProject.libraries.bean.ListWrapper;
import it.unibo.aswProject.libraries.bean.SensorList;
import it.unibo.aswProject.libraries.bean.User;
import it.unibo.aswProject.libraries.bean.UserSensorList;
import it.unibo.aswProject.libraries.xml.ManageXML;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;


/**
 *
 * @author enricobenini
 */
public class UserSensorListFile {
     private volatile static UserSensorListFile instance = null;
    private final File userSensorFile;
    private JAXBContext context;
    private ManageXML mngXML;

     /**
     * Return a singleton object of UserListFile
     *     
     * @param servletContext
     * @return
     * @throws Exception
     */
    public static UserSensorListFile getInstance(ServletContext servletContext) throws Exception {
        if (instance == null) {
            synchronized (UserSensorListFile.class) {
                if (instance == null) {
                    instance = new UserSensorListFile(servletContext);
                }
            }
        }
        return instance;
    }

    private UserSensorListFile(ServletContext servletContext) throws Exception {
        context = JAXBContext.newInstance(UserSensorList.class);
        mngXML = new ManageXML();
        String webPagesPath = servletContext.getRealPath("/");
        userSensorFile = new File(webPagesPath + "WEB-INF/xml/userSensor.xml"); // this only works with default config of tomcat
    }
    
     /**
     * Read the xml db
     *
     * @return the list of sensor
     * @throws Exception
     */
    public synchronized UserSensorList readFile() throws Exception {
        if (!userSensorFile.exists()) {
            writeFile(new UserSensorList());
        }
        InputStream in = new FileInputStream(userSensorFile);
        Document userSensorDoc = mngXML.parse(in);
        Unmarshaller u = context.createUnmarshaller();
        UserSensorList sensors = (UserSensorList) u.unmarshal(userSensorDoc);
        return sensors;
    }
    
    private void writeFile(UserSensorList userSensorList) throws Exception {
        Marshaller marsh = context.createMarshaller();
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        marsh.marshal(userSensorList, doc);
        OutputStream out = new FileOutputStream(userSensorFile);
        mngXML.transform(out, doc);
        out.close();
    }
    
    public synchronized Map<String,Boolean> getSensorIdsByUser(User user) throws Exception{
        return readFile().userSensor.get(user.username).sensorList;
    }
    
    public synchronized void addSensorToUser(User user, String sensor) throws Exception{
        UserSensorList usl = readFile();
        ListWrapper userSensors = usl.userSensor.get(user.username);
        if (!isSensorUserRelationExist(userSensors.sensorList,sensor)){
            userSensors.sensorList.put(sensor,true);
            usl.userSensor.put(user.username, userSensors);
            writeFile(usl);
        }
    }
    public synchronized void removeSensorToUser(User user, String sensor) throws Exception{
        UserSensorList usl = readFile();
        ListWrapper userSensors = usl.userSensor.get(user.username);
        if (isSensorUserRelationExist(userSensors.sensorList,sensor)){
            userSensors.sensorList.remove(sensor);
            usl.userSensor.put(user.username, userSensors);
            writeFile(usl);
        }
    }
    public synchronized void setSensorsToUser(User user, SensorList sensors) throws Exception{
        UserSensorList usl = readFile();
        List<String> sen = sensors.sensors.stream().map(s -> s.Name).collect(Collectors.toList());
        ListWrapper lw = new ListWrapper();
        lw.setList(sen);
        usl.userSensor.put(user.username, lw);
        writeFile(usl);
    }
    
    public synchronized void setUserSensorRelation(String sensorName, Boolean sensorEnable, String username) throws Exception {
        User tempUser = new User();
        tempUser.username = username;
        UserSensorList usl = readFile();
        usl.userSensor.get(username).sensorList.replace(sensorName, sensorEnable);
        writeFile(usl);
        EventDispatcher.getInstance().update(SensorEventType.ValueChanged);
    }

    private boolean isSensorUserRelationExist(Map<String,Boolean> sensorMap, String sensor) {
        return sensorMap.containsKey(sensor);
    }
}
