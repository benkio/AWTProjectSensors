/*
 * Copyright 2015 Enrico Benini.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package asw1030.dal;

import asw1030.beans.Sensor;
import asw1030.beans.SensorList;
import asw1030.libraries.xml.ManageXML;
import asw1030.beans.UserList;
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
public class SensorListFile {

    private volatile static SensorListFile instance = null;
    private final File sensorFile;
    private JAXBContext context;
    private ManageXML mngXML;
    private String webPagesPath;
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
            synchronized (SensorListFile.class) {
                if (instance == null) {
                    instance = new SensorListFile(servletContext);
                }
            }
        }
        return instance;
    }
    
    private SensorListFile(ServletContext servletContext) throws Exception {
        this.servletContext = servletContext;
        context = JAXBContext.newInstance(SensorList.class);
        mngXML = new ManageXML();
        String webPagesPath = servletContext.getRealPath("/");
        sensorFile = new File(webPagesPath + "WEB-INF/xml/sensors.xml"); // this only works with default config of tomcat        
    }

    /**
     * Read the xml db
     *
     * @return the list of users
     * @throws Exception
     */
    public synchronized SensorList readFile() throws Exception {
        if (!sensorFile.exists()) {
            createFile();
        }
        InputStream in = new FileInputStream(sensorFile);
        Document userDoc = mngXML.parse(in);
        Unmarshaller u = context.createUnmarshaller();
        SensorList sensors = (SensorList) u.unmarshal(userDoc);
        return sensors;
    }

    /**
     * Delete a user, removing its entry from the xml db
     *
     * @param username
     * @throws Exception
     */
    public synchronized void deleteSensor(int index) throws Exception {
        SensorList sl = readFile();
        
        sl.sensors.remove(index);
        
        writeFile(sl);
    }
    /**
     * Register a new user, adding a new entry in the xml db
     *
     * @param s
     * @throws Exception
     */
    public synchronized int addSensor(Sensor s) throws Exception {
        SensorList sl = readFile();
      
        int index= sl.index;
        s.setId(index);
        sl.sensors.put(sl.index, s);
        sl.index++;
        
        writeFile(sl);
        
        return index;
    }

    /**
     * @param username
     * @return user information
     * @throws Exception
     */
    public synchronized Sensor getSensorById(int id) throws Exception {
        SensorList sl = readFile();
        
        if(sl.sensors.containsKey(id)){
            return sl.sensors.get(id);
        }
        
        throw new Exception("User does not exist.");
    }

    private synchronized void writeFile(SensorList sList) throws Exception {
        Marshaller marsh = context.createMarshaller();
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        marsh.marshal(sList, doc);
        OutputStream out = new FileOutputStream(sensorFile);
        mngXML.transform(out, doc);
        out.close();
    }

    private void createFile() throws Exception {

        SensorList sl = new SensorList();
        writeFile(sl);
    }
}
