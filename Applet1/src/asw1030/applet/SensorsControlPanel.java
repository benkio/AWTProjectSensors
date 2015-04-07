/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1030.applet;

import asw1030.libraries.commonServiceRequests.SensorRequests;
import asw1030.libraries.http.HTTPClient;
import asw1030.libraries.http.HTTPClientFactory;
import asw1030.libraries.xml.ManageXML;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import org.w3c.dom.*;
import asw1030.libraries.ui.EntryListCellRenderer;

/**
 *
 * @author enricobenini
 */
public class SensorsControlPanel extends JApplet {

    private ManageXML mngXML;
    private HTTPClient hc;
    private final SensorRequests sensorsRequests = new SensorRequests();
    private AppletGUI appletGUI;
    private String username;
    private CometValueUpdaterThread cometValueUpdaterThread;
    private SensorDownloadWorker sensorDownloadWorker;

    @Override
    public void init() {
        username = getParameter("username");
        try {
            hc = HTTPClientFactory.GetHttpClient(getParameter("sessionID"), getDocumentBase());
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
            NodeList sensors = sensorsRequests.getSensors(mngXML,hc);
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
                if (Boolean.valueOf(sensorElem.getAttribute("visible"))){
                    String[] listElem = {
                        sensorElem.getAttribute("id"),
                        sensorElem.getTextContent(),
                        sensorElem.getAttribute("value")
                    };
                    appletGUI.model.addElement(listElem);
                }
            }
            cometValueUpdaterThread = new CometValueUpdaterThread();
            cometValueUpdaterThread.start(); 
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

            model = new DefaultListModel<>();
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
                    String message = sensorsRequests.getNewEvent(mngXML, hc);
                    if (message.equals("NewEvent")){
                        runComet = false;
                        sensorDownloadWorker = new SensorDownloadWorker();
                        sensorDownloadWorker.execute();
                    }
                } catch (Exception e) {
                    runComet = false;
                    System.out.println(e);
                }
            }
        }
    }
}
