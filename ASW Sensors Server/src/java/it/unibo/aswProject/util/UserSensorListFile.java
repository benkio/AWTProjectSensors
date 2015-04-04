/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.util;


import it.unibo.aswProject.libraries.bean.SensorList;
import it.unibo.aswProject.libraries.bean.User;
import it.unibo.aswProject.libraries.bean.UserList;
import it.unibo.aswProject.libraries.bean.UserSensorList;
import it.unibo.aswProject.libraries.xml.ManageXML;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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
            synchronized (UserListFile.class) {
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
    
    private synchronized void writeFile(UserSensorList userSensorList) throws Exception {
        Marshaller marsh = context.createMarshaller();
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        marsh.marshal(userSensorList, doc);
        OutputStream out = new FileOutputStream(userSensorFile);
        mngXML.transform(out, doc);
        out.close();
    }
    
    public synchronized SensorList getSensorListByUser(User user){}
    public synchronized void addSensorToUser(User user){}
    public synchronized void removeSensorToUser(User user){}
    public synchronized void setSensorToUsers(UserList users, SensorList sensors){}
}