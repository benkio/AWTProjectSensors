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
package it.unibo.aswProject.util;

import it.unibo.aswProject.libraries.xml.ManageXML;
import it.unibo.aswProject.libraries.bean.User;
import it.unibo.aswProject.libraries.bean.UserList;
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
public class UserListFile {

    private volatile static UserListFile instance = null;
    private final File userFile;
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
    public static UserListFile getInstance(ServletContext servletContext) throws Exception {
        if (instance == null) {
            synchronized (UserListFile.class) {
                if (instance == null) {
                    instance = new UserListFile(servletContext);
                }
            }
        }
        return instance;
    }
    
    private UserListFile(ServletContext servletContext) throws Exception {
        this.servletContext = servletContext;
        context = JAXBContext.newInstance(UserList.class);
        mngXML = new ManageXML();
        String webPagesPath = servletContext.getRealPath("/");
        userFile = new File(webPagesPath + "WEB-INF/xml/users.xml"); // this only works with default config of tomcat
    }

    /**
     * Read the xml db
     *
     * @return the list of users
     * @throws Exception
     */
    public synchronized UserList readFile() throws Exception {
        if (!userFile.exists()) {
            createFile();
        }
        InputStream in = new FileInputStream(userFile);
        Document userDoc = mngXML.parse(in);
        Unmarshaller u = context.createUnmarshaller();
        UserList users = (UserList) u.unmarshal(userDoc);
        return users;
    }

    /**
     * Register a new user, adding a new entry in the xml db
     *
     * @param user
     * @throws Exception
     */
    public synchronized void registerUser(User user) throws Exception {
        UserList ul = readFile();
        if (!isUserRegistered(user, ul)) {
            user.isAdmin = false;
            ul.users.add(user);
            writeFile(ul);
            UserSensorListFile.getInstance(servletContext).setSensorsToUser(user, SensorListFile.getInstance(servletContext).readFile());
        } else {
            throw new Exception("User already registered.");
        }
    }

    /**
     * Delete a user, removing its entry from the xml db
     *
     * @param username
     * @throws Exception
     */
    public synchronized void deleteUser(String username) throws Exception {
        UserList ul = readFile();
        for (User usr : ul.users) {
            if (username.equals(usr.username)) {
                ul.users.remove(usr);
                writeFile(ul);
                return;
            }
        }
        throw new Exception("User does not exist.");
    }

    /**
     * Search a user in the xml db
     *
     * @param str the string to match
     * @return the list of users that match str
     * @throws Exception
     */
    public synchronized UserList searchUsers(String str) throws Exception {
        UserList ul = readFile();
        UserList returnList = new UserList();
        for (User usr : ul.users) {
            if (usr.username.contains(str) || usr.email.contains(str)) {
                returnList.users.add(usr);
            }
        }
        return returnList;
    }

    /**
     * Check user credentials
     *
     * @param user
     * @return the user, if credentials are ok
     * @throws Exception
     */
    public synchronized User loginUser(User user) throws Exception {
        User usr = getUserByUsername(user.username);
        if (usr.pass.equals(user.pass)) {
            return usr;
        } else {
            throw new Exception("Wrong password.");
        }
    }

    /**
     * @param username
     * @return user information
     * @throws Exception
     */
    public synchronized User getUserByUsername(String username) throws Exception {
        UserList ul = readFile();
        for (User usr : ul.users) {
            if (usr.username.equals(username)) {
                return usr;
            }
        }
        throw new Exception("User does not exist.");
    }

    private synchronized void writeFile(UserList userList) throws Exception {
        Marshaller marsh = context.createMarshaller();
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        marsh.marshal(userList, doc);
        OutputStream out = new FileOutputStream(userFile);
        mngXML.transform(out, doc);
        out.close();
    }

    private boolean isUserRegistered(User user, UserList ul) {
        for (User usr : ul.users) {
            if (usr.username.equals(user.username)) {
                return true;
            }
        }
        return false;
    }

    private void createFile() throws Exception {
        User admin = new User();
        admin.username = "admin";
        admin.pass = "admin";
        admin.email = "admin@AWTSensors.com";
        admin.isAdmin = true;
        UserList ul = new UserList();
        ul.users.add(admin);
        UserSensorListFile.getInstance(servletContext).setSensorsToUser(admin, SensorListFile.getInstance(servletContext).readFile());
        writeFile(ul);
    }
}
