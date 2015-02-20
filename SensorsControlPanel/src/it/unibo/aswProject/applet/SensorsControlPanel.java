/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.aswProject.applet;

import it.unibo.aswProject.libraries.http.HTTPClient;
import it.unibo.aswProject.libraries.xml.ManageXML;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import org.w3c.dom.*;
import it.unibo.aswProject.libraries.ui.EntryListCellRenderer;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author enricobenini
 */
public class SensorsControlPanel extends JApplet {

    private ManageXML mngXML;
    private HTTPClient hc = new HTTPClient();
    private AppletGUI appletGUI;
    private String username;

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
            new SensorDownloadWorker().execute();
        } catch (Exception e) {
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
                    "50" //TODO impostare il vero valore del sensore
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
        public JProgressBar sensorValueProgressBar;
        public JLabel sensorValueLabel;
        private CometValueUpdaterRunnable cometValueUpdaterRunnable;
        private JLabel progressLabel;
        private JPanel progressPanel;

        public void fillContainer(Container cp) {
            // Initialize the Swing UI
            contentPane = cp;
            contentPane.setLayout(new BorderLayout());
            
            model = new DefaultListModel<String[]>();
            sensorsList = new JList(model);
            sensorsList.setCellRenderer(new EntryListCellRenderer());
            jScrollPane = new JScrollPane(sensorsList);

            sensorsList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    JList list = (JList) evt.getSource();
                    if (evt.getClickCount() == 1) { // User has clicked
                        int index = list.locationToIndex(evt.getPoint());
                        String[] row = (String[]) model.getElementAt(index);
                        if (cometValueUpdaterRunnable != null)
                        {
                            cometValueUpdaterRunnable.stopComet();
                        }
                        cometValueUpdaterRunnable = new CometValueUpdaterRunnable(row[0]);
                        new Thread(cometValueUpdaterRunnable).start();
                    }
                }
            });
            contentPane.add(jScrollPane,BorderLayout.CENTER);
        }
    }

    private class CometValueUpdaterRunnable implements Runnable {

        private final String sensorName;
        private boolean runComet = true;

        public CometValueUpdaterRunnable(String sensorName) {
            this.sensorName = sensorName;
        }

        public synchronized void stopComet() {
            runComet = false;
        }

        @Override
        public void run() {
            while (runComet) {
                try {
                    // prepare the request xml
                    Document data = mngXML.newDocument("GetValues");
//                    Document data = mngXML.newDocument("SensorValueRequest");
//                    Element sensorIDTag = data.createElement("sensorName");
//                    Text sensorIDTextNode = data.createTextNode(sensorName);
//                    data.appendChild(sensorIDTag);
                    Document answer = hc.execute("Sensors", data);
                    if (runComet) {
                        SwingUtilities.invokeAndWait(new RunnableImpl(answer));
                    }
                } catch (Exception e) {
                    runComet = false;
                }
            }
        }

        private class RunnableImpl implements Runnable {

            private final Document answer;

            public RunnableImpl(Document answer) {
                this.answer = answer;
            }

            @Override
            public void run() {
                try {
                String newValue = answer.getDocumentElement().getChildNodes().item(0).getTextContent();
                appletGUI.sensorValueLabel.setText(newValue+"%");
                appletGUI.sensorValueProgressBar.setValue(Integer.valueOf(newValue));
                    new ManageXML().transform(System.out, answer);
                } catch (TransformerConfigurationException | ParserConfigurationException ex) {
                    Logger.getLogger(SensorsControlPanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TransformerException | IOException ex) {
                    Logger.getLogger(SensorsControlPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
