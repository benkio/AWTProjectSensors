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

import asw1030.beans.Actuator;
import asw1030.beans.ActuatorList;
import asw1030.libraries.xml.ManageXML;
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
    private String webPagesPath;
    private final ServletContext servletContext;
    /**
     * Return a singleton object of UserListFile
     *     
* @param servletContext
     * @return
     * @throws Exception
     */
    public static ActuatorListFile getInstance(ServletContext servletContext) throws Exception {
        if (instance == null) {
            synchronized (ActuatorList.class) {
                if (instance == null) {
                    instance = new ActuatorListFile(servletContext);
                }
            }
        }
        return instance;
    }
    
    private ActuatorListFile(ServletContext servletContext) throws Exception {
        this.servletContext = servletContext;
        context = JAXBContext.newInstance(ActuatorList.class);
        mngXML = new ManageXML();
        String webPagesPath = servletContext.getRealPath("/");
        actuatorFile = new File(webPagesPath + "WEB-INF/xml/actuators.xml"); // this only works with default config of tomcat        
    }

    /**
     * Read the xml db
     *
     * @return the list of users
     * @throws Exception
     */
    public synchronized ActuatorList readFile() throws Exception {
        if (!actuatorFile.exists()) {
            createFile();
        }
        InputStream in = new FileInputStream(actuatorFile);
        Document userDoc = mngXML.parse(in);
        Unmarshaller u = context.createUnmarshaller();
        ActuatorList actuators = (ActuatorList) u.unmarshal(userDoc);
        return actuators;
    }

    /**
     * Delete a user, removing its entry from the xml db
     *
     * @param username
     * @throws Exception
     */
    public synchronized void deleteActuator(int index) throws Exception {
        ActuatorList al = readFile();
        
        al.actuators.remove(index);
        
        writeFile(al);
    }
    /**
     * Register a new user, adding a new entry in the xml db
     *
     * @param s
     * @throws Exception
     */
    public synchronized int addActuator(Actuator s) throws Exception {
        ActuatorList sl = readFile();
      
        int index= sl.index;
        s.setId(index);
        sl.actuators.put(sl.index, s);
        sl.index++;
        
        writeFile(sl);
        
        return index;
    }

    /**
     * @param username
     * @return user information
     * @throws Exception
     */
    public synchronized Actuator getActuatorById(int id) throws Exception {
        ActuatorList sl = readFile();
        
        if(sl.actuators.containsKey(id)){
            return sl.actuators.get(id);
        }
        
        throw new Exception("User does not exist.");
    }

    private synchronized void writeFile( ActuatorList aList) throws Exception {
        Marshaller marsh = context.createMarshaller();
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        marsh.marshal(aList, doc);
        OutputStream out = new FileOutputStream(actuatorFile);
        mngXML.transform(out, doc);
        out.close();
    }

    private void createFile() throws Exception {

        ActuatorList al = new ActuatorList();
        writeFile(al);
    }
}
