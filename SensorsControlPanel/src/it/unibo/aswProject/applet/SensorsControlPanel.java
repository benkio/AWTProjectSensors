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
import java.util.ArrayList;

/**
 *
 * @author enricobenini
 */
public class SensorsControlPanel extends JApplet {

    private ManageXML mngXML;
    private HTTPClient hc = new HTTPClient();
    private AppletGUI appletGUI;
    private String username;
    private CometValueUpdaterThread cometValueUpdaterThread;

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
        } catch (Exception e) {
            System.err.println("createGUI non eseguito con successo");
            Logger.getLogger(SensorsControlPanel.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void start() {
        new SensorDownloadWorker().execute();
        if (cometValueUpdaterThread != null && cometValueUpdaterThread.isAlive()) {
            cometValueUpdaterThread.stopComet();
        }
        cometValueUpdaterThread = new CometValueUpdaterThread();
        cometValueUpdaterThread.start();
    }

    @Override
    public void stop() {
        if (cometValueUpdaterThread.isAlive()) {
            cometValueUpdaterThread.stopComet();
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
        }

        private NodeList getSensors() throws Exception {
            // prepare the request xml
            Document data = mngXML.newDocument("GetSensors");
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
                    Document data = mngXML.newDocument("GetValues");
                    Document answer = hc.execute("Sensors", data);
                    if (runComet) {
                        SwingUtilities.invokeAndWait(new UpdateValues(answer));
                    }
                } catch (Exception e) {
                    runComet = false;
                    System.out.println(e);
                }
            }
        }

        private class UpdateValues implements Runnable {

            private final Document answer;

            public UpdateValues(Document answer) {
                this.answer = answer;
            }

            @Override
            public void run() {
                ArrayList<String[]> sensors = new ArrayList<>();
                try {
                    for (int i = 0; i < appletGUI.model.getSize(); i++) {
                        sensors.add(appletGUI.model.elementAt(i));
                    }
                    appletGUI.model.removeAllElements();
                    mngXML.transform(System.out, answer);
                    NodeList Notifiers = answer.getElementsByTagName("Notify");
                    if (Notifiers.getLength() == 0) {
                        for (String[] elem : sensors) {
                            elem[1] = "Timeout Occurred";
                        }
                    } else {
                        for (int i = 0; i < Notifiers.getLength(); i++) {
                            Element e = (Element) Notifiers.item(i);
                            String sensorId = e.getAttribute("number"); //TODO CHANGE WITH ID
                            for (String[] elem : sensors) {
                                if (elem[0].equals(sensorId)) {
                                    switch (e.getAttribute("kind")) {
                                        case "value":
                                            elem[2] = e.getElementsByTagName("message").item(0).getTextContent();
                                            break;
                                        case "status":
                                            elem[1] = e.getElementsByTagName("message").item(0).getTextContent();
                                            break;
                                    }
                                    break;
                                }
                            }

                        }
                    }
                    for (String[] elem : sensors) {
                        appletGUI.model.addElement(elem);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(SensorsControlPanel.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println(ex.getMessage());
                }

            }
        }
    }

}
