/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.applet;

import it.unibo.aswProject.libraries.http.HTTPClient;
import it.unibo.aswProject.libraries.xml.ManageXML;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import org.w3c.dom.*;
import it.unibo.aswProject.libraries.ui.EntryListCellRenderer;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 *
 * @author enricobenini
 */
public class SensorsControlPanel extends JApplet {

    private ManageXML mngXML;
    private final HTTPClient hc = new HTTPClient();
    private AppletGUI appletGUI;
    private String username;
    private CometValueUpdaterThread cometValueUpdaterThread;
    private SensorDownloadWorker sensorDownloadWorker;

    @Override
    public void init() {
        username = getParameter("username");
        try {
            initHTTPClient();
            mngXML = new ManageXML();
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    appletGUI = new AppletGUI();
                    appletGUI.fillContainer(getContentPane());
                }
            });
        } catch (MalformedURLException | TransformerConfigurationException | ParserConfigurationException | InterruptedException | InvocationTargetException e) {
            System.err.println("createGUI non eseguito con successo");
            Logger.getLogger(SensorsControlPanel.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void initHTTPClient() throws MalformedURLException {
        hc.setSessionId(getParameter("sessionId"));
        // represent the path portion of the URL as a file
        URL url = getDocumentBase();
        File file = new File(url.getPath());
        // get the parent of the file
        String parentPath = file.getParent();
        // construct a new url with the parent path
        URL parentUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(), parentPath);
        hc.setBase(parentUrl);
    }

    @Override
    public void start() {
        if (cometValueUpdaterThread != null && cometValueUpdaterThread.isAlive()) {
            cometValueUpdaterThread.stopComet();
        }
        sensorDownloadWorker = new SensorDownloadWorker();
        sensorDownloadWorker.execute();
    }

    @Override
    public void stop() {
        if (cometValueUpdaterThread.isAlive()) {
            cometValueUpdaterThread.stopComet();
        }
    }

    private class SensorDownloadWorker extends SwingWorker<Void, NodeList> {

        @Override
        protected Void doInBackground() throws Exception {
            NodeList sensors = getSensors();
            publish(sensors);
            return null;
        }

        @Override
        protected void process(java.util.List<NodeList> chunks) {
            NodeList xmlDocs = chunks.get(0);
            Element sensorsList = (Element) xmlDocs.item(0);
            appletGUI.model.removeAllElements();
            for (int i = sensorsList.getChildNodes().getLength() - 1; i >= 0; i--) {
                Element sensorElem = (Element) sensorsList.getChildNodes().item(i);
                String[] listElem = {
                    sensorElem.getAttribute("id"),
                    sensorElem.getTextContent(),
                    sensorElem.getAttribute("value")
                };
                appletGUI.model.addElement(listElem);
            }
            cometValueUpdaterThread = new CometValueUpdaterThread();
            cometValueUpdaterThread.start(); 
        }
        
        private NodeList getSensors() throws Exception {
            // prepare the request xml
            Document data = mngXML.newDocument("getSensors");
            // do request
            Document answer = hc.execute("Sensors", data);
            // get response
            NodeList sensorsList = answer.getElementsByTagName("SensorsList");
            return sensorsList;
        }
    }

    private class AppletGUI {

        private JScrollPane jScrollPane;
        private JList sensorsList;
        private Container contentPane;
        public JLabel errorLabel;
        public DefaultListModel<String[]> model;

        public void fillContainer(Container cp) {
            // Initialize the Swing UI
            contentPane = cp;
            contentPane.setLayout(new BorderLayout());

            model = new DefaultListModel<String[]>();
            sensorsList = new JList(model);
            sensorsList.setCellRenderer(new EntryListCellRenderer());
            jScrollPane = new JScrollPane(sensorsList);
            contentPane.add(jScrollPane, BorderLayout.CENTER);
            
            errorLabel = new JLabel();
            errorLabel.setText("Status: OK");
            contentPane.add(errorLabel, BorderLayout.PAGE_END);
        }
    }

    private class CometValueUpdaterThread extends Thread {

        private boolean runComet = true;

        public CometValueUpdaterThread() {
        }

        public synchronized void stopComet() {
            runComet = false;
        }

        @Override
        public void run() {
            while (runComet) {
                System.out.println(runComet);
                try {
                    // prepare the request xml
                    Document data = mngXML.newDocument("waitEvents");
                    Document answer = hc.execute("Sensors", data);
                    mngXML.transform(System.out, answer);
                    String message = answer.getElementsByTagName("message").item(0).getTextContent();
                    if (message.equals("NewEvent")){
                        runComet = false;
                        sensorDownloadWorker = new SensorDownloadWorker();
                        sensorDownloadWorker.execute();
                    }
                } catch (TransformerException | ParserConfigurationException | SAXException | IOException | DOMException e) {
                    runComet = false;
                    System.out.println(e);
                }
            }
        }
    }
}
