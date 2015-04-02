/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.util;

import it.unibo.aswProject.libraries.bean.SensorList;
import it.unibo.aswProject.libraries.bean.UserList;
import it.unibo.aswProject.libraries.xml.ManageXML;
import java.io.File;
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerConfigurationException;

/**
 *
 * @author Enrico Benini
 */
public class SensorListFile {
    
    private volatile static SensorListFile instance = null;
    private final File sensorFile;
    private JAXBContext context;
    private ManageXML mngXML;

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
        mngXML = new ManageXML();
        String webPagesPath = servletContext.getRealPath("/");
        sensorFile = new File(webPagesPath + "WEB-INF/xml/sensors.xml"); // this only works with default config of tomcat
    }
    
}
