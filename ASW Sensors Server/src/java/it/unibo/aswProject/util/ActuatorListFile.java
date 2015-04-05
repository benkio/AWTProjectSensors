/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.util;

import it.unibo.aswProject.controller.EventDispatcher;
import it.unibo.aswProject.libraries.bean.Actuator;
import it.unibo.aswProject.libraries.bean.ActuatorList;
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
 * @author Enrico Benini
 */
public class ActuatorListFile {
    
    private volatile static ActuatorListFile instance = null;
    private final File actuatorFile;
    private JAXBContext context;
    private ManageXML mngXML;

     /**
     * Return a singleton object of UserListFile
     *     
     * @param servletContext
     * @return
     * @throws Exception
     */
    public static ActuatorListFile getInstance(ServletContext servletContext) throws Exception {
        if (instance == null) {
            synchronized (UserListFile.class) {
                if (instance == null) {
                    instance = new ActuatorListFile(servletContext);
                }
            }
        }
        return instance;
    }

    private ActuatorListFile(ServletContext servletContext) throws Exception {
        context = JAXBContext.newInstance(ActuatorList.class);
        mngXML = new ManageXML();
        String webPagesPath = servletContext.getRealPath("/");
        actuatorFile = new File(webPagesPath + "WEB-INF/xml/actuators.xml"); // this only works with default config of tomcat
    }
    
     /**
     * Read the xml db
     *
     * @return the list of actuator
     * @throws Exception
     */
    public synchronized ActuatorList readFile() throws Exception {
        if (!actuatorFile.exists()) {
            createFile();
        }
        InputStream in = new FileInputStream(actuatorFile);
        Document actuatorDoc = mngXML.parse(in);
        Unmarshaller u = context.createUnmarshaller();
        ActuatorList actuators = (ActuatorList) u.unmarshal(actuatorDoc);
        return actuators;
    }
    
    private synchronized void writeFile(ActuatorList actuatorList) throws Exception {
        Marshaller marsh = context.createMarshaller();
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        marsh.marshal(actuatorList, doc);
        OutputStream out = new FileOutputStream(actuatorFile);
        mngXML.transform(out, doc);
        out.close();
    }
    
    private void createFile() throws Exception {
        ActuatorList al = new ActuatorList();
        Actuator a1 = new Actuator();
        Actuator a2 = new Actuator();
        a1.id = 1;
        a1.value = 50;
        a2.id = 2;
        a2.value = 50;
        al.actuators.add(a1);
        al.actuators.add(a2);
        writeFile(al);
    }
     /**
     * @param id
     * @return actuator information
     * @throws Exception
     */
    public synchronized Actuator getActuatorByName(int id) throws Exception {
        ActuatorList al = readFile();
        for (Actuator a : al.actuators) {
            if (a.id == id) {
                return a;
            }
        }
        throw new Exception("Actuator does not exist.");
    }
    
     /**
     * Delete a actuator, removing its entry from the xml db
     *
     * @param id
     * @throws Exception
     */
    public synchronized void deleteActuator(int id) throws Exception {
        ActuatorList al = readFile();
        for (Actuator a : al.actuators) {
            if (id == a.id) {
                al.actuators.remove(a);
                writeFile(al);
                return;
            }
        }
        throw new Exception("Actuator does not exist.");
    }
    
     /**
     * Add a new actuator, adding a new entry in the xml db
     *
     * @param actuator
     * @throws Exception
     */
    public synchronized void addActuator(Actuator actuator) throws Exception {
        ActuatorList al = readFile();
        if (!isActuatorInDB(actuator, al)) {
            al.actuators.add(actuator);
            writeFile(al);
        } else {
            throw new Exception("Actuator already exist.");
        }
    }
    
    public synchronized void setValue(Actuator actuator, int newValue) throws Exception {
        ActuatorList al = readFile();
        if (isActuatorInDB(actuator,al)){
            int index = al.actuators.indexOf(actuator);
            actuator.value = newValue;
            al.actuators.set(index, actuator);
            writeFile(al);
            EventDispatcher.getInstance().actuatorUpdated(actuator);
        }
        else
            throw new Exception("Actuator does not exist.");
    }

    private boolean isActuatorInDB(Actuator actuator, ActuatorList al) {
        return al.actuators.contains(actuator);
    }

    public synchronized int getValue(Actuator actuator) throws Exception{
        return getActuatorByName(actuator.id).value;
    }
}
